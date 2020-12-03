package entities;

import graphics.Sprite;

import java.util.ArrayList;

public class Bomber extends GameCharacter {

    public static final double BOMBER_SPEED = 1.8;
    private final int BOMB_AMOUNT = 1;
    public static int FLAME_SIZE = 1;
    private int availableBomb = BOMB_AMOUNT;

    public Bomber(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.moveSpeed = BOMBER_SPEED;
    }

    @Override
    public void update(){
        if (!this.isDoomed) {
            this.xLeft += this.rightVelocity * this.moveSpeed;
            this.yTop += this.downVelocity * this.moveSpeed;
        } else {
            this.getNextImg();
        }
    }

    @Override
    public void getDirection(Bomber player, ArrayList<Entity> blocks, ArrayList<Bomb> bombs) {
        /* Bomber will not automatically move. */
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
                this.getNextImg(Sprite.bomber_left, 1);
                break;
            case "RIGHT":
                this.velocityUpdate(1, 0);
                this.getNextImg(Sprite.bomber_right, 0);
                break;
            case "UP":
                this.velocityUpdate(0, -1);
                this.getNextImg(Sprite.bomber_up, 3);
                break;
            case "DOWN":
                this.velocityUpdate(0, 1);
                this.getNextImg(Sprite.bomber_down, 2);
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
