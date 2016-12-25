package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.DogAsset;
import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 *
 */
public class InputHighScoreScreen extends StageScreen {
	
	private static Color color = new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.25f);
	
	private Label score;
	private TextField textField;
	
	private QueryButton continueButton;
	
	private MyWarningDialog warningDialog;
	
	public InputHighScoreScreen(Batch batch) {
		
		super(batch);

		BitmapFont font = dogRunner.assetManager.get(DogAsset.ARIAL_YELLOW_L.FILE_NAME, BitmapFont.class);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;

		Label title = new Label("New High Score:", labelStyle);
		title.setX(dogRunner.GAME_WIDTH / 2f - title.getPrefWidth() / 2f);
		title.setY(0f);
		
		stage.addActor(title);
		
		score = new Label("", labelStyle);
		
		stage.addActor(score);
		
		Label name = new Label("Name:", labelStyle);
		name.setY(title.getPrefHeight() * 2f);
		
		stage.addActor(name);
		
		TextFieldStyle tFStyle = new TextFieldStyle();
		tFStyle.font = font;
		tFStyle.fontColor = font.getColor();
		tFStyle.cursor = new TextureRegionDrawable(
				new TextureRegion(dogRunner.assetManager.get(DogTexture.BLANK.FILE_NAME, Texture.class))
				).tint(color);
		tFStyle.background = new TextureRegionDrawable(
				new TextureRegion(dogRunner.assetManager.get(DogTexture.BLANK.FILE_NAME, Texture.class))
				);
		
		textField = new TextField("", tFStyle);
		textField.setX(0);
		textField.setY(name.getY() + name.getPrefHeight());
		textField.setWidth(dogRunner.GAME_WIDTH - title.getPrefHeight());
		textField.setMaxLength(32);
		
		stage.addActor(textField);
		
		continueButton = new QueryButton(dogRunner.GAME_WIDTH - title.getPrefHeight(), dogRunner.GAME_HEIGHT - title.getPrefHeight(), title.getPrefHeight(), title.getPrefHeight(),
				new TextureRegion(dogRunner.assetManager.get(DogTexture.RESUME_IMG.FILE_NAME, Texture.class)));
		
		stage.addActor(continueButton);
		
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = dogRunner.assetManager.get(DogAsset.ARIAL_RED_M.FILE_NAME, BitmapFont.class);
		
		warningDialog = new MyWarningDialog("", windowStyle);
		
		labelStyle = new LabelStyle();
		labelStyle.font = windowStyle.titleFont;
		
		warningDialog.text("Are you sure you want to quit?", labelStyle);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = labelStyle.font;
		buttonStyle.downFontColor = Color.BLUE;
		buttonStyle.fontColor = labelStyle.font.getColor();
		
		warningDialog.button("No     ", false, buttonStyle);  //sends "false" as the result
		warningDialog.button("     Yes", true, buttonStyle); //sends "true" as the result
		
		warningDialog.setBackground(new TextureRegionDrawable(
				new TextureRegion(dogRunner.assetManager.get(DogTexture.BLANK.FILE_NAME, Texture.class)))
				);
	}
	
	@Override
	public void show() {
		
		super.show();
		
		Gdx.gl.glClearColor(1f, 140f / 255f, 0f, 1f);
		
		score.setText(String.valueOf(dogRunner.userProfile.score));
		score.setX(dogRunner.GAME_WIDTH / 2f - score.getPrefWidth() / 2f);
		score.setY(score.getPrefHeight() * 1.5f);

		stage.addActor(score);
				
		textField.setText("");
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (continueButton.queryClicked()) {
			
			String temp = textField.getText().trim();
			if (temp.isEmpty()) {
				
				warningDialog.show(stage);
			}
			else {
				
				dogRunner.highScoreFM.highScore.updateScores(temp, dogRunner.userProfile.score);
				
				dogRunner.highScoreFM.save();
				
				dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
				
				return;
			}
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		score.remove();
		warningDialog.remove();
	}
	
	private class MyWarningDialog extends Dialog {

		public MyWarningDialog(String title, WindowStyle windowStyle) {
			
			super(title, windowStyle);	
		}
		
		@Override
		public void result(Object result) {
			
			if (Boolean.valueOf(String.valueOf(result))) {
				
				InputHighScoreScreen.this.dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
			}
		}
	}
}
