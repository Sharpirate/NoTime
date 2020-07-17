package com.sharpirate.notime.tools;

import com.badlogic.gdx.InputProcessor;
import com.sharpirate.notime.screens.Gameplay;

public class InputHandler implements InputProcessor {

    // game screen reference
    private final Gameplay gameplay;

    // counter for increasing the speed
    private byte count;

    public InputHandler(final Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(gameplay.sandclock.body.getLinearVelocity().y == 0 && gameplay.firstCollision && pointer == 0) {

            if(Calculator.isBetween(screenY, 0, Constants.touchAreas[0])) {
                gameplay.sandclock.body.applyForceToCenter(0, 640, true);
                gameplay.sandclock.rotate(1.066f);
            }
            else if(Calculator.isBetween(screenY, Constants.touchAreas[0], Constants.touchAreas[1])) {
                gameplay.sandclock.body.applyForceToCenter(0, 460, true);
                gameplay.sandclock.rotate(0.766f);
            }
            else {
                gameplay.sandclock.body.applyForceToCenter(0, 280, true);
                gameplay.sandclock.rotate(0.466f);
            }
            gameplay.score++;
            gameplay.scoreChanged = true;

            if(gameplay.speed < 4.2f) {
                count++;
                if(count == 10) {
                    count = 0;
                    gameplay.speed += 0.15f;
                    gameplay.sandclock.body.setLinearVelocity(gameplay.speed, 0);
                    gameplay.collider.body.setLinearVelocity(gameplay.speed, 0);
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
