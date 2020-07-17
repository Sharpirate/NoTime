package com.sharpirate.notime.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

public class VirtualViewport extends Viewport {

    private float sourceWidth;
    private float sourceHeight;

    public VirtualViewport(float sourceWidth, float sourceHeight) {
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;

        setCamera(new OrthographicCamera());
        buildViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }

    private void buildViewport(int screenWidth, int screenHeight, boolean centerCamera) {
        float sourceRatio = sourceWidth / sourceHeight;
        float targetRatio = (float)screenWidth / (float)screenHeight;

        int viewportWidth = Math.round(targetRatio < sourceRatio ? sourceHeight * targetRatio : sourceWidth);
        int viewportHeight = Math.round(targetRatio < sourceRatio ? sourceHeight : sourceWidth / targetRatio);

        setWorldSize(viewportWidth, viewportHeight);

        setScreenBounds(0, 0, screenWidth, screenHeight);

        apply(centerCamera);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        buildViewport(screenWidth, screenHeight, centerCamera);
    }
}
