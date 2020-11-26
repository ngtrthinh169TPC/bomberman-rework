package entities;

import graphics.Sprite;

public class Grass extends Entity {
    public Grass(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public void update() {
        /* Grass can't be updated **/
    }
}