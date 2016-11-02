package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.TextureRegionGameObject;
import org.qohs.dogrunner.gameobjects.gameover.GameOverMessage;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Derek Zhang
 *
 */
public class GameOverScreen extends StageScreen {

	public GameOverScreen(Batch batch) {
		
		super(batch);
		
		TextureRegion tRegion = new TextureRegion(dogRunner.assetManager.get(DogAssets.GAME_OVER_EXPLOSION.FILE_NAME, Texture.class));
		tRegion.flip(false, true);
		int height = dogRunner.GAME_HEIGHT;
		int width = (int) (1f * height / tRegion.getTexture().getHeight() * tRegion.getTexture().getWidth());
		stage.addActor(new TextureRegionGameObject((dogRunner.GAME_WIDTH - width) / 2f, 0f, width, height, tRegion));
		
		stage.addActor(new GameOverMessage(0f, 0f, dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT));
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1f, 165f/255f, 0f, 1f);
		super.render(delta);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
