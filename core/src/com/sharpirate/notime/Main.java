package com.sharpirate.notime;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.sharpirate.notime.screens.Gameplay;
import com.sharpirate.notime.screens.Help;
import com.sharpirate.notime.screens.Loading;
import com.sharpirate.notime.screens.Menu;
import com.sharpirate.notime.tools.AdHandler;

public class Main extends Game {

	// game screens
	public Menu menu;
	public Gameplay gameplay;
	public Help help;

	// resources
	public SpriteBatch batch;
	public ShapeRenderer shaper;
	public AssetManager assetManager;

	// UI skin
	public Skin skin;
	public TextureAtlas atlas;

	// ad handler
	public AdHandler adHandler;

    // pause boolean
    private boolean paused;

	public Main() {}

	public Main(AdHandler adHandler) {
		this.adHandler = adHandler;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		this.setScreen(new Loading(this));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
		shaper.dispose();
		skin.dispose();
		atlas.dispose();

		// dispose screens
		gameplay.dispose();
		help.dispose();
	}
}