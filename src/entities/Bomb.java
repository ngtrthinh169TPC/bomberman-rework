package entities;

import graphics.Sprite;

public class Bomb extends Entity {
    private final long detonationTimer;
    public static final long DETONATE_TIME = 3;
    public Bomb(int x, int y, Sprite sprite, long timer) {
        super (x, y, sprite);
        this.detonationTimer = timer;
    }

    public long getDetonationTimer() {
        return detonationTimer;
    }

    @Override
    public void update() {

    }
}
