package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class Flame extends Entity {
    private final long expireTimer;
    public static final double EXPIRE_TIME = 0.6;
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
        this.expireTimer = timer;
        this.NEXT_SPRITE_TIME = 12;
    }

    public boolean expired(long timer) {
        return (double)(timer - expireTimer) / 1000000000 >= EXPIRE_TIME;
    }

    @Override
    public void update() {
        /* Nothing to do here. */
    }
}
