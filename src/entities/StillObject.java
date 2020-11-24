package entities;

import javafx.scene.image.Image;

public abstract class StillObject extends Entity {
    public StillObject(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        /* StillObject will not be updated unless the level is passed **/
    }
}
