package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class Ballom extends GameCharacter {
    public Ballom(double x, double y, ArrayList<Sprite> sprites) {
        super(x, y, sprites);
        this.moveSpeed = 0.6;
        this.getDirection();
    }

    @Override
    public void getDirection() {
        Random random = new Random();
        int direction = random.nextInt(4);
        this.velocityUpdate(moveX.get(direction), moveY.get(direction));
    }
}
