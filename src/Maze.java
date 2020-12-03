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

    private Bomber player = new Bomber(0, 0, Sprite.bomber_right);
    private final ArrayList<GameCharacter> enemies = new ArrayList<>();
    private final ArrayList<Grass> grasses = new ArrayList<>();
    private final ArrayList<Entity> blocks = new ArrayList<>();
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private final ArrayList<Item> items = new ArrayList<>(); // Anything else
    private final ArrayList<Flame> flames = new ArrayList<>();

    private MediaPlayer mediaPlayer;

    private int gameStatus = 0;
    /* 0 : Trò chơi đang chạy
     * 1 : Bomber thắng
     * 2 : Bomber mất 1 mạng
     */

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
                    grasses.add(new Grass(j, i, Sprite.grass));
                    switch (currentLine.charAt(j)) {
                        case '#':
                            blocks.add(new Wall(j, i, Sprite.wall));
                            break;
                        case '*':
                            blocks.add(new Brick(j, i, Sprite.brick));
                            break;
                        case 'x':
                            items.add(new Portal(j, i, Sprite.portal, "PORTAL"));
                            blocks.add(new Brick(j, i, Sprite.brick));
                            break;
                        case 'p':
                            player = new Bomber(j, i, Sprite.bomber_right);
                            break;
                        case '1':
                            enemies.add(new Ballom(j, i, Sprite.ballom_left));
                            break;
                        case '2':
                            enemies.add(new Oneal(j, i, Sprite.oneal_left));
                            break;
                        case 'f':
                            items.add(new Item(j, i, Sprite.powerup_flames, "FLAME"));
                            blocks.add(new Brick(j, i, Sprite.brick));
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
        if (!player.expired(timer)) {
            inputProcess(keyInput, timer);
            bomberUpdate(timer);
            enemiesUpdate(timer);

            bombProcess(timer);
            flames.removeIf(f -> f.expired(timer));
            flames.forEach(Entity::update);

            /* Đốt bỏ m* mấy con đứng trong vùng lửa. */
            for (Flame flame : flames) {
                if (player.notDoomedYet() && flame.collideWith(player)) {
                    player.setBroken(Sprite.bomber_dead, timer);
                }
                for (GameCharacter g : enemies) {
                    if (g.notDoomedYet() && flame.collideWith(g)) {
                        g.setBroken(Sprite.mob_dead, timer);
                    }
                }
            }

            blocks.removeIf(e -> e.expired(timer));
            blocks.forEach(Entity::update);

            itemProcess();
        } else {
            System.out.println("GAME OVER");
            System.exit(0);
        }

    }

    /** Render mọi thứ lên màn hình. **/
    public void render(Canvas canvas, GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        grasses.forEach(g -> g.render(gc));
        items.forEach(g -> g.render(gc));
        blocks.forEach(g -> g.render(gc));
        bombs.forEach(g -> g.render(gc));
        flames.forEach(g -> g.render(gc));
        player.render(gc);
        enemies.forEach(g -> g.render(gc));
    }

    /** Return 0 nếu game đang chạy, return 1 nếu qua màn, return 2 nếu bomber thua. **/
    public int levelStatus() {
        if (gameStatus != 0) {
            closeLevel();
        }
        return gameStatus;
    }

    /** Xử lí bom nổ **/
    private void explosive(int xUnit, int yUnit, long timer) {

        /* Thêm flame vào những ô bị bom nổ. */
        flames.add(new Flame(xUnit, yUnit, Sprite.explosion_centre, timer));
        for (int i = 0; i < 4; ++ i) {
            boolean flameIsBlocked = false;
            for (int j = 1; j <= Bomber.FLAME_SIZE; ++ j) {
                for (Entity e : blocks) {
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
            player.velocityUpdate(0, 0);
            return;
        }

        /* Xử lí input liên quan đến hành động của bomber */
        String action = keyInput.get(keyInput.size() - 1);

        player.control(action, bombs, timer);
    }

    /** Xử lí kích nổ bom. **/
    private void bombProcess(long timer) {
        for (Bomb b : bombs) {
            if (!b.collideWith(player)) {
                b.setCollidable();
            }
        }

        /* Cho nổ những quả bom đã hết thời gian. */
        if (!bombs.isEmpty()) {
            for (Bomb b : bombs) {
                if (b.detonated(timer)) {
                    int x = b.getXUnit();
                    int y = b.getYUnit();
                    flames.add(new Flame(x, y, Sprite.explosion_centre, timer));
                    explosive(x, y, timer);
                    player.addBomb();
                }
            }

            bombs.removeIf(b -> b.detonated(timer));
        }
    }

    /** Update vị trí mới cho Bomber. **/
    private void bomberUpdate(long timer) {
        player.update();
        /* Nếu Bomber chưa toang. */
        if (player.notDoomedYet()) {
            for (Entity e : blocks) {
                if (e.isCollidable()) {
                    if (player.collideWith(e)){
                        player.snapCollision(e);
                    }
                }
            }
            for (Bomb bomb : bombs) {
                if (bomb.isCollidable()) {
                    if (player.collideWith(bomb)) {
                        player.snapCollision(bomb);
                    }
                }
            }

            for (GameCharacter g : enemies) {
                if (player.collideWith(g)) {
                    player.setBroken(Sprite.bomber_dead, timer);
                }
            }
        } else if (player.expired(timer)) {
            gameStatus = 2;
        }
    }

    /** Update vị trí mới cho Enemies. **/
    private void enemiesUpdate(long timer) {
        enemies.removeIf(g -> g.expired(timer));
        for (GameCharacter g : enemies) {
            g.update();
            /* Nếu enemy này chưa toang. */
            if (g.notDoomedYet()) {
                g.getDirection(player, blocks, bombs);
            }
        }
    }

    private void itemProcess() {
        for (Item e : items) {
            if (e.collideWith(player)) {
                switch (e.getAbility()) {
                    case "PORTAL":
                        if (enemies.isEmpty()) {
                            gameStatus = 1;
                        }
                        break;
                    case "FLAME":
                        Bomber.FLAME_SIZE ++;
                        break;
                    default:
                        break;
                }
            }
        }
        items.removeIf(e -> e.collideWith(player) && !e.getAbility().equals("PORTAL"));
    }

    /** Gọi hàm này khi hết màn chơi hiện tại. **/
    private void closeLevel() {
        mediaPlayer.stop();
    }
}
