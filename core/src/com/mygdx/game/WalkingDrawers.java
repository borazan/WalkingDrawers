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
    ArrayList<Virus> viruses = new ArrayList<>();
    OrthographicCamera camera;
    OrthographicCamera staticCamera;
    int worldWidth;
    int worldHeight;
    float baseMoveSpeed = 700f;
    float moveSpeed;
    float baseZoomSpeed = 1f;
    float zoomSpeed;
    int numberOfDrawers = 20000;
    int numberOfViruses = 200;
    int screenSize = 1080;
    float drawerSize = 2f;
    float virusSize = 8f;
    int virusMovementMultiplier = 4; //how many times per frame should viruses move

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
        Gdx.graphics.setWindowedMode(screenSize, screenSize);
        worldWidth = Gdx.graphics.getWidth() * 8;
        worldHeight = Gdx.graphics.getHeight() * 8;
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, worldWidth, worldHeight, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        shape = new ShapeRenderer();
        fboRegion.flip(false, true); // FBOs are upside down
        staticCamera = new OrthographicCamera(worldWidth, worldHeight);
        staticCamera.position.set(staticCamera.viewportWidth / 2, staticCamera.viewportHeight / 2, 0);
        staticCamera.update();
        camera = new OrthographicCamera((float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        camera.setToOrtho(false);
        camera.zoom = .05f;
        camera.update();
        for (int i = 0; i < numberOfDrawers; i++) {
            drawers.add(new Drawer());
        }
        for (int i = 0; i < numberOfViruses; i++) {
            viruses.add(new Virus());
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
            shape.rect(drawer.getX(), drawer.getY(), drawerSize, drawerSize);
        }
        for (Virus virus : viruses) {
            shape.setColor(virus.getColor());
            for (int i = 0; i < virusMovementMultiplier; i++) {
                virus.update();
                shape.rect(virus.getX(), virus.getY(), virusSize, virusSize);
            }
        }
        shape.end();
        fbo.end();
        batch.setProjectionMatrix(camera.combined); //user-controlled camera for viewing
        batch.begin();
        batch.draw(fboRegion.getTexture(), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "camera.zoom: " + String.format("%.3g%n", camera.zoom), 10, Gdx.graphics.getHeight() - 25);
        font.draw(batch, "movement speed: " + String.format("%.3g%n", moveSpeed), 10, Gdx.graphics.getHeight() - 40);
        font.draw(batch, "zoom speed: " + String.format("%.3g%n", zoomSpeed), 10, Gdx.graphics.getHeight() - 55);


        batch.end();
    }

    public void handleCameraInput() {
        updateCameraSpeed();
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
            if (camera.zoom <= 0.05f) camera.zoom = 0.05F;
        } else if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) {
            camera.zoom += zoomSpeed * Gdx.graphics.getDeltaTime();
            if (camera.zoom >= 1f) camera.zoom = 1f;
        }
        // clear canvas
        if (Gdx.input.isKeyPressed((Input.Keys.SPACE))) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

    public void updateCameraSpeed() {
        moveSpeed = baseMoveSpeed * camera.zoom;
        zoomSpeed = baseZoomSpeed * camera.zoom;
    }

    public void dispose() {
        shape.dispose();
        fbo.dispose();
        batch.dispose();
        font.dispose();
    }
}
