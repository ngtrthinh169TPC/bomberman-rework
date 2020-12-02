package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Brick extends Entity {
    public Brick(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.collidable = true;
        this.destructible = true;
        this.NEXT_SPRITE_TIME = 15;
    }

    @Override
    public void update() {
        if (this.isDoomed) {
            this.getNextImg();
        }
    }
}
