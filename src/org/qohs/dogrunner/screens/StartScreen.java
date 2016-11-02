package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.*;
import org.qohs.dogrunner.gameobjects.start.*;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StartScreen extends StageScreen {
	
	public StartScreen(Batch batch) {
		
		super(batch);
		
		TextureRegion tRegion = new TextureRegion(dogRunner.assetManager.get(DogAssets.DOG_IMG.FILE_NAME, Texture.class));
		//ratio = new height / picture height
		//width = ratio * picture width
		float width = (dogRunner.GAME_HEIGHT * 1.0f) / tRegion.getTexture().getHeight() * tRegion.getTexture().getWidth();
		tRegion.flip(false, true);
		GameObject trGameObj = new TextureRegionGameObject(dogRunner.GAME_WIDTH / 10f, 0f,
				width, dogRunner.GAME_HEIGHT,
				tRegion);
		stage.addActor(trGameObj);
		trGameObj.setTouchable(Touchable.disabled);
		
		GameObject startButton = new StartButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_WIDTH / 20f,
				dogRunner.GAME_HEIGHT / 4f,
				dogRunner.GAME_HEIGHT / 2f, dogRunner.GAME_HEIGHT / 2f);
		stage.addActor(startButton);
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
