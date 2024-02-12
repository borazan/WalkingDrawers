package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

public class WalkingDrawers extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont font;
    ShapeRenderer shape;
    FrameBuffer fbo;
    TextureRegion fboRegion;
    ArrayList<Drawer> drawers = new ArrayList<>();
    OrthographicCamera camera;
    OrthographicCamera staticCamera;
    float moveSpeed = 20;
    float zoomSpeed = 0.1f;
    int numberOfDrawers = 40000;
    int screenSize = 1200;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
        Gdx.graphics.setWindowedMode(screenSize, screenSize);
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth() * 8 , Gdx.graphics.getHeight() * 8, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        shape = new ShapeRenderer();
        fboRegion.flip(false, true); // FBOs are upside down
        staticCamera = new OrthographicCamera(Gdx.graphics.getWidth() * 8, Gdx.graphics.getHeight() * 8);
        staticCamera.position.set(staticCamera.viewportWidth / 2, staticCamera.viewportHeight / 2, 0);
        staticCamera.update();
        camera = new OrthographicCamera((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.zoom = .05f;
        camera.update();
        for (int i = 0; i < numberOfDrawers; i++) {
            drawers.add(new Drawer());
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);
        //Render to the FBO
        fbo.begin();
        handleCameraInput();
        camera.update();
        shape.setProjectionMatrix(staticCamera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Drawer drawer : drawers) {
            drawer.update();
            shape.setColor(drawer.getColor());
            shape.rect(drawer.getX(), drawer.getY(), 2f, 2f);
        }
        shape.end();
        fbo.end();
        batch.setProjectionMatrix(camera.combined); //user-controlled camera for viewing
        batch.begin();
        batch.draw(fboRegion.getTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    public void handleCameraInput() {
        // move camera left and right
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, moveSpeed * Gdx.graphics.getDeltaTime());
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -moveSpeed * Gdx.graphics.getDeltaTime());
        }

        // move camera up and down
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-moveSpeed * Gdx.graphics.getDeltaTime(), 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(moveSpeed * Gdx.graphics.getDeltaTime(), 0);
        }

        // zoom camera
        if (Gdx.input.isKeyPressed((Input.Keys.UP))) {
            camera.zoom -= zoomSpeed * Gdx.graphics.getDeltaTime();
        } else if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) {
            camera.zoom += zoomSpeed * Gdx.graphics.getDeltaTime();
        }
        // clear canvas
        if (Gdx.input.isKeyPressed((Input.Keys.SPACE))) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    public void dispose() {
        shape.dispose();
        fbo.dispose();
        batch.dispose();
        font.dispose();
    }
}
