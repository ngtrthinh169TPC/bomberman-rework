package entities;

import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entity {
    protected double xAxis; /* Coordinate counted from TOP_LEFT corner **/
    protected double yAxis;
    protected Image img;
    protected double realWidth;
    protected double realHeight;

    /** Constructor that converts unit coordinate into canvas coordinate **/
    public Entity(double xUnit, double yUnit, Sprite sprite) {
        this.xAxis = xUnit * Sprite.SCALED_SIZE;
        this.yAxis = yUnit * Sprite.SCALED_SIZE;
        this.img = sprite.getFxImage();
        this.realWidth = sprite.getRealWidth();
        this.realHeight = sprite.getRealHeight();
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, xAxis, yAxis);
    }

    public abstract void update();
}
