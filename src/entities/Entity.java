package entities;

import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public abstract class Entity {
    protected double xLeft; /* Coordinate counted from TOP_LEFT corner **/
    protected double yTop;
    protected Image img; /* Current sprite's image **/
    protected double realWidth; /* Size in canvas **/
    protected double realHeight;
    protected boolean collidable;
    protected boolean destructible;

    protected double nextLeft;
    protected double nextTop;
    protected double nextRight;
    protected double nextBottom;

    /** Constructor that converts unit coordinate into canvas coordinate **/
    public Entity(double xUnit, double yUnit, Sprite sprite) {
        this.xLeft = xUnit * Sprite.SCALED_SIZE;
        this.yTop = yUnit * Sprite.SCALED_SIZE;
        this.img = sprite.getFxImage();
        this.realWidth = sprite.getRealWidth() * Sprite.SCALE_RATIO;
        this.realHeight = sprite.getRealHeight() * Sprite.SCALE_RATIO;
        this.collidable = false;
        this.destructible = false;
    }

    public int getXUnit() {
        return (int)(xLeft + Sprite.DEFAULT_SIZE) / Sprite.SCALED_SIZE;
    }

    public int getYUnit() {
        return (int)(yTop + Sprite.DEFAULT_SIZE) / Sprite.SCALED_SIZE;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, xLeft, yTop);
    }

    public abstract void update();

    /** Phát hiện va chạm **/
    public Entity collisionDetected(List<Entity> entities) {
        for (Entity e : entities) {
            if (e.isCollidable()) {
                if (this.collideWith(e)) {
                    return e;
                }
            }
        }
        return null;
    }

    /** Kiểm tra va chạm **/
    private boolean collideWith(Entity entity) {
        this.nextLeft = this.xLeft;
        this.nextRight = this.nextLeft + this.realWidth;
        this.nextTop = this.yTop;
        this.nextBottom = this.nextTop + this.realHeight;
        return !((this.nextRight <= entity.xLeft)
                || (this.nextLeft >= entity.xLeft + entity.realWidth)
                || (this.nextBottom <= entity.yTop)
                || (this.nextTop >= entity.yTop + entity.realHeight));
    }
}
