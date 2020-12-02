import entities.Bomber;
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
    private Stage stage;

    private final ArrayList<String> keyInput = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        switchLevel(1);

        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                maze.update(keyInput, l);
                maze.render(canvas, gc);
                if (maze.levelStatus(l) == 1) {
                    switchLevel(2);
                }
            }
        };
        timer.start();
    }

    private void switchLevel(int level) {
        maze = new Maze(level);
        canvas = new Canvas(Sprite.SCALED_SIZE * Maze.WIDTH,
                Sprite.SCALED_SIZE * Maze.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        maze.render(canvas, gc);
        stage.setScene(scene);
        scene.setOnKeyPressed(
                event -> {
                    String code = event.getCode().toString();
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
    }
}