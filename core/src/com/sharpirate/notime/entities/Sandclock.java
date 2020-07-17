package com.sharpirate.notime.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.tools.Constants;

public class Sandclock extends Actor {
    private Actor actor = this;
    public Body body;
    public TextureRegion img;

    private Vector2 prevPos = new Vector2();
    public Vector2 position = new Vector2();

    public Sandclock(World world, Viewport vport, final Main app) {
        img = app.assetManager.get("gfx/images.pack", TextureAtlas.class).findRegion("sandclock");

        this.setSize(img.getRegionWidth(), img.getRegionHeight());
        this.setPosition(vport.getCamera().position.x - this.getWidth() / 2, vport.getScreenHeight() - (this.getHeight() / 2));
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);

        createBody(world);
    }

    private void createBody(World world) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.fixedRotation = true;

        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((this.getWidth() / 2) / Constants.PPM, (this.getHeight() / 2 - 2) / Constants.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.BIT_PLAYER;
        fdef.filter.maskBits = Constants.BIT_PLATFORM;

        body.createFixture(fdef).setUserData("player");
        body.getFixtureList().get(0).setFriction(0);

        shape.dispose();
    }

    public void resetPosition(Viewport vport) {
        this.body.setTransform(vport.getCamera().position.x / Constants.PPM, vport.getScreenHeight() / Constants.PPM, 0);
        this.position.x = vport.getCamera().position.x;
        this.position.y = vport.getScreenHeight();
        this.setPosition(position.x, position.y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setPosition(position.x, position.y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(img, this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2,
                this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(),
                this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    public void rotate(float duration) {
        this.addAction(Actions.sequence(Actions.delay(0.03f), Actions.rotateTo(-180, duration - 0.03f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        actor.setRotation(0);
                    }
                })));
    }

    public void savePosition() {
        this.prevPos.x = this.body.getPosition().x;
        this.prevPos.y = this.body.getPosition().y;
    }

    public void interpolate(float alpha) {
        this.position.x = (this.body.getPosition().x * alpha + this.prevPos.x * (1.0f - alpha)) * Constants.PPM;
        this.position.y = (this.body.getPosition().y * alpha + this.prevPos.y * (1.0f - alpha)) * Constants.PPM;
    }
}