package entities;

import graphics.Sprite;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;

public class Bomber extends GameCharacter {
    public static final double BOMBER_SPEED = 1.2;
    public static final ArrayList<String> availableCommand = new ArrayList<>(
            Arrays.asList("LEFT", "RIGHT", "UP", "DOWN")
    );

    private static final int WALK_TIME = 6;

    private int changeTimer = 0;
    private String direction = "RIGHT";

    public Bomber(double x, double y, Image img) {
        super(x, y, img);
        this.moveSpeed = BOMBER_SPEED;
    }

    @Override
    public void directionUpdate(ArrayList<String> input) {
        input.removeIf(s -> !availableCommand.contains(s));
        if (input.isEmpty()) {
            this.velocityUpdate(0, 0);
            return;
        }

        if (input.get(input.size() - 1).equals(direction)) {
            changeTimer++;
        } else {
            changeTimer = 0;
        }
        this.direction = input.get(input.size() - 1);

        switch (direction) {
            case "LEFT":
                this.velocityUpdate(-1, 0);
                this.img = Sprite.bomber_left.get((changeTimer / WALK_TIME) % 3).getFxImage();
                break;
            case "RIGHT":
                this.velocityUpdate(1, 0);
                this.img = Sprite.bomber_right.get((changeTimer / WALK_TIME) % 3).getFxImage();
                break;
            case "UP":
                this.velocityUpdate(0, -1);
                this.img = Sprite.bomber_up.get((changeTimer / WALK_TIME) % 3).getFxImage();
                break;
            case "DOWN":
                this.velocityUpdate(0, 1);
                this.img = Sprite.bomber_down.get((changeTimer / WALK_TIME) % 3).getFxImage();
                break;
            default:
                break;
        }
    }
}
