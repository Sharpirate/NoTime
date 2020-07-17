package com.sharpirate.notime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.tools.Constants;
import com.sharpirate.notime.tools.VirtualViewport;

abstract class AbstractScreen implements Screen {

    protected final Main app;
    final Stage stage;

    AbstractScreen(final Main app) {
        this.app = app;
        stage = new Stage(new VirtualViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT), app.batch);
    }

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        resize(width, height, false);
    }

    void resize(int width, int height, boolean centerCamera) {
        stage.getViewport().update(width, height, centerCamera);
        Constants.initAreas(Constants.touchAreas, stage.getViewport().getScreenHeight());
        Constants.initAreas(Constants.drawAreas, stage.getViewport().getWorldHeight());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
