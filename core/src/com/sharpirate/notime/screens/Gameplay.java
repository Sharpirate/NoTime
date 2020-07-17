package com.sharpirate.notime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.entities.Collider;
import com.sharpirate.notime.entities.GameOverPanel;
import com.sharpirate.notime.entities.Hud;
import com.sharpirate.notime.entities.Platform;
import com.sharpirate.notime.entities.Sandclock;
import com.sharpirate.notime.tools.AdvancedContactListener;
import com.sharpirate.notime.tools.Constants;
import com.sharpirate.notime.tools.InputHandler;
import com.sharpirate.notime.tools.Prefs;

public class Gameplay extends AbstractScreen {

    // viewport reference
    private final Viewport vport;

    // input handler
    private InputHandler inputHandler;

    // box2d
    private World world;

    // fixed update
    private float accumulator;

    // actors
    public Sandclock sandclock;
    public Platform[] platforms = new Platform[4];
    public Collider collider;
    private GameOverPanel gop;

    // platforms translate
    public boolean translatePending;
    public byte translateIndex;

    // hud
    private Hud hud;

    // game vars
    public float speed;
    public short score;
    public boolean dead;
    private boolean prepared;
    public boolean scoreChanged;
    public boolean adsHandled;
    private byte deadCounter;
    public boolean firstCollision;

    Gameplay(final Main app) {
        super(app); // obtain app reference and setup stage

        this.vport = stage.getViewport();

        init(); // initialize references
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void update(float delta) {
        if(!dead) {
            updateWorld(delta);
            updateCamera();
            handleTranslate();
            updateLiveStat();
        }
        else if(!prepared) {
            sandclock.body.setLinearVelocity(0, 0); // clear velocities
            collider.body.setLinearVelocity(0, 0);

            sandclock.clearActions();
            sandclock.remove(); // remove sandclock from the stage

            for(Platform platform : platforms) {
                platform.clearActions();
                platform.remove(); // remove platforms from the stage
            }

            Prefs.writeScore(score); // save score

            Gdx.input.setInputProcessor(null); // remove input

            prepared = true;
        }

        stage.act(delta);
        hud.update(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta); // update and clear

        hud.render();
        stage.draw();
    }

    @Override
    public void show() {
        dead = false;
        prepared = false;
        accumulator = 0f;
        score = 0;
        speed = 3f;
        scoreChanged = false;
        adsHandled = false;
        firstCollision = false;

        setup(); // setup all actors and positions
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        super.dispose(); // dispose the stage
        world.dispose();
        hud.dispose();
    }

    private void init() {
        inputHandler = new InputHandler(this);

        world = new World(new Vector2(0, 0), true); // create the world with 0 gravity
        world.setContactListener(new AdvancedContactListener(this));

        sandclock = new Sandclock(world, vport, app);

        for(byte index = 0; index < platforms.length; index++) {
            platforms[index] = new Platform(world, index, app); // create platforms at default position
        }

        collider = new Collider(world, vport, this);

        hud = new Hud(app, this);

        gop = new GameOverPanel(app, this, vport);
    }

    private void setup() {
        vport.getCamera().position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0); // reset camera position
        vport.getCamera().update();

        sandclock.resetPosition(vport); // reset sandclock position
        stage.addActor(sandclock);

        for(byte index  = 0; index < platforms.length; index++) {
            if(index != 0) {
                platforms[index].body.setTransform(platforms[index-1].body.getPosition().x + nextDistance(), // position platforms
                        platforms[0].body.getPosition().y, 0);
            }
            else {
                platforms[index].body.setTransform((vport.getCamera().position.x + platforms[index].img.getRegionWidth() / 2 - sandclock.img.getRegionWidth() / 2) / Constants.PPM,
                        ((vport.getCamera().position.y - vport.getWorldHeight() / 2) + platforms[index].getHeight() * 1.5f) / Constants.PPM, 0);
            }
            platforms[index].repositionActor();
            stage.addActor(platforms[index]);
        }

        collider.resetPosition(vport); // reset collider position

        hud.setup(); // reset hud

        switchInput(dead); // redirect input to the inputHandler

        world.setGravity(Constants.GRAVITY); // apply gravity
    }

    public void switchInput(boolean dead) {
        if(!dead) {
            Gdx.input.setInputProcessor(inputHandler);
        } else {
            Gdx.input.setInputProcessor(stage);
        }
    }

    private float nextDistance() {
        float amount = 0f;

        switch(MathUtils.random(2)) {
            case 0:
                amount = 2.25f;
                break;
            case 1:
                amount = 3.25f;
                break;
            case 2:
                amount = 4f;
                break;
        }

        return amount;
    }

    private void updateLiveStat() {
        if(sandclock.position.y <= vport.getCamera().position.y - vport.getScreenHeight() / 2 - sandclock.img.getRegionWidth() / 2) {
            dead = true;
        }
    }

    private void handleTranslate() {
        if(translatePending) {
            if(translateIndex != 0) {
                platforms[translateIndex].body.setTransform(platforms[translateIndex-1].body.getPosition().x + nextDistance(), platforms[translateIndex].body.getPosition().y, 0);
            } else {
                platforms[translateIndex].body.setTransform(platforms[platforms.length-1].body.getPosition().x + nextDistance(), platforms[translateIndex].body.getPosition().y, 0);
            }
            translatePending = false;
            if(platforms[translateIndex].getColor().a == 1f) {
                platforms[translateIndex].fade();
            }
        }
    }

    private void updateWorld(float delta) {
        if(delta > 0.25f) delta = 0.25f; // limit delta

        accumulator += delta;

        while(accumulator >= Constants.STEP) {
            sandclock.savePosition();
            world.step(Constants.STEP, 8, 3);

            accumulator -= Constants.STEP;
        }

        sandclock.interpolate(accumulator / Constants.STEP);
    }

    private void updateCamera() {
        vport.getCamera().position.x = sandclock.position.x;
        vport.getCamera().update();
    }

    public void restart() {
        gop.clearActions();
        gop.remove();
        app.setScreen(app.gameplay);
    }

    public void handleAds() {
        app.adHandler.loadInterstitial();

        deadCounter++;

        if(deadCounter == Constants.DEADS_FOR_ADS) {
            deadCounter = 0;
            app.adHandler.showInterstitial();
        }

        showGOP();
    }

    private void showGOP() {
        stage.addActor(gop); // add GOP to the stage
        gop.show(); // show GOP
    }
}