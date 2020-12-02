package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Grass extends Entity {
    public Grass(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
    }

    @Override
    public void update() {
        /* Grass can't be updated **/
    }
}