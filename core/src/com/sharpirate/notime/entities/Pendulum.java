package com.sharpirate.notime.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sharpirate.notime.Main;

class Pendulum extends Actor {

    private TextureRegion img;

    Pendulum(Viewport vport, final Main app) {

        img = app.assetManager.get("gfx/images.pack", TextureAtlas.class).findRegion("pendulum");

        this.setWidth(img.getRegionWidth());
        this.setHeight(img.getRegionHeight());
        this.setPosition(vport.getCamera().position.x - this.getWidth() / 2, (vport.getCamera().position.y + vport.getWorldHeight() / 2) - (this.getHeight() - this.getHeight() / 17f));
        this.setOrigin(this.getWidth() / 2, this.getHeight());
        this.setScale(1, 1);
    }

    @Override
    public void act(float delta) {}

    void act(boolean act, float delta) {
        if(act) super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(img, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    void reposition(Viewport vport) {
        this.setPosition(vport.getCamera().position.x - this.getWidth() / 2, (vport.getCamera().position.y + vport.getWorldHeight() / 2) - (this.getHeight() - this.getHeight() / 17f));
    }
}
