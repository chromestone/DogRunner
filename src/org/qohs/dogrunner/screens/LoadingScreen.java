package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.DogRunner.Creator;
import org.qohs.dogrunner.io.DogCustomGraphic;
import org.qohs.dogrunner.util.ProgressBarCustomizer;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Not a registered screen (it's rogue :)
 * 
 * @author Derek Zhang
 *
 */
public class LoadingScreen extends StageScreen {

	private ProgressBar progressBar;
	private final Creator creator;
	
	//private boolean assetsLoaded;
	
	private FontSelectScreen fontSelectScreen;
	
	public LoadingScreen(Creator creator) {
		
		super();
		
		progressBar = new ProgressBar(0f, 1f, .01f, false,
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		//progressBar.setX(dogRunner.GAME_WIDTH / 2f - progressBar.getWidth() / 2f);
		progressBar.setX(0f);//-dogRunner.GAME_WIDTH * .25f);
		progressBar.setWidth(dogRunner.GAME_WIDTH); //dogRunner.GAME_WIDTH * 1.25f);
		progressBar.setY(dogRunner.GAME_HEIGHT / 2f - progressBar.getHeight() / 2f);
		
		ProgressBarCustomizer.customize1(dogRunner, progressBar);

		stage.addActor(progressBar);
		
		Label label = new Label("Loading",
						dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		label.setColor(Color.WHITE);
		
		label.setFontScale(2f * dogRunner.GAME_WIDTH / 800f);
		label.setHeight(label.getPrefHeight());
		label.setWidth(label.getPrefWidth());
		
		label.setX(dogRunner.GAME_WIDTH / 2f - label.getWidth() / 2f);
		label.setY(progressBar.getY() - label.getHeight() * 1.5f);
		
		stage.addActor(label);
		
		this.creator = creator;
		
		//assetsLoaded = false;
		
		fontSelectScreen = null;
	}

	@Override
	public void show() {
		
		super.show();
		
		progressBar.setValue(dogRunner.assetManager.getProgress());
	
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1f);
	}
	
	@Override
	public void render(float delta) {
		
		if (dogRunner.assetManager.update()) {

			if (!creator.isIODone()) {

				return;
			}
			
			if (!dogRunner.fontSelectFM.validatedSize) {
				
				if (fontSelectScreen == null) {
					
					fontSelectScreen = new FontSelectScreen();
				}
				
				creator.loadFonts = true;

				dogRunner.setScreen(fontSelectScreen);
				
				return;
			}

			if (creator.create()) {
					
				dogRunner.setScreen(DogScreens.Type.START_SCREEN);
				
				return;
			}
		}
		else {
			
			progressBar.setValue(dogRunner.assetManager.getProgress());
		}
		
		//stage.draw();
		super.render(delta);
	}
	
	@Override
	public void hide() {

		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
	}
}
