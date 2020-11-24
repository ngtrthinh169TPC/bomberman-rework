import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BombermanGame extends Application {
    private GraphicsContext gc;
    private Canvas canvas;
    private Maze maze;

    private final ArrayList<String> keyInput = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        maze = new Maze(1);

        canvas = new Canvas(Sprite.SCALED_SIZE * Maze.WIDTH,
                Sprite.SCALED_SIZE * Maze.HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(
                event -> {
                    String code = event.getCode().toString();
                    //System.out.println(code); // Test input to make new features
                    if (!keyInput.contains(code)) {
                        keyInput.add(code);
                    }
                }
        );

        scene.setOnKeyReleased(
                event -> {
                    String code = event.getCode().toString();
                    keyInput.remove(code);
                }
        );

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                maze.update(keyInput);
                maze.render(canvas, gc);
            }
        };
        timer.start();
    }
}