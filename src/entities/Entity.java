package entities;

import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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
}
