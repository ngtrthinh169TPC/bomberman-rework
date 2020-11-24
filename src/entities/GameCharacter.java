package entities;

import javafx.scene.image.Image;

import java.util.ArrayList;

public abstract class GameCharacter extends Entity {
    //protected double xCentre;
    //protected double yCentre;

    protected double moveSpeed = 0;
    protected double rightVelocity;
    protected double downVelocity;

    public GameCharacter(double x, double y, Image img) {
        super(x, y, img);
        //this.xCentre = xAxis + Sprite.SCALED_SIZE * 0.5;
        //this.yCentre = yAxis + Sprite.SCALED_SIZE * 0.5;
        this.rightVelocity = 0;
        this.downVelocity = 0;
    }

    @Override
    public void update() {
        this.xAxis += this.rightVelocity * this.moveSpeed;
        this.yAxis += this.downVelocity * this.moveSpeed;
    }

    public abstract void directionUpdate(ArrayList<String> input);

    protected void velocityUpdate(double rv, double dv) {
        this.rightVelocity = rv;
        this.downVelocity = dv;
    }
}
