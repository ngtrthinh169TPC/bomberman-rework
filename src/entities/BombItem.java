package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class BombItem extends Entity {
    public BombItem(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        isBombItem = true;
        isFlameItem = false;
        isSpeedItem= false;
    }

    @Override
    public void update () {

    }
}
