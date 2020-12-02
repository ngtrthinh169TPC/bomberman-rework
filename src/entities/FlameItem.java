package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class FlameItem extends Entity {
    public FlameItem(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        isFlameItem = true;
        isBombItem = false;
        isSpeedItem = false;
    }

    @Override
    public void update () {

    }
}
