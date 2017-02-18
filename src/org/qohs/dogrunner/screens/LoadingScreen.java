package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.DogRunner.Creator;
import org.qohs.dogrunner.io.DogCustomGraphic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 *
 */
public class LoadingScreen extends StageScreen {

	private ProgressBar progressBar;
	private final Creator creator;
	
	private boolean assetsLoaded;
	
	public LoadingScreen(Creator creator) {
		
		super();
		
		progressBar = new ProgressBar(0f, 1f, .0001f, false,
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		//progressBar.setX(dogRunner.GAME_WIDTH / 2f - progressBar.getWidth() / 2f);
		progressBar.setX(-dogRunner.GAME_WIDTH * .25f);
		progressBar.setWidth(dogRunner.GAME_WIDTH * 1.25f);
		progressBar.setY(dogRunner.GAME_HEIGHT / 2f - progressBar.getHeight() / 2f);
		
		TextureRegionDrawable d;
		
		d = (new TextureRegionDrawable(
						dogRunner.assetManager.get(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas", TextureAtlas.class)
						.findRegion("default-round-large")));
		
		progressBar.getStyle().background.setMinHeight(d.getMinHeight());
		progressBar.getStyle().background.setMinWidth(d.getMinWidth());
		
		progressBar.getStyle().knob = d.tint(Color.GREEN);
		progressBar.getStyle().knobBefore = progressBar.getStyle().knob;
		progressBar.getStyle().knobAfter = null;

		stage.addActor(progressBar);
		
		Label label = new Label("Loading",
						dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		label.setColor(Color.BLACK);
		
		label.setX(dogRunner.GAME_WIDTH / 2f - label.getWidth() / 2f);
		label.setY(progressBar.getY() - label.getHeight() * 1.5f);
		
		stage.addActor(label);
		
		this.creator = creator;
		
		assetsLoaded = false;
	}

	
	@Override
	public void render(float delta) {
		
		if (assetsLoaded) {

			if (creator.finished()) {

				dogRunner.setScreen(DogScreens.Type.START_SCREEN);
				
				return;
			}
		}
		else {
			
			progressBar.setValue(dogRunner.assetManager.getProgress());
			
			if (assetsLoaded = dogRunner.assetManager.update()) {
				
				creator.create();
			}
		}
		
		stage.draw();
	}
}
