package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Bomb extends Entity {
    private final long detonationTimer;
    public static final long DETONATE_TIME = 2;
    public Bomb(double x, double y, ArrayList<Sprite> sprites, long timer) {
        super (x, y, sprites);
        this.detonationTimer = timer;
    }

    public boolean detonated(long timer) {
        return (timer - detonationTimer) / 1000000000 >= DETONATE_TIME;
    }

    /*public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }*/

    @Override
    public void update() {

    }
}
