package entities;

import graphics.Sprite;

import java.util.ArrayList;

public abstract class GameCharacter extends Entity {

    private static final int WALK_TIME = 6;
    protected double moveSpeed = 0;
    protected double rightVelocity;
    protected double downVelocity;

    private int frameTimer = 0;
    private int frameNumber = 0;
    protected int currentDirection = 0;

    public GameCharacter(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.rightVelocity = 0;
        this.downVelocity = 0;
        this.collidable = true;
        this.destructible = true;
    }

    public abstract void getDirection(Bomber player, ArrayList<Entity> blocks, ArrayList<Bomb> bombs);

    @Override
    public void update() {
        if (!this.isDoomed) {
            this.xLeft += this.rightVelocity * this.moveSpeed;
            this.yTop += this.downVelocity * this.moveSpeed;
        }
        this.getNextImg();
    }

    /** Chuyển sang sprite tiếp theo để tạo hiệu ứng di chuyển. **/
    public void getNextImg(ArrayList<Sprite> sprites, int direction) {
        if (direction == currentDirection) {
            this.frameTimer ++;
            if (this.frameTimer >= WALK_TIME) {
                this.frameTimer %= WALK_TIME;
                this.frameNumber = (this.frameNumber + 1) % 3;
                // 3 is the number of sprites for an entity
            }
        } else {
            this.sprites = sprites;
            this.frameTimer = 0;
            this.frameNumber = 0;
            this.currentDirection = direction;
        }
        this.img = this.sprites.get(frameNumber).getFxImage();
    }

    /** Cập nhật vận tốc, aka hướng di chuyển. **/
    public void velocityUpdate(double rv, double dv) {
        this.rightVelocity = rv;
        this.downVelocity = dv;
    }

    /** Xử lí khi xảy ra va chạm **/
    public void snapCollision(Entity entity) {

        // Va chạm ở phía phải nhân vật
        if (this.nextRight > entity.xLeft
                && this.nextLeft < entity.xLeft
                && rightVelocity > 0) {
            this.xLeft = entity.xLeft - this.realWidth;
            if (this.nextTop < entity.yTop + entity.realHeight
                    && this.nextTop > entity.yTop + entity.realHeight - Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop + entity.realHeight;
                return;
            }
            if (this.nextBottom > entity.yTop
                    && this.nextBottom < entity.yTop + Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop - this.realHeight;
                return;
            }
            return;
        }

        // Va chạm ở phía trái nhân vật
        if (this.nextLeft < entity.xLeft + entity.realWidth
                && this.nextRight > entity.xLeft + entity.realWidth
                && this.rightVelocity < 0) {
            this.xLeft = entity.xLeft + entity.realWidth;
            if (this.nextTop < entity.yTop + entity.realHeight
                    && this.nextTop > entity.yTop + entity.realHeight - Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop + entity.realHeight;
                return;
            }
            if (this.nextBottom > entity.yTop
                    && this.nextBottom < entity.yTop + Sprite.CORNER_SNAP) {
                this.yTop = entity.yTop - this.realHeight;
                return;
            }
            return;
        }

        // Va chạm ở phía dưới nhân vật
        if (this.nextBottom > entity.yTop
                && this.nextTop < entity.yTop
                && this.downVelocity > 0) {
            this.yTop = entity.yTop - this.realHeight;
            if (this.nextLeft < entity.xLeft + entity.realWidth
                    && this.nextLeft > entity.xLeft + entity.realWidth - Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft + entity.realWidth;
                return;
            }
            if (this.nextRight > entity.xLeft
                    && this.nextRight < entity.xLeft + Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft - this.realWidth;
                return;
            }
            return;
        }

        // Va chạm ở phía trên nhân vật
        if (this.nextTop < entity.yTop + entity.realHeight
                && nextBottom > entity.yTop + entity.realHeight
                && downVelocity < 0) {
            this.yTop = entity.yTop + entity.realHeight;
            if (this.nextLeft < entity.xLeft + entity.realWidth
                    && this.nextLeft > entity.xLeft + entity.realWidth - Sprite.CORNER_SNAP) {
                this.xLeft = entity.xLeft + entity.realWidth;
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
