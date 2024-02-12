package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public abstract class Walker {
    protected int x;
    protected int y;
    protected Color color;
    protected int xMax;
    protected int yMax;
    protected double alpha;

    Walker() {
        this.xMax = Gdx.graphics.getWidth() * 8;
        this.yMax = Gdx.graphics.getHeight() * 8;
        this.x = (int) (Math.random() * xMax);
        this.y = (int) (Math.random() * yMax);
        this.alpha = 1.0;
        this.color = new Color();
    }

    public abstract void update();

    public abstract void move();

    public abstract void changeColor();

    protected int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    protected int edgeCorrection(int value, int max) {
        if (value >= max) {
            return 0;
        }
        if (value <= 0) {
            return max;
        } else return value;
    }

    protected float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
