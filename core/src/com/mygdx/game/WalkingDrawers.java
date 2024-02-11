package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

public class WalkingDrawers extends ApplicationAdapter {
    ShapeRenderer shape;
    FrameBuffer fbo;
    SpriteBatch spriteBatch;
    TextureRegion fboRegion;
    ArrayList<Drawer> drawers = new ArrayList<>();
    OrthographicCamera camera;

    @Override
    public void create() {
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        shape = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        fboRegion.flip(false, true); // FBOs are upside down, so flip the texture region
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false); // Set up the camera with y-up coordinate system
        camera.update();
        int numberOfDrawers = 20;
        for (int i = 0; i < numberOfDrawers; i++) {
            drawers.add(new Drawer());
        }
    }

    @Override
    public void render() {
        fbo.begin();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setProjectionMatrix(camera.combined);
        for (Drawer drawer : drawers) {
            drawer.update();
            shape.setColor(drawer.getColor());
            shape.rect(drawer.getX(), drawer.getY(), 2f, 2f);
        }
        shape.end();
        fbo.end();
        ScreenUtils.clear(0, 0, 0, 1);
        spriteBatch.begin();
        spriteBatch.draw(fboRegion.getTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, fboRegion.getRegionWidth(), fboRegion.getRegionHeight(), false, false);
        spriteBatch.end();
    }

    public void dispose() {
        shape.dispose();
        spriteBatch.dispose();
        fbo.dispose();
    }
}
