package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class Oneal extends GameCharacter {
    public Oneal(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.moveSpeed = 1.2;
        this.velocityUpdate(-1, 0);
    }

    @Override
    public void getDirection(Bomber player, ArrayList<Entity> blocks, ArrayList<Bomb> bombs) {
        int direction = 4;
        int targetDistance = 10;
        for (int i = 0; i < 4; ++ i) {
            for (int j = 1; j < 4; ++ j) {
                if (player.getXUnit() == this.getXUnit() + Entity.moveX.get(i) * j
                        && player.getYUnit() == this.getYUnit() + Entity.moveY.get(i) * j) {
                    direction = i;
                    targetDistance = j;
                    break;
                }
            }
        }

        if (direction < 4) {
            for (int j = 1; j < targetDistance; ++j) {
                for (Entity e : blocks) {
                    if (e.getXUnit() == this.getXUnit() + Entity.moveX.get(direction) * targetDistance
                            && e.getYUnit() == this.getYUnit() + Entity.moveY.get(direction) * targetDistance) {
                        direction = 4;
                    }
                }
            }
        }

        if (direction < 4) {
            this.currentDirection = direction;
            this.velocityUpdate(moveX.get(direction), moveY.get(direction));
            if (!(this.currentDirection == direction)) {
                if (direction % 2 == 0) {
                    this.getNextImg(Sprite.oneal_right, direction);
                } else {
                    this.getNextImg(Sprite.oneal_left, direction);
                }
            }
            for (Entity e : blocks) {
                if (e.isCollidable()) {
                    if (this.collideWith(e)) {
                        this.snapCollision(e);
                        if (this.getXUnit() * Sprite.SCALED_SIZE - this.xLeft < 0) {
                            this.currentDirection = 1;
                            this.velocityUpdate(-1, 0);
                            this.getNextImg(Sprite.oneal_left, direction);
                        }
                        if (this.getXUnit() * Sprite.SCALED_SIZE - this.xLeft > 0) {
                            this.currentDirection = 0;
                            this.velocityUpdate(1, 0);
                            this.getNextImg(Sprite.oneal_right, direction);
                        }
                        if (this.getYUnit() * Sprite.SCALED_SIZE - this.yTop < 0) {
                            this.currentDirection = 3;
                            this.velocityUpdate(0, -1);
                            this.getNextImg(Sprite.oneal_left, direction);
                        }
                        if (this.getXUnit() * Sprite.SCALED_SIZE - this.xLeft > 0) {
                            this.currentDirection = 2;
                            this.velocityUpdate(0, 1);
                            this.getNextImg(Sprite.oneal_right, direction);
                        }
                    }
                }
            }
        } else {
            for (Entity e : blocks) {
                if (e.isCollidable()) {
                    if (this.collideWith(e)){
                        this.snapCollision(e);
                        Random random = new Random();
                        direction = random.nextInt(4);
                        if (!(this.currentDirection == direction)) {
                            if (direction % 2 == 0) {
                                this.getNextImg(Sprite.oneal_right, direction);
                            } else {
                                this.getNextImg(Sprite.oneal_left, direction);
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
                        direction = random.nextInt(4);
                        if (!(this.currentDirection == direction)) {
                            if (direction % 2 == 0) {
                                this.getNextImg(Sprite.oneal_right, direction);
                            } else {
                                this.getNextImg(Sprite.oneal_left, direction);
                            }
                            this.currentDirection = direction;
                            this.velocityUpdate(moveX.get(direction), moveY.get(direction));
                        }
                    }
                }
            }
        }
    }
}
