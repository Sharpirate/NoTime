package com.sharpirate.notime.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.screens.Gameplay;
import com.sharpirate.notime.tools.Prefs;

public class GameOverPanel extends Table {

    private final Table actor = this;

    // references
    private final Main app;
    private final Gameplay gameplay;
    private Viewport vport;

    // background
    private TextureRegion bg;

    // widgets
    private Label score;
    private Label bestScore;

    public GameOverPanel(final Main app, final Gameplay gameplay, Viewport vport) {

        this.app = app;
        this.gameplay = gameplay;
        this.vport = vport;

        bg = app.assetManager.get("gfx/images.pack", TextureAtlas.class).findRegion("board");

        initPanel();
        initWidgets();
    }

    private void initPanel() {
        this.setBackground(new TextureRegionDrawable(bg));
        this.setSize(bg.getRegionWidth(), bg.getRegionHeight());
    }

    private void initWidgets() {
        score = new Label("", app.skin, "basic-label");
        score.setAlignment(Align.center);
        bestScore = new Label("", app.skin, "basic-label");
        bestScore.setAlignment(Align.center);

        Button scoreBtn = new Button(app.skin, "score-button");
        scoreBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actor.addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        // score
                    }
                })));
            }
        });

        Button playBtn = new Button(app.skin, "play-button");
        playBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actor.addAction(Actions.delay(0.25f, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        gameplay.restart();
                    }
                })));
            }
        });

        this.add(score).expand();
        this.add(bestScore).expand();
        this.row();
        this.add(scoreBtn).left();
        this.add(playBtn).right();
    }

    public void show() {
        updateLabels();

        this.setPosition(vport.getCamera().position.x - this.getWidth() / 2, vport.getCamera().position.y + vport.getWorldHeight());
        this.addAction(Actions.sequence(Actions.moveTo(vport.getCamera().position.x - this.getWidth() / 2, vport.getCamera().position.y - this.getHeight() / 2, 0.6f), Actions.run(new Runnable() {
            @Override
            public void run() {
                // redirect input to GOP
                gameplay.switchInput(gameplay.dead);
            }
        })));
    }

    private void updateLabels() {
        score.setText("SCORE:\n" + String.valueOf(gameplay.score));
        bestScore.setText("BEST:\n" + String.valueOf(Prefs.readScore()));
    }

}