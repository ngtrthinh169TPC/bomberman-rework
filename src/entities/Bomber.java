package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Bomber extends GameCharacter {

    public static final double BOMBER_SPEED = 1.7;
    private final int BOMB_AMOUNT = 1;
    public static final int FLAME_SIZE = 2;
    private int availableBomb = BOMB_AMOUNT;

    public int getFLAME_SIZE() {
        return FLAME_SIZE;
    }

    public Bomber(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.moveSpeed = BOMBER_SPEED;
    }

    public boolean haveBomb() {
        return availableBomb > 0;
    }

    public void dropBomb() {
        if (availableBomb > 0) {
            availableBomb --;
        }
    }

    public void addBomb() {
        availableBomb ++;
    }

    public void control(String action, ArrayList<Bomb> bombs, long timer) {
        switch (action) {
            case "LEFT":
                this.velocityUpdate(-1, 0);
                this.getNextImg(Sprite.bomber_left, action);
                break;
            case "RIGHT":
                this.velocityUpdate(1, 0);
                this.getNextImg(Sprite.bomber_right, action);
                break;
            case "UP":
                this.velocityUpdate(0, -1);
                this.getNextImg(Sprite.bomber_up, action);
                break;
            case "DOWN":
                this.velocityUpdate(0, 1);
                this.getNextImg(Sprite.bomber_down, action);
                break;
            case "SPACE":
                if (this.haveBomb()) {
                    this.dropBomb();
                    bombs.add(new Bomb(this.getXUnit(), this.getYUnit(), Sprite.bomb, timer));
                }
            default:
                break;
        }
    }
}
