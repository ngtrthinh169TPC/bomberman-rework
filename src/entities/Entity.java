package entities;

import graphics.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Entity {
    public static final ArrayList<Integer> moveX = new ArrayList<>(
            Arrays.asList(
                    1, -1, 0, 0
            )
    );
    public static final ArrayList<Integer> moveY = new ArrayList<>(
            Arrays.asList(
                    0, 0, 1, -1
            )
    );
    public static final int NEXT_SPRITE_TIME = 15;
    public static final double EXPIRE_TIME = 0.7;

    protected double xLeft; /* Coordinate counted from TOP_LEFT corner **/
    protected double yTop;
    protected Image img; /* Current sprite's image **/
    protected double realWidth; /* Size in canvas **/
    protected double realHeight;
    protected boolean collidable;
    protected boolean destructible;
    protected boolean isDoomed = false;
    protected long destroyedTimer;

    protected double nextLeft;
    protected double nextTop;
    protected double nextRight;
    protected double nextBottom;

    private int frameTimer = 0;
    private int frameNumber = 0;
    private ArrayList<Sprite> sprites;

    /** Constructor that converts unit coordinate into canvas coordinate **/
    public Entity(double xUnit, double yUnit, ArrayList<Sprite> sprites) {
        this.xLeft = xUnit * Sprite.SCALED_SIZE;
        this.yTop = yUnit * Sprite.SCALED_SIZE;
        this.sprites = sprites;
        this.img = sprites.get(0).getFxImage();
        this.realWidth = sprites.get(0).getRealWidth() * Sprite.SCALE_RATIO;
        this.realHeight = sprites.get(0).getRealHeight() * Sprite.SCALE_RATIO;
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

    public boolean notDoomedYet() {
        return !isDoomed;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, xLeft, yTop);
    }

    public boolean expired(long timer) {
        if (!this.isDoomed) {
            return false;
        }
        return (double)(timer - destroyedTimer) / 1000000000 >= EXPIRE_TIME;
    }

    /** Dùng cho Brick, Enemy và Bomber bị đốt. **/
    public void setBroken(ArrayList<Sprite> sprites, long timer) {
        this.isDoomed = true;
        this.sprites = sprites;
        this.img = sprites.get(0).getFxImage();
        this.destroyedTimer = timer;
    }

    public abstract void update();

    public void getNextImg() {
        this.frameTimer ++;
        if (this.frameTimer >= NEXT_SPRITE_TIME) {
            this.frameTimer %= NEXT_SPRITE_TIME;
            this.frameNumber = (this.frameNumber + 1) % 3;
            // 3 is the number of sprites for an entity
        }
        this.img = sprites.get(frameNumber).getFxImage();
    }

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
    public boolean collideWith(Entity entity) {
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
