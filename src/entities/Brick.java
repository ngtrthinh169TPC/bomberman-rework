package entities;

import javafx.scene.image.Image;

public class Brick extends Entity {
    public Brick(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        /* Check if this was destroyed by flames **/
    }
}
