package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;

public class Virus extends Walker {
    float whiteChance = 0.05f;

    Virus() {
        super();
        if (Math.random() < whiteChance) {
            color.set(1, 1, 1, 1); //White
        } else color.set(0, 0, 0, 1); //Black

    }

    @Override
    public void update() {
        move();

    }

    @Override
    public void move() {
        x += (Math.random() > 0.5) ? 1 : -1;
        y += (Math.random() > 0.5) ? 1 : -1;
        x = edgeCorrection(x, xMax - 1); //-1 since coordinates are 0-based
        y = edgeCorrection(y, yMax - 1);
    }

    @Override
    public void changeColor() {
    }
}
