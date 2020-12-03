package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class Ballom extends GameCharacter {
    public Ballom(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.moveSpeed = 0.8;
        this.velocityUpdate(-1, 0);
    }

    @Override
    public void getDirection(Bomber player, ArrayList<Entity> blocks, ArrayList<Bomb> bombs) {
        for (Entity e : blocks) {
            if (e.isCollidable()) {
                if (this.collideWith(e)){
                    this.snapCollision(e);
                    Random random = new Random();
                    int direction = random.nextInt(4);
                    if (!(this.currentDirection == direction)) {
                        if (direction % 2 == 0) {
                            this.getNextImg(Sprite.ballom_right, direction);
                        } else {
                            this.getNextImg(Sprite.ballom_left, direction);
                        }
                        this.currentDirection = direction;
                        this.velocityUpdate(moveX.get(direction), moveY.get(direction));
                    }
                }
            }
        }
        for (Bomb bomb : bombs) {
            if (bomb.isCollidable()) {
                if (this.collideWith(bomb)) {
                    this.snapCollision(bomb);
                    Random random = new Random();
                    int direction = random.nextInt(4);
                    if (!(this.currentDirection == direction)) {
                        if (direction % 2 == 0) {
                            this.getNextImg(Sprite.ballom_right, direction);
                        } else {
                            this.getNextImg(Sprite.ballom_left, direction);
                        }
                        this.currentDirection = direction;
                        this.velocityUpdate(moveX.get(direction), moveY.get(direction));
                    }
                }
            }
        }
    }
}
