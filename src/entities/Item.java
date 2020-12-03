package entities;

import graphics.Sprite;

import java.util.ArrayList;
import java.util.Collections;

public class Item extends Entity {
    private final String ability;

    public Item(double x, double y, Sprite sprite, String ability) {
        super(x, y, new ArrayList<>(Collections.singletonList(sprite)));
        this.ability = ability;
    }

    public String getAbility() {
        return ability;
    }

    @Override
    public void update() {

    }
}
