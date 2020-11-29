import entities.*;
import graphics.Sprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Maze {
    public static int WIDTH;
    public static int HEIGHT;

    public static final ArrayList<String> availableCommand = new ArrayList<>(
            Arrays.asList("LEFT", "RIGHT", "UP", "DOWN", "X", "Z")
    );

    private final List<Bomber> players = new ArrayList<>();
    private final List<GameCharacter> enemies = new ArrayList<>();
    private final List<Grass> grasses = new ArrayList<>();
    private final List<Entity> blocks = new ArrayList<>(); // Bricks and Walls
    private final List<Bomb> bombs = new ArrayList<>(); // Bombs
    private final List<Entity> entities = new ArrayList<>(); // Anything else

    //private final List<Entity> stillObjects = new ArrayList<>();

    public Maze(int level) {    //hiển thị lên màn hình các entity của trò chơi
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
            String currentLine = sc.nextLine();
            for (int i = 0; i < HEIGHT; ++ i) {
                currentLine = sc.nextLine();
                for (int j = 0; j < WIDTH; ++ j) {
                    grasses.add(new Grass(j, i, Sprite.grass));
                    switch (currentLine.charAt(j)) {
                        case '#':
                            blocks.add(new Wall(j, i, Sprite.wall));
                            break;
                        case '*':
                            blocks.add(new Brick(j, i, Sprite.brick));
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
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Called from -createMap/BombermanGame.java-");
            e.printStackTrace();
        }
    }

    public void update(ArrayList<String> keyInput) {
        inputProcess(keyInput);

        //enemies.forEach(GameCharacter::update);
        //blocks.forEach(Entity::update);
        //entities.forEach(Entity::update);
        for (Bomber b : players) {
            Entity collidedObject = b.collisionDetected(blocks);
            if (collidedObject == null) {
                b.update();
            } else {
                b.snapCollision(collidedObject);
            }
        }
    }

    public void render(Canvas canvas, GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        grasses.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bombs.forEach(g -> g.render(gc));
        blocks.forEach(g -> g.render(gc));
        enemies.forEach(g -> g.render(gc));
        players.forEach(g -> g.render(gc));
    }

    private void inputProcess(ArrayList<String> keyInput) { /*hàm xử lý input nhập vào từ bàn phím*/
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
            case "X":
                if (players.get(0).haveBomb()) {
                    players.get(0).placeBomb();
                    int bomber1X = players.get(0).getXUnit();
                    int bomber1Y = players.get(0).getYUnit();
                    bombs.add(new Bomb(bomber1X, bomber1Y, Sprite.bomb.get(0)));
                }
                break;
            case "Z":
                if (!bombs.isEmpty()) {
                    bombs.remove(bombs.size() - 1);
                    players.get(0).explodeBomb();
                }
                break;
            default:
                break;
        }
    }
}
