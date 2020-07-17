package com.sharpirate.notime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.entities.Background;
import com.sharpirate.notime.tools.Constants;

public class Help extends AbstractScreen implements Screen {

    private Background bg;
    private Table table;

    Help(final Main app) {
        super(app);

        init();
    }

    private void init() {
        bg = new Background(app);

        Label topText = new Label("Tap in this area for a big jump", app.skin, "basic-label");
        topText.getStyle().font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Label centerText = new Label("Tap in this area for a normal jump", app.skin, "basic-label");
        centerText.getStyle().font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Label bottomText = new Label("Tap in this area for a small jump", app.skin, "basic-label");
        bottomText.getStyle().font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        table = new Table();
        table.setFillParent(true);

        table.add(topText).expand();
        table.add();
        table.row();
        table.add(centerText).expand();
        table.row();
        table.add(bottomText).expand();

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.menu);
            }
        });
    }

    @Override
    public void show() {
        stage.addActor(bg);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // update and clear

        stage.draw();

        app.shaper.setProjectionMatrix(stage.getCamera().combined);
        app.shaper.begin(ShapeType.Filled);
        app.shaper.setColor(1, 0, 0, 1);
        app.shaper.rect(0, Constants.drawAreas[0] - 1, stage.getViewport().getWorldWidth(), 3);
        app.shaper.rect(0, Constants.drawAreas[1] - 1, stage.getViewport().getWorldWidth(), 3);
        app.shaper.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
