package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Brick extends Entity {
    public Brick(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.collidable = true;
        this.destructible = true;
    }

    @Override
    public void update() {
        /* Check if this was destroyed by flames **/
    }
}
