import entities.*;
import graphics.Sprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Maze {
    public static int WIDTH;
    public static int HEIGHT;

    public static final ArrayList<String> availableCommand = new ArrayList<>(
            Arrays.asList("LEFT", "RIGHT", "UP", "DOWN", "SPACE")
    );

    private final ArrayList<Bomber> players = new ArrayList<>();
    private final ArrayList<GameCharacter> enemies = new ArrayList<>();
    private final ArrayList<Entity> environment = new ArrayList<>();
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private final ArrayList<Entity> entities = new ArrayList<>(); // Anything else
    private final ArrayList<Flame> flames = new ArrayList<>();

    private MediaPlayer mediaPlayer;

    /** Khởi tạo màn chơi. **/
    public Maze(int level, String audioFile) {
        try {
            File file = new File("res/levels/Level" + level + ".txt");
            Scanner sc = new Scanner(file);
            int currentLevel = sc.nextInt();
            if (currentLevel != level) {
                System.out.println("Something is wrong." +
                        " Might be the level file or the numbering");
                System.exit(0);
            }
            HEIGHT = sc.nextInt();
            WIDTH = sc.nextInt();
            String currentLine;
            sc.nextLine();
            for (int i = 0; i < HEIGHT; ++i) {
                currentLine = sc.nextLine();
                for (int j = 0; j < WIDTH; ++j) {
                    environment.add(new Grass(j, i, Sprite.grass));
                    switch (currentLine.charAt(j)) {
                        case '#':
                            environment.add(new Wall(j, i, Sprite.wall));
                            break;
                        case '*':
                            environment.add(new Brick(j, i, Sprite.brick));
                            break;
                        case 'x':
                            entities.add(new Portal(j, i, Sprite.portal));
                            break;
                        case 'p':
                            players.add(new Bomber(j, i, Sprite.bomber_right));
                            break;
                        case '1':
                            enemies.add(new Ballom(j, i, Sprite.ballom_left));
                            break;
                        case '2':
                            enemies.add(new Oneal(j, i, Sprite.oneal_left));
                            break;
                        default:
                            break;
                    }
                }
            }
            sc.close();

            String bgAudioPath = "res/audio/" + audioFile + ".mp3";
            Media backgroundAudio = new Media(new File(bgAudioPath).toURI().toString());
            mediaPlayer = new MediaPlayer(backgroundAudio);
            mediaPlayer.setAutoPlay(true);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: Level" + level + ".txt");
            //e.printStackTrace();
            System.exit(0);
        } catch (NoSuchElementException e) {
            System.out.println("The file Level" + level + ".txt is not in the correct form");
            //e.printStackTrace();
            System.exit(0);
        }
    }

    /** Cập nhật trạng thái màn chơi. **/
    public void update(ArrayList<String> keyInput, long timer) {
        if (!players.get(0).expired(timer)) {
            inputProcess(keyInput, timer);
            bomberUpdate(timer);
            enemiesUpdate(timer);

            bombProcess(timer);
            flames.removeIf(f -> f.expired(timer));
            flames.forEach(Entity::update);

            /* Đốt bỏ m* mấy con đứng trong vùng lửa. */
            for (Flame flame : flames) {
                for (Bomber b : players) {
                    if (b.notDoomedYet() && flame.collideWith(b)) {
                        b.setBroken(Sprite.bomber_dead, timer);
                    }
                }
                for (GameCharacter g : enemies) {
                    if (g.notDoomedYet() && flame.collideWith(g)) {
                        g.setBroken(Sprite.mob_dead, timer);
                    }
                }
            }

            environment.removeIf(e -> e.expired(timer));
            environment.forEach(Entity::update);
        } else {
            System.out.println("GAME OVER");
            System.exit(0);
        }

    }

    /** Render mọi thứ lên màn hình. **/
    public void render(Canvas canvas, GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        environment.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bombs.forEach(g -> g.render(gc));
        flames.forEach(g -> g.render(gc));
        players.forEach(g -> g.render(gc));
        enemies.forEach(g -> g.render(gc));
    }

    /** Return 0 nếu game đang chạy, return 1 nếu qua màn, return 2 nếu bomber thua. **/
    public int levelStatus(long timer) {
        if (enemies.isEmpty()) {
            closeLevel();
            return 1;
        }
        if (players.get(0).expired(timer)) {
            return 2;
        }
        return 0;
    }

    /** Xử lí bom nổ **/
    private void explosive(int xUnit, int yUnit, long timer) {

        /* Thêm flame vào những ô bị bom nổ. */
        flames.add(new Flame(xUnit, yUnit, Sprite.explosion_centre, timer));
        for (int i = 0; i < 4; ++ i) {
            boolean flameIsBlocked = false;
            for (int j = 1; j <= Bomber.FLAME_SIZE; ++ j) {
                for (Entity e : environment) {
                    /* Gặp Wall hoặc Brick thì đánh dấu để dừng lại */
                    if (e.isCollidable()
                            && e.getXUnit() == xUnit + Entity.moveX.get(i) * j
                            && e.getYUnit() == yUnit + Entity.moveY.get(i) * j) {
                        flameIsBlocked = true;
                        if (e.isDestructible()) {
                            e.setBroken(Sprite.brick_broken, timer);
                        }
                    }
                }

                if (flameIsBlocked) {
                    break;
                }

                if (j < Bomber.FLAME_SIZE) {
                    flames.add(new Flame(xUnit + Entity.moveX.get(i) * j,
                            yUnit + Entity.moveY.get(i) * j, Sprite.explosion_middle.get(i), timer));
                } else {
                    flames.add(new Flame(xUnit + Entity.moveX.get(i) * j,
                            yUnit + Entity.moveY.get(i) * j, Sprite.explosion_end.get(i), timer));
                }
            }
        }
    }

    /** Xử lí input từ bàn phím. **/
    private void inputProcess(ArrayList<String> keyInput, long timer) {

        /* Lọc input không xử lí */
        keyInput.removeIf(s -> !availableCommand.contains(s));
        if (keyInput.isEmpty()) {
            players.get(0).velocityUpdate(0, 0);
            return;
        }

        /* Xử lí input liên quan đến hành động của bomber */
        String action = keyInput.get(keyInput.size() - 1);

        players.get(0).control(action, bombs, timer);
    }

    /** Xử lí kích nổ bom. **/
    private void bombProcess(long timer) {
        /* Cho nổ những quả bom đã hết thời gian. */
        if (!bombs.isEmpty()) {
            for (Bomb b : bombs) {
                if (b.detonated(timer)) {
                    int x = b.getXUnit();
                    int y = b.getYUnit();
                    flames.add(new Flame(x, y, Sprite.explosion_centre, timer));
                    explosive(x, y, timer);
                    players.get(0).addBomb();
                }
            }

            bombs.removeIf(b -> b.detonated(timer));
        }
    }

    /** Update vị trí mới cho Bomber. **/
    private void bomberUpdate(long timer) {
        for (Bomber b : players) {
            b.update();
            /* Nếu Bomber chưa toang. */
            if (players.get(0).notDoomedYet()) {
                Entity collidedBrick = b.collisionDetected(environment);
                if (collidedBrick != null) {
                    b.snapCollision(collidedBrick);
                }

                for (GameCharacter g : enemies) {
                    if (b.collideWith(g)) {
                        b.setBroken(Sprite.bomber_dead, timer);
                    }
                }
            }
        }
    }

    /** Update vị trí mới cho Enemies. **/
    private void enemiesUpdate(long timer) {
        enemies.removeIf(g -> g.expired(timer));
        for (GameCharacter g : enemies) {
            g.update();
            /* Nếu enemy này chưa toang. */
            if (players.get(0).notDoomedYet()) {
                Entity collidedBrick = g.collisionDetected(environment);
                if (collidedBrick != null) {
                    g.snapCollision(collidedBrick);
                    g.getDirection();
                }
            }
        }
    }

    /** Gọi hàm này khi hết màn chơi hiện tại. **/
    private void closeLevel() {
        mediaPlayer.stop();
    }
}
