package com.sharpirate.notime.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sharpirate.notime.screens.Gameplay;
import com.sharpirate.notime.tools.Constants;

public class Collider {
    private final Gameplay gameplay;
    public Body body;

    public Collider(World world, Viewport vport, final Gameplay gameplay) {
        this.gameplay = gameplay;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyType.DynamicBody;
        bdef.fixedRotation = true;
        bdef.position.set((vport.getCamera().position.x - ((gameplay.sandclock.getWidth()) + gameplay.platforms[0].getWidth())) / Constants.PPM,
                vport.getCamera().position.y / Constants.PPM);

        body = world.createBody(bdef);
        body.setGravityScale(0); // disable gravity

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f / Constants.PPM, (Constants.WORLD_HEIGHT / 2) / Constants.PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.isSensor = true;
        fdef.shape = shape;
        fdef.filter.categoryBits = Constants.BIT_COLLIDER;
        fdef.filter.maskBits = Constants.BIT_PLATFORM;

        body.createFixture(fdef).setUserData("collider");
        shape.dispose();
    }

    public void resetPosition(Viewport vport) {
        this.body.setTransform((vport.getCamera().position.x - ((gameplay.sandclock.getWidth()) + gameplay.platforms[0].getWidth())) / Constants.PPM,
                vport.getCamera().position.y / Constants.PPM, 0);
    }
}
