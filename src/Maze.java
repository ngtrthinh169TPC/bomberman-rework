import entities.*;
import graphics.Sprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Maze {
    public static int WIDTH;
    public static int HEIGHT;

    private final List<Bomber> players = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final List<StillObject> stillObjects = new ArrayList<>();

    public Maze(int level) {
        try {
            File file = new File("res/levels/Level1.txt");
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
                    stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                    switch (currentLine.charAt(j)) {
                        case '#':
                            stillObjects.add(new Wall(j, i, Sprite.wall.getFxImage()));
                            break;
                        case '*':
                            entities.add(new Brick(j, i, Sprite.brick.getFxImage()));
                            break;
                        case 'x':
                            entities.add(new Portal(j, i, Sprite.portal.getFxImage()));
                            break;
                        case 'p':
                            players.add(
                                    new Bomber(j, i, Sprite.bomber_right.get(0).getFxImage()));
                            break;
                        default:
                            break;
                    }
                }
            }
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                System.out.println(data);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Called from -createMap/BombermanGame.java-");
            e.printStackTrace();
        }
    }

    public void update(ArrayList<String> keyInput) {
        players.forEach(b -> b.directionUpdate(keyInput));

        entities.forEach(Entity::update);
        players.forEach(Bomber::update);
    }

    public void render(Canvas canvas, GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        players.forEach(g -> g.render(gc));
    }
}
