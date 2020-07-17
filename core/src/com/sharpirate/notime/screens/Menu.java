package com.sharpirate.notime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.entities.Background;

public class Menu extends AbstractScreen {

    private Background bg;

    private Table table;
    private Image sign;
    private Button playBtn, helpBtn, rateBtn;
    private Label copyright;

    Menu(final Main app) {
        super(app);

        init();
    }

    private void init() {
        bg = new Background(app);

        sign = new Image(app.assetManager.get("gfx/images.pack", TextureAtlas.class).findRegion("sign"));

        playBtn = new Button(app.skin, "play-button");
        playBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playBtn.addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        app.setScreen(app.gameplay);
                        dispose(); // dispose the menu
                    }
                })));
            }
        });

        helpBtn = new Button(app.skin, "help-button");
        helpBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playBtn.addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        app.setScreen(app.help);
                    }
                })));
            }
        });

        rateBtn = new Button(app.skin, "rate-button");
        rateBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playBtn.addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.sharpirate.notime");
                    }
                })));
            }
        });


        copyright = new Label("© Sharpirate 2017", app.skin, "copyright-label");
        copyright.getStyle().font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);

        table.add(sign).colspan(2).expand().center();
        table.row();
        table.row();
        table.add(playBtn).colspan(2).expandX().bottom();
        table.row();
        table.add(helpBtn).expandX().left().padLeft(20).padBottom(20);
        table.add(rateBtn).expandX().right().padRight(20).padBottom(20);
    }

    private void setup() {
        stage.addActor(bg);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        setup();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height, true);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // update and clear
        stage.draw();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose(); // dispose the stage
    }
}
