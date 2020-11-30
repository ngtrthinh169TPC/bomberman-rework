import entities.*;
import graphics.Sprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

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
    private final List<GameCharacter> ballom = new ArrayList<>();
    private final List<GameCharacter> oneal = new ArrayList<>();
    private final List<Grass> grasses = new ArrayList<>();
    private final List<Entity> bricks = new ArrayList<>(); // Bricks and Walls
    private final List<Entity> walls = new ArrayList<>();
    private final List<Bomb> bombs = new ArrayList<>(); // Bombs
    private final List<Entity> entities = new ArrayList<>(); // Anything else
    private final List<Bomb> bomb_explosive = new ArrayList<>();

    /** Khởi tạo màn chơi. **/
    public Maze(int level) {
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
            try {
                for (int i = 0; i < HEIGHT; ++ i) {
                    currentLine = sc.nextLine();
                    for (int j = 0; j < WIDTH; ++ j) {
                        grasses.add(new Grass(j, i, Sprite.grass));
                        switch (currentLine.charAt(j)) {
                            case '#':
                                walls.add(new Wall(j, i, Sprite.wall));
                                break;
                            case '*':
                                bricks.add(new Brick(j, i, Sprite.brick));
                                break;
                            case 'x':
                                entities.add(new Portal(j, i, Sprite.portal));
                                break;
                            case 'p':
                                players.add(new Bomber(j, i, Sprite.bomber_right.get(0)));
                                break;
                            case '1':
                                ballom.add(new Ballom(j, i, Sprite.ballom_left.get(0)));
                                break;
                            case '2':
                                oneal.add(new Oneal(j, i, Sprite.oneal_left.get(0)));
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("The file Level" + level + ".txt is not in the correct form");
                //e.printStackTrace();
                System.exit(0);
            } finally {
                sc.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: Level" + level + ".txt");
            //e.printStackTrace();
            System.exit(0);
        }
    }

    public void update(ArrayList<String> keyInput, long timer) {    //xử lý bomb
        inputProcess(keyInput, timer);
        if (!bombs.isEmpty()) {
            for (Bomb b : bombs) {
                if ((timer - b.getDetonationTimer()) / 1000000000 >= Bomb.DETONATE_TIME) {
                    System.out.println("BOOM");
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
                bomb_explosive.removeAll(bomb_explosive);
                System.out.println("Explosive");
            }
        }

        for (Bomber b : players) {
            Entity collidedObject = b.collisionDetected(bricks);
            if (collidedObject == null) {
                b.update();
            } else {
                b.snapCollision(collidedObject);
            }
        }

        for (Bomber b : players) {
            Entity collidedObject = b.collisionDetected(walls);
            if (collidedObject == null) {
                b.update();
            } else {
                b.snapCollision(collidedObject);
            }
        }
    }

    public void explosive(int xUnit, int yUnit, long timer) {

        for (Grass e : grasses) {
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

        for (Entity w : walls) {
            if ((w.getXUnit() == xUnit && w.getYUnit() == yUnit - 1)
                    || (w.getXUnit() == xUnit && w.getYUnit() == yUnit + 1)
                    || (w.getXUnit() == xUnit - 1 && w.getYUnit() == yUnit)
                    || (w.getXUnit() == xUnit + 1 && w.getYUnit() == yUnit)) {
                bomb_explosive.removeIf(b -> b.getXUnit() == w.getXUnit() && b.getYUnit() == w.getYUnit());
            }
        }

        bricks.removeIf(e -> (e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1)
                || (e.getXUnit() == xUnit && e.getYUnit() == yUnit + 1)
                || (e.getXUnit() == xUnit - 1 && e.getYUnit() == yUnit)
                || (e.getXUnit() == xUnit + 1 && e.getYUnit() == yUnit));

        ballom.removeIf(e -> (e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1)
                || (e.getXUnit() == xUnit && e.getYUnit() == yUnit + 1)
                || (e.getXUnit() == xUnit - 1 && e.getYUnit() == yUnit)
                || (e.getXUnit() == xUnit + 1 && e.getYUnit() == yUnit));

        oneal.removeIf(e -> (e.getXUnit() == xUnit && e.getYUnit() == yUnit - 1)
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

    /** Render là render. **/
    public void render(Canvas canvas, GraphicsContext gc) { //render mọi thứ lên màn hình
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        grasses.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bombs.forEach(g -> g.render(gc));
        bricks.forEach(g -> g.render(gc));
        walls.forEach(g -> g.render(gc));
        bomb_explosive.forEach(g -> g.render(gc));
        ballom.forEach(g -> g.render(gc));
        oneal.forEach(g -> g.render(gc));
        players.forEach(g -> g.render(gc));
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
}
