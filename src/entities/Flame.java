package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class Flame extends Entity {
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
