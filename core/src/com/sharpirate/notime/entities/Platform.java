package com.sharpirate.notime.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.tools.Constants;


public class Platform extends Actor {
    public Body body;
    public TextureRegion img;

    public Platform(World world, final byte index, final Main app) {

        img = app.assetManager.get("gfx/images.pack", TextureAtlas.class).findRegion("platform");

        createBody(world, index);

        this.setBounds(this.body.getPosition().x * Constants.PPM, this.body.getPosition().y * Constants.PPM, this.img.getRegionWidth(), this.img.getRegionHeight());
        this.setOrigin(this.getWidth() / 2, this.getHeight() / 2);
    }

    private void createBody(World world, byte index) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.StaticBody;

        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((img.getRegionWidth() / 2) / Constants.PPM, (img.getRegionHeight() / 2) / Constants.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.BIT_PLATFORM;
        fdef.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_COLLIDER;

        body.createFixture(fdef).setUserData(index);

        shape.dispose();
    }

    public void fade() {
        this.addAction(Actions.sequence(Actions.fadeOut(0.75f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        repositionActor();
                    }
                })));
    }

    public void repositionActor() {
        this.setX(this.body.getPosition().x * Constants.PPM);
        this.setY(this.body.getPosition().y * Constants.PPM);
        this.addAction(Actions.alpha(1f));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(this.img, this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2, this.getWidth(), this.getHeight());
    }
}