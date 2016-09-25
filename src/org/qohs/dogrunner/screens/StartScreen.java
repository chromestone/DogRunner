package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.*;
import org.qohs.dogrunner.gameobjects.start.*;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StartScreen extends StageScreen {
	
	public StartScreen(Batch batch) {
		
		super(batch);
		
		GameObject startButton = new StartButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 2 - dogRunner.GAME_WIDTH / 20,
				dogRunner.GAME_HEIGHT / 4,
				dogRunner.GAME_HEIGHT / 2, dogRunner.GAME_HEIGHT / 2);
		stage.addActor(startButton);
		
		TextureRegion tRegion = new TextureRegion(dogRunner.assetManager.get(DogAssets.DALMATIAN.fileName, Texture.class));
		//ratio = new height / picture height
		//width = ratio * picture width
		double width = (dogRunner.GAME_HEIGHT * 1.0) / tRegion.getTexture().getHeight() * tRegion.getTexture().getWidth();
		tRegion.flip(false, true);
		GameObject trGameObj = new TextureRegionGameObject(dogRunner.GAME_WIDTH / 10,
				0,
				(int) width,
				dogRunner.GAME_HEIGHT,
				tRegion);
		stage.addActor(trGameObj);
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
