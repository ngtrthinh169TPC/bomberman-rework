package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Oneal extends GameCharacter {
    public Oneal(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.moveSpeed = 1.2;
        this.getDirection();
    }

    @Override
    public void getDirection() {

    }
}
