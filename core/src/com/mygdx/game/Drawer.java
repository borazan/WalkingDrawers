package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Drawer {
    private int x;
    private int y;
    private Color color;
    private int xMax;
    private int yMax;
    private double alpha;

    public Drawer() {
        this.xMax = Gdx.graphics.getWidth();
        this.yMax = Gdx.graphics.getHeight();
        this.x = (int) (Math.random() * xMax);
        this.y = (int) (Math.random() * yMax);
        this.alpha = 1.0;
        this.color = new Color();
        color.set((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) alpha);
    }

    public void update() {
        move();
        changeColor();
    }

    public void move() {
        x += (Math.random() > 0.5) ? 1 : -1;
        y += (Math.random() > 0.5) ? 1 : -1;

        x = clamp(x, 0, xMax - 1); //-1 since coordinates are 0-based
        y = clamp(y, 0, yMax - 1);
    }

    public void changeColor() {
        float r = color.r + (((float) Math.random() > 0.5) ? 0.01f : -0.01f);
        float g = color.g + (((float) Math.random() > 0.5) ? 0.01f : -0.01f);
        float b = color.b + (((float) Math.random() > 0.5) ? 0.01f : -0.01f);

        r = clamp(r, 0f, 1f);
        g = clamp(g, 0f, 1f);
        b = clamp(b, 0f, 1f);

        color.set(r, g, b, 1f);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private float clamp(float value, float min, float max) {
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
