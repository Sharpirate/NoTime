package com.sharpirate.notime.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sharpirate.notime.screens.Gameplay;

public class AdvancedContactListener implements ContactListener {

    // game screen reference
    private final Gameplay gameplay;


    public AdvancedContactListener(final Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    @Override
    public void beginContact(Contact contact) {

        // if the contact is between the collider and a platform, not between the sandclock and a platform
        if(contact.getFixtureA().getUserData() == "collider" || contact.getFixtureB().getUserData() == "collider") {

            // which fixture is the platform and translate the platform with that index
            if(contact.getFixtureA().getUserData() == "collider") {
                gameplay.translateIndex = (Byte)contact.getFixtureB().getUserData();
            }
            else {
                gameplay.translateIndex = (Byte)contact.getFixtureA().getUserData();
            }
            gameplay.translatePending = true;
        }
        else if(!gameplay.firstCollision) { // else its between the sandclock and a platform
            gameplay.sandclock.body.setLinearVelocity(gameplay.speed, 0);
            gameplay.collider.body.setLinearVelocity(gameplay.speed, 0);
            gameplay.firstCollision = true;
        }

    }

    @Override
    public void endContact(Contact contact) {

        // if the contact is between the sandclock and a platform, not between the collider and a platform
        if(contact.getFixtureA().getUserData() == "player" || contact.getFixtureB().getUserData() == "player") {

            if(gameplay.score != 0) { // don't handle the last begin contact from the previous game

                // which fixture is the platform and fade the platform with that index
                if(contact.getFixtureA().getUserData() == "player") {
                    gameplay.platforms[(Byte)contact.getFixtureB().getUserData()].fade();
                }
                else {
                    gameplay.platforms[(Byte)contact.getFixtureA().getUserData()].fade();
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
