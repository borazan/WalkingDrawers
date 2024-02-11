package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;

public class Drawer {
    private int x;
    private int y;
    private Color color;
    private int xMax;
    private int yMax;
    private double alpha;
    private float biasXThreshold; //compared against Math.Random 0.5 in move()
    private float biasYThreshold;
    private final float biasChance = 0.0005f; //chance to enter a movement bias phase for the given duration
    private final int biasDuration = 5; //in seconds, gets multiplied by 1000 for ms
    private final float biasXIntensity = 0.2f; //how intense should this Drawer's biases be
    private final float biasYIntensity = 0.2f;
    private long biasEndTime = 0;
    private boolean isBiased = false;

    public Drawer() {
        this.xMax = Gdx.graphics.getWidth();
        this.yMax = Gdx.graphics.getHeight();
        this.x = (int) (Math.random() * xMax);
        this.y = (int) (Math.random() * yMax);
        this.alpha = 1.0;
        this.color = new Color();
        color.set((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) alpha);
        this.biasXThreshold = 0.5f + (((float)Math.random() - 0.5f) * biasXIntensity);
        this.biasYThreshold = 0.5f + (((float)Math.random() - 0.5f) * biasYIntensity);
    }

    public void update() {
        move();
        changeColor();
    }

    public void move() {
        long currentTime = TimeUtils.millis();
        if (!isBiased) {
            if (Math.random() <= biasChance) { //roll for bias, only if currently unbiased
                isBiased = true;
                biasEndTime = currentTime + (biasDuration * 1000);
            }
        } else { //currently biased
            if (currentTime >= biasEndTime) {
                isBiased = false;
            }
        }

        if (isBiased) {
            x += (Math.random() > biasXThreshold) ? 1 : -1;
            y += (Math.random() > biasXThreshold) ? 1 : -1;

        } else {
            x += (Math.random() > 0.5) ? 1 : -1;
            y += (Math.random() > 0.5) ? 1 : -1;
        }
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
