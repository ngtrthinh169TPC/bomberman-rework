package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class SpeedItem extends Entity {
    public SpeedItem(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        isSpeedItem = true;
        isBombItem = false;
        isFlameItem = false;
    }

    @Override
    public void update () {

    }
}
