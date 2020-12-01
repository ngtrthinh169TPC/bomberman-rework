package entities;

import graphics.Sprite;

public class Brick extends Entity {
    public Brick(double x, double y, Sprite sprite) {
        super(x, y, sprite);
        this.collidable = true;
        this.destructible = true;
    }

    @Override
    public void update() {
        /* Check if this was destroyed by flames **/
    }
}
