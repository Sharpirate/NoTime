package com.sharpirate.notime.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sharpirate.notime.Main;
import com.sharpirate.notime.tools.Constants;

public class Background extends Actor {

    private TextureRegion img;

    public Background(final Main app) {

        img = app.assetManager.get("gfx/images.pack", TextureAtlas.class).findRegion("sea");

        this.setWidth(img.getRegionWidth());
        this.setHeight(img.getRegionHeight());
        this.setPosition(Constants.WORLD_WIDTH / 2 - this.getWidth() / 2, Constants.WORLD_HEIGHT / 2 - this.getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(img, this.getX(), this.getY());
    }
}
