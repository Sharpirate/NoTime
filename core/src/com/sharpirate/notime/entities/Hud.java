package com.sharpirate.notime.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.screens.Gameplay;
import com.sharpirate.notime.tools.Constants;
import com.sharpirate.notime.tools.VirtualViewport;

public class Hud {
    // app reference
    private final Main app;

    // game screen reference
    private final Gameplay gameplay;

    // stage
    private Stage stage;

    private Pendulum pendulum;
    private boolean rotatePendulum = true;

    // score
    private Label score;
    private boolean prepared;
    private Table table;

    public Hud(final Main app, final Gameplay gameplay) {
        this.gameplay = gameplay;
        this.app = app;

        init(app.batch); // initialize references
    }

    private void init(SpriteBatch batch) {
        stage = new Stage(new VirtualViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT), batch);

        pendulum = new Pendulum(stage.getViewport(), app);

        Runnable playTick = new Runnable() {
            @Override
            public void run() {
                app.assetManager.get("sounds/tick.wav", Sound.class).play(1f);
            }
        };

        Runnable playTock = new Runnable() {
            @Override
            public void run() {
                app.assetManager.get("sounds/tock.wav", Sound.class).play(1f);
            }
        };

        Runnable checkDead = new Runnable() {
            @Override
            public void run() {
                if(gameplay.dead) {
                    rotatePendulum = false;
                }
            }
        };

        RepeatAction rotate = Actions.forever(Actions.sequence(Actions.rotateTo(45, 0.5f), Actions.run(playTick),
                Actions.rotateTo(0, 0.5f), Actions.run(checkDead),
                Actions.rotateTo(-45, 0.5f), Actions.run(playTock),
                Actions.rotateTo(0, 0.5f), Actions.run(checkDead)));
        pendulum.addAction(rotate);

        score = new Label(String.valueOf(gameplay.score), app.skin, "score-label");
        score.getStyle().font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        table = new Table();
        table.setFillParent(true);
        table.align(Align.top | Align.center);
        table.add(score).pad(stage.getViewport().getWorldHeight() / 6);

        Background bg = new Background(app);

        stage.addActor(bg);
        stage.addActor(pendulum);
    }

    public void update(float delta) {
        pendulum.act(rotatePendulum, delta);
        stage.act();

        if(!rotatePendulum && !gameplay.adsHandled) {
            gameplay.handleAds();
            gameplay.adsHandled = true;
        }

        if(!gameplay.dead) {
            if(gameplay.scoreChanged) {
                score.setText(String.valueOf(gameplay.score));
                gameplay.scoreChanged = false;
            }
        }
        else if(!prepared) {
            table.remove();
            prepared = true;
        }
    }

    public void render() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        pendulum.reposition(stage.getViewport());
    }

    public void dispose() {
        stage.dispose();
    }

    public void setup() {
        rotatePendulum = true;
        prepared = false;
        score.setText("0");

        stage.addActor(table);
    }
}
