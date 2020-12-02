package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class Flame extends Entity {
    public static final ArrayList<Integer> moveX = new ArrayList<>(
            Arrays.asList(
                    1, -1, 0, 0
            )
    );
    public static final ArrayList<Integer> moveY = new ArrayList<>(
            Arrays.asList(
                    0, 0, 1, -1
            )
    );

    public Flame(double x, double y, ArrayList<Sprite> sprites, long timer) {
        super(x, y, sprites);
        this.collidable = true;
        this.destroyedTimer = timer;
        this.isDoomed = true;
    }

    @Override
    public void update() {
        this.getNextImg();
    }
}
