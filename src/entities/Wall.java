package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Wall extends Entity {
    public Wall(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.collidable = true;
    }

    @Override
    public void update() {
        /* Wall can't be updated **/
    }
}
