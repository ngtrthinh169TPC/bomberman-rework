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

    private final List<Bomber> players = new ArrayList<>();
    private final List<GameCharacter> enemies = new ArrayList<>();
    private final List<Entity> environment = new ArrayList<>();
    private final List<Bomb> bombs = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>(); // Anything else
    private final List<Bomb> bomb_explosive = new ArrayList<>();

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
                            players.add(new Bomber(j, i, Sprite.bomber_right.get(0)));
                            break;
                        case '1':
                            enemies.add(new Ballom(j, i, Sprite.ballom_left.get(0)));
                            break;
                        case '2':
                            enemies.add(new Oneal(j, i, Sprite.oneal_left.get(0)));
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

        inputProcess(keyInput, timer);
        bombProcess(timer);
        bomberUpdate();
    }

    /** Render là render. **/
    public void render(Canvas canvas, GraphicsContext gc) { //render mọi thứ lên màn hình
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        environment.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bombs.forEach(g -> g.render(gc));
        bomb_explosive.forEach(g -> g.render(gc));
        enemies.forEach(g -> g.render(gc));
        players.forEach(g -> g.render(gc));
    }

    /** Return 0 nếu game đang chạy, return 1 nếu qua màn, return 2 nếu bomber thua. **/
    public int levelStatus() {
        if (enemies.isEmpty()) {
            closeLevel();
            return 1;
        }
        return 0;
    }

    /** Xử lí bom nổ **/
    private void explosive(int xUnit, int yUnit, long timer) {

        /* Thêm flame vào những ô bị bom nổ. */
        for (Entity e : environment) {
            if (e.isCollidable()) {
                continue;
            }
            if (e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1) {
                bomb_explosive.add(new Bomb(e.getXUnit(), e.getYUnit(), Sprite.explosion_vertical_top_last.get(0), timer));
            }
            if (e.getXUnit() == xUnit && e.getYUnit() == yUnit + 1) {
                bomb_explosive.add(new Bomb(e.getXUnit(), e.getYUnit(), Sprite.explosion_vertical_down_last.get(0), timer));
            }
            if (e.getXUnit() == xUnit - 1 && e.getYUnit() == yUnit) {
                bomb_explosive.add(new Bomb(e.getXUnit(), e.getYUnit(), Sprite.explosion_horizontal_left_last.get(0), timer));
            }
            if (e.getXUnit() == xUnit + 1 && e.getYUnit() == yUnit) {
                bomb_explosive.add(new Bomb(e.getXUnit(), e.getYUnit(), Sprite.explosion_horizontal_right_last.get(0), timer));
            }
        }

        for (Entity e : environment) {
            if (e.isCollidable()) {
                if ((e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1)
                        || (e.getXUnit() == xUnit && e.getYUnit() == yUnit + 1)
                        || (e.getXUnit() == xUnit - 1 && e.getYUnit() == yUnit)
                        || (e.getXUnit() == xUnit + 1 && e.getYUnit() == yUnit)) {
                    bomb_explosive.removeIf(b -> b.getXUnit() == e.getXUnit()
                            && b.getYUnit() == e.getYUnit());
                }
            }
        }

        environment.removeIf(e -> ((e.isDestructible())
                && ((e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1)
                || (e.getXUnit() == xUnit && e.getYUnit() == yUnit + 1)
                || (e.getXUnit() == xUnit - 1 && e.getYUnit() == yUnit)
                || (e.getXUnit() == xUnit + 1 && e.getYUnit() == yUnit))));

        enemies.removeIf(e -> (e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1)
                || (e.getXUnit() == xUnit && e.getYUnit() == yUnit + 1)
                || (e.getXUnit() == xUnit - 1 && e.getYUnit() == yUnit)
                || (e.getXUnit() == xUnit + 1 && e.getYUnit() == yUnit));

        for (Bomber p : players) {
            int x = p.getXUnit();
            int y = p.getYUnit();
            if ((p.getXUnit() == xUnit && p.getYUnit() == yUnit - 1)
                    || (p.getXUnit() == xUnit && p.getYUnit() == yUnit + 1)
                    || (p.getXUnit() == xUnit - 1 && p.getYUnit() == yUnit)
                    || (p.getXUnit() == xUnit + 1 && p.getYUnit() == yUnit)
                    || (p.getXUnit() == xUnit && p.getYUnit() == yUnit)) {
                players.remove(p);
                players.add(new Bomber(x, y, Sprite.bomber_dead.get(0)));
            }
        }
    }

    /** Xử lí input từ bàn phím. **/
    private void inputProcess(ArrayList<String> keyInput, long timer) {
        keyInput.removeIf(s -> !availableCommand.contains(s));
        if (keyInput.isEmpty()) {
            players.get(0).velocityUpdate(0, 0);
            return;
        }

        String currentAction = keyInput.get(keyInput.size() - 1);

        switch (currentAction) {
            case "LEFT":
                players.get(0).velocityUpdate(-1, 0);
                players.get(0).getNextImg(Sprite.bomber_left, currentAction);
                break;
            case "RIGHT":
                players.get(0).velocityUpdate(1, 0);
                players.get(0).getNextImg(Sprite.bomber_right, currentAction);
                break;
            case "UP":
                players.get(0).velocityUpdate(0, -1);
                players.get(0).getNextImg(Sprite.bomber_up, currentAction);
                break;
            case "DOWN":
                players.get(0).velocityUpdate(0, 1);
                players.get(0).getNextImg(Sprite.bomber_down, currentAction);
                break;
            case "SPACE":
                if (players.get(0).haveBomb()) {
                    players.get(0).placeBomb();
                    int bomber1X = players.get(0).getXUnit();
                    int bomber1Y = players.get(0).getYUnit();
                    bombs.add(new Bomb(bomber1X, bomber1Y, Sprite.bomb.get(0), timer));
                }
                break;
            default:
                break;
        }
    }

    /** Xử lí trạng thái bom. **/
    private void bombProcess(long timer) {
        if (!bombs.isEmpty()) {
            for (Bomb b : bombs) {
                if ((timer - b.getDetonationTimer()) / 1000000000 >= Bomb.DETONATE_TIME) {
                    int x = b.getXUnit();
                    int y = b.getYUnit();
                    bombs.remove(b);
                    bomb_explosive.add(new Bomb(x, y, Sprite.bomb_exploded.get(0), timer));
                    explosive(x, y, timer);
                    players.get(0).addBomb();
                    if (bombs.isEmpty()) {
                        break;
                    }
                }
            }
        }

        if (!bomb_explosive.isEmpty()) {
            Bomb p = bomb_explosive.get(0);
            if ((timer - p.getDetonationTimer()) / 1000000000 >= Bomb.DETONATE_TIME) {
                bomb_explosive.clear();
            }
        }
    }

    /** Update Bomber status **/
    private void bomberUpdate() {
        for (Bomber b : players) {
            Entity collidedBrick = b.collisionDetected(environment);
            if (collidedBrick == null) {
                b.update();
            } else {
                b.snapCollision(collidedBrick);
            }
        }
    }

    private void closeLevel() {
        mediaPlayer.stop();
    }
}
