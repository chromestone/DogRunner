package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.*;
import org.qohs.dogrunner.gameobjects.start.*;
import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StartScreen extends StageScreen {
	
	private Music backMusic;
	
	public StartScreen() {
		
		super();
        
		TextureRegion background;
        background = new TextureRegion(dogRunner.assetManager.get(DogTexture.START_BACKGROUND.FILE_NAME, Texture.class));
        background.flip(false, true);
        
        Image image = new Image(background);
		Vector2 scaled = Scaling.fill.apply(image.getWidth(), image.getHeight(), dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT);
		image.setWidth(scaled.x);
		image.setHeight(scaled.y);
        image.setPosition((dogRunner.GAME_WIDTH - image.getWidth()) * 100f / 192f, (dogRunner.GAME_HEIGHT - image.getHeight()) * 67f / 108f);
        stage.addActor(image);
		
        /*
		TextureRegion tRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.DOG.FILE_NAME, Texture.class));
		tRegion.flip(false, true);
		
		//ratio = new height / picture height
		//width = ratio * picture width
		float width = (dogRunner.GAME_HEIGHT * 1.0f) / tRegion.getRegionHeight() * tRegion.getRegionWidth();

		GameObject trGameObj = new TextureRegionGameObject(dogRunner.GAME_WIDTH / 10f, 0f,
				width, dogRunner.GAME_HEIGHT,
				tRegion);
		stage.addActor(trGameObj);
		trGameObj.setTouchable(Touchable.disabled);
		*/
		
		/*GameObject startButton = new StartButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_WIDTH / 20f,
				dogRunner.GAME_HEIGHT / 4f,
				dogRunner.GAME_HEIGHT / 2f, dogRunner.GAME_HEIGHT / 2f);*/
        float width = image.getWidth() / 5f;
        float height = image.getHeight() / 5f;
        
        
        GameObject startButton = new StartButton((dogRunner.GAME_WIDTH * 100f / 192f) - (width / 2f),
        		(dogRunner.GAME_HEIGHT * 67f / 108f) - (height / 2f),
        		width, height);
		stage.addActor(startButton);
		
		stage.addActor(new HighScoreButton(dogRunner.GAME_WIDTH - width, //(dogRunner.GAME_WIDTH * 150f / 192f) - (width / 2f),
        		(dogRunner.GAME_HEIGHT * 52f / 108f) - (height / 2f),
        		width, height));
		
		stage.addActor(new OptionsButton(0f,//(dogRunner.GAME_WIDTH * 25f / 192f) - (width / 2f),
        		(dogRunner.GAME_HEIGHT * 52f / 108f) - (height / 2f),
        		width, height));
		
		backMusic = dogRunner.assetManager.get(DogMusic.START_THEME.FILE_NAME, Music.class);
		backMusic.setVolume(.4f);
		backMusic.setLooping(true);
	}
	
	@Override
	public void show() {
		
		super.show();
		
		backMusic.play();
	}
	/*
	@Override
	public void render(float delta) {
		
		stage.draw();
	}*/
	
	@Override
	public void hide() {
		
		super.hide();
	}
}
