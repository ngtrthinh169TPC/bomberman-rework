import graphics.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
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

        String bgAudioPath = "res/audio/03_StageTheme.mp3";
        Media backgroundAudio;
        MediaPlayer mediaPlayer = null;

        try {
            backgroundAudio = new Media(new File(bgAudioPath).toURI().toString());
            mediaPlayer = new MediaPlayer(backgroundAudio);
            mediaPlayer.setAutoPlay(true);

        } catch (MediaException e) {
            System.out.println("Audio file: " + bgAudioPath + " is not found");
        }
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }

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

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                maze.update(keyInput, l);
                maze.render(canvas, gc);
            }
        };
        timer.start();
    }
}