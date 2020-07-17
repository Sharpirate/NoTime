package com.sharpirate.notime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sharpirate.notime.Main;

public class Loading extends AbstractScreen {

    // logo
    private Texture logoImg;

    public Loading(final Main app) {
        super(app); // call the constructor of the parent class
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        queueAssets();

        initLogo();
    }

    private void initLogo() {
        logoImg = new Texture("gfx/logo.png");
        logoImg.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Image logo = new Image(logoImg);
        logo.setPosition(stage.getCamera().position.x - logo.getWidth() / 2, stage.getCamera().position.y - logo.getHeight() / 2);
        logo.addAction(Actions.alpha(0f));
        logo.addAction(Actions.sequence(Actions.fadeIn(1f), Actions.run(new Runnable() {
            @Override
            public void run() {
                app.shaper = new ShapeRenderer();
                app.assetManager.finishLoading();
                app.atlas = app.assetManager.get("gfx/images.pack", TextureAtlas.class);
                app.skin = new Skin(Gdx.files.internal("config/skin.json"), app.atlas);
                initScreens();
            }
        }), Actions.fadeOut(1f), Actions.run(new Runnable() {
            @Override
            public void run() {
                app.setScreen(app.menu);
            }
        })));

        stage.addActor(logo);
    }

    private void initScreens() {
        app.help = new Help(app);
        app.gameplay = new Gameplay(app);
        app.menu = new Menu(app);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // clear and update

        stage.draw();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose(); // dispose the stage
        logoImg.dispose();
    }

    private void queueAssets() {
        app.assetManager.load("gfx/images.png", Texture.class);
        app.assetManager.load("gfx/images.pack", TextureAtlas.class);
        app.assetManager.load("sounds/tick.wav", Sound.class);
        app.assetManager.load("sounds/tock.wav", Sound.class);
    }
}
