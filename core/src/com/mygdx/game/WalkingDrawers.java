package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class WalkingDrawers extends ApplicationAdapter {
    ShapeRenderer shape;
    ArrayList<Drawer> drawers = new ArrayList<>();
    private final int numberOfDrawers = 4;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        for (int i = 0; i < 10; i++) {
            drawers.add(new Drawer());
        }
    }

    @Override
    public void render() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Drawer drawer : drawers) {
            drawer.update();
            shape.setColor(drawer.getColor());
            shape.rect(drawer.getX(), drawer.getY(), 2f, 2f);
        }
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }
}
