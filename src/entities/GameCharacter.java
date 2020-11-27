package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public abstract class GameCharacter extends Entity {

    private static final int WALK_TIME = 6;
    protected double moveSpeed = 0;
    protected double rightVelocity;
    protected double downVelocity;

    private int frameTimer = 0;
    private int frameNumber = 0;
    private String currentDirection = "RIGHT";

    public GameCharacter(double x, double y, Sprite sprite) {
        super(x, y, sprite);
        this.rightVelocity = 0;
        this.downVelocity = 0;
    }

    public void getNextImg(ArrayList<Sprite> sprites, String direction) {
        if (direction.equals(currentDirection)) {
            this.frameTimer ++;
            if (this.frameTimer >= WALK_TIME) {
                this.frameTimer %= WALK_TIME;
                this.frameNumber = (this.frameNumber + 1) % 3;
            }
        } else {
            this.frameTimer = 0;
            this.frameNumber = 0;
            this.currentDirection = direction;
        }
        this.img = sprites.get(frameNumber).getFxImage();
    }

    @Override
    public void update() {
        this.xLeft += this.rightVelocity * this.moveSpeed;
        this.yTop += this.downVelocity * this.moveSpeed;
    }

    public Entity collisionDetected(List<Entity> entities) {
        for (Entity e : entities) {
            if (this.collideWith(e)) {
                return e;
            }
        }
        return null;
    }

    public abstract void actionUpdate(ArrayList<String> input);

    public void velocityUpdate(double rv, double dv) {
        this.rightVelocity = rv;
        this.downVelocity = dv;
    }

    private boolean collideWith(Entity entity) {
        nextLeft = this.xLeft + this.rightVelocity * this.moveSpeed;
        nextRight = nextLeft + this.realWidth;
        nextTop = this.yTop + this.downVelocity * this.moveSpeed;
        nextBottom = nextTop + this.realHeight;
        return !((nextRight <= entity.xLeft)
                || (nextLeft >= entity.xLeft + entity.realWidth)
                || (nextBottom <= entity.yTop)
                || (nextTop >= entity.yTop + entity.realHeight));
    }

    public void snapCollision(Entity entity) {

        if (this.nextRight > entity.xLeft
                && this.nextLeft < entity.xLeft
                && rightVelocity > 0) {
            this.xLeft = entity.xLeft - this.realWidth;
            if (this.nextTop < entity.yTop + entity.realHeight
                    && this.nextTop > entity.yTop + entity.realHeight - Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop + this.realHeight;
                return;
            }
            if (this.nextBottom > entity.yTop
                    && this.nextBottom < entity.yTop + Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop - this.realHeight;
                return;
            }
            return;
        }

        if (this.nextLeft < entity.xLeft + entity.realWidth
                && this.nextRight > entity.xLeft + entity.realWidth
                && this.rightVelocity < 0) {
            this.xLeft = entity.xLeft + entity.realWidth;
            if (this.nextTop < entity.yTop + entity.realHeight
                    && this.nextTop > entity.yTop + entity.realHeight - Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop + this.realHeight;
                return;
            }
            if (this.nextBottom > entity.yTop
                    && this.nextBottom < entity.yTop + Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop - this.realHeight;
                return;
            }
            return;
        }

        if (this.nextBottom > entity.yTop
                && this.nextTop < entity.yTop
                && this.downVelocity > 0) {
            this.yTop = entity.yTop - this.realHeight;
            if (this.nextLeft < entity.xLeft + entity.realWidth
                    && this.nextLeft > entity.xLeft + entity.realWidth - Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft + this.realWidth;
                return;
            }
            if (this.nextRight > entity.xLeft
                    && this.nextRight < entity.xLeft + Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft - this.realWidth;
                return;
            }
            return;
        }

        if (this.nextTop < entity.yTop + entity.realHeight
                && nextBottom > entity.yTop + entity.realHeight
                && downVelocity < 0) {
            this.yTop = entity.yTop + entity.realHeight;
            if (this.nextLeft < entity.xLeft + entity.realWidth
                    && this.nextLeft > entity.xLeft + entity.realWidth - Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft + this.realWidth;
                return;
            }
            if (this.nextRight > entity.xLeft
                    && this.nextRight < entity.xLeft + Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft - this.realWidth;
                //return;
            }
            // return;
        }
    }
}
