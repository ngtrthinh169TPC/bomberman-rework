package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Bomber extends GameCharacter {

    public static final double BOMBER_SPEED = 0.8;
    public static final int BOMB_AMOUNT = 1;
    private int availableBomb = BOMB_AMOUNT;

    public Bomber(double x, double y, Sprite sprite) {
        super(x, y, sprite);
        this.moveSpeed = BOMBER_SPEED;
    }

    @Override
    public void actionUpdate(ArrayList<String> input) {

    }

    public boolean haveBomb() {
        return availableBomb > 0;
    }

    public void placeBomb() {
        if (availableBomb > 0) {
            availableBomb --;
        }
    }

    public void addBomb() {
        availableBomb ++;
    }
}
