package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.io.DogAtlasRegion;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StorylineScreen extends StageScreen {

	private Label storyLabel;
	private QueryButton backButton;
	private QueryButton forwardButton;
	
	public StorylineScreen() {
		
		super();
		
		float xPad = dogRunner.GAME_WIDTH / 20f;
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.ARIAL_WHITE_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.BLACK;
		storyLabel = new Label("Iwefjweofijwoieoweifjewoifjweofweijfweoijweofewjfewofjoiejewowejfoiwefjweof", labelStyle);
		storyLabel.setWrap(true);
		storyLabel.setX(xPad);
		storyLabel.setY(dogRunner.GAME_HEIGHT * 2f / 3f);
		storyLabel.setWidth(dogRunner.GAME_WIDTH - xPad * 2);
		storyLabel.setHeight(dogRunner.GAME_HEIGHT / 3f);
		storyLabel.debug();
		stage.addActor(storyLabel);

		AtlasRegion pauseTexture = dogRunner.getAtlasRegion(DogAtlasRegion.PAUSE_IMG);
		
		backButton = new QueryButton(0, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f, dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, 
				new TextureRegion(pauseTexture));
		stage.addActor(backButton);
		
		forwardButton = new QueryButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f, dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, 
				new TextureRegion(pauseTexture));
		stage.addActor(forwardButton);
	}
	
	
}
