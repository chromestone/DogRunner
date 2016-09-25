package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.TextureRegionGameObject;
import org.qohs.dogrunner.gameobjects.gameover.GameOverMessage;
import org.qohs.dogrunner.io.DogAssets;

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
		
		TextureRegion tRegion = new TextureRegion(dogRunner.assetManager.get(DogAssets.GAME_OVER_EXPLOSION.fileName, Texture.class));
		tRegion.flip(false, true);
		int height = dogRunner.GAME_HEIGHT;
		int width = (int) (1.0 * height / tRegion.getTexture().getHeight() * tRegion.getTexture().getWidth());
		stage.addActor(new TextureRegionGameObject((dogRunner.GAME_WIDTH - width) / 2, 0, width, height, tRegion));
		
		stage.addActor(new GameOverMessage(0, 0, dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT));
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
