package entities;

import graphics.Sprite;

public class Wall extends Entity {
    public Wall(int x, int y, Sprite sprite) {
        super(x, y, sprite);
        this.collidable = true;
    }

    @Override
    public void update() {
        /* Wall can't be updated **/
    }
}
