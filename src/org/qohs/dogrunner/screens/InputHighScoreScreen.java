package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

/**
 * I couldn't find a better place to blab:
 * 
 * The difference between pref width and height and just width and height
 * is that width and height is actually what the hit box of the actor is
 * the pref width and height is where the drawn boundaries of the actor is
 * so the actor can actually draw outside of its hit box
 * width and height are set. you have to MANUALLY change them
 * 
 * @author Derek Zhang
 *
 */
public class InputHighScoreScreen extends StageScreen {
	
	//private static Color color = new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.25f);
	
	private Label score;
	//private float underTitleY;
	
	private TextField textField;
	
	private QueryButton continueButton;
	
	private MyWarningDialog warningDialog;
	
	public InputHighScoreScreen() {
		
		super();

		BitmapFont font = dogRunner.assetManager.get(DogFont.YELLOW_L.FILE_NAME, BitmapFont.class);
		BitmapFont smallFont = dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME, BitmapFont.class);
		
		LabelStyle smallLabelStyle = new LabelStyle();
		smallLabelStyle.font = smallFont;
		
		////////////////////////////////
		
		Label name = new Label("Name:", smallLabelStyle);
		name.setY(0f);
		
		stage.addActor(name);
		
		////////////////////////////////

		/*TextFieldStyle tFStyle = new TextFieldStyle();
		tFStyle.font = smallFont;
		tFStyle.fontColor = smallFont.getColor();
		tFStyle.background = new TextureRegionDrawable(
				dogRunner.getAtlasRegion(DogAtlasRegion.BLANK)
				);*/
		
		Skin skin = dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class);
		
		//textField = new TextField("", tFStyle);
		textField = new TextField("", skin);
		textField.getStyle().font = smallFont;
		textField.getStyle().fontColor = smallFont.getColor();
		//textField.getStyle().cursor.setMinHeight(name.getPrefHeight() * 2f);
		//textField.setScale(2f);
		//textField.scaleBy(2f);
		textField.getStyle().cursor = (new TextureRegionDrawable(
				dogRunner.assetManager.get(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas", TextureAtlas.class)
				.findRegion("default-round")));
		textField.setWidth(dogRunner.GAME_WIDTH);
		textField.setHeight(name.getHeight());
		textField.setX(0f);
		textField.setY(name.getY() + name.getHeight());
		textField.setMaxLength(32);
		
		stage.addActor(textField);

		////////////////////////////////
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		
		////////////////////////////////
		float underTitleY;
		
		Label title = new Label("New High Score:", labelStyle);
		
		if (title.getWidth() > dogRunner.GAME_WIDTH) {
			
			title.setFontScale(dogRunner.GAME_WIDTH / title.getWidth());
			title.setWidth(title.getPrefWidth());
		}
		
		title.setX(dogRunner.GAME_WIDTH / 2f - title.getWidth() / 2f);
		
		float titleY = textField.getY() + textField.getHeight();
		float titleY2 = dogRunner.GAME_HEIGHT / 2f - title.getHeight() / 2f;

		title.setY(titleY < titleY2 ? titleY2: titleY);
		
		underTitleY = title.getY() + title.getHeight() * 1.5f;
		
		stage.addActor(title);
		
		////////////////////////////////

		score = new Label("_", labelStyle);
		score.setX(dogRunner.GAME_WIDTH / 2f - score.getWidth() / 2f);
		score.setY(underTitleY);
		score.setAlignment(Align.center);
		score.setText("");
		stage.addActor(score);
		
		////////////////////////////////
		
		continueButton = new QueryButton(dogRunner.GAME_WIDTH - name.getHeight(), 0f, name.getHeight(), name.getHeight(),
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		
		stage.addActor(continueButton);
		
		////////////////////////////////
		
		BitmapFont titleFont = dogRunner.assetManager.get(DogFont.RED_M.FILE_NAME, BitmapFont.class);
		
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = titleFont;
		
		
		//apparently titles don't work very well
		//(also due to change of coordinates, dialogs draw "inverted" but text is upright? :(
		//warningDialog = new MyWarningDialog("Derek programmed most of this.", windowStyle);//, skin);
		warningDialog = new MyWarningDialog("", windowStyle);//, skin);
		
		labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.RED_S.FILE_NAME, BitmapFont.class);
		
		warningDialog.text("No high score will be attributed to you. Continue?", labelStyle);
		//Label dialogLabel = new Label("No high score will be attributed to you. Continue?", labelStyle);
		//dialogLabel.setWrap(true);
		//warningDialog.text(dialogLabel);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = titleFont;
		buttonStyle.downFontColor = Color.BLUE;
		buttonStyle.fontColor = titleFont.getColor();
		
		warningDialog.button("No     ", false, buttonStyle);  //sends "false" as the result
		warningDialog.button("     Yes", true, buttonStyle); //sends "true" as the result
		
		//warningDialog.button("No     ", false);
		//warningDialog.button("     Yes", true);
		
		Color backColor = new Color(Color.DARK_GRAY);
		backColor.a = 0.85f;
		
		warningDialog.setBackground(new TextureRegionDrawable(
				dogRunner.getAtlasRegion(DogAtlasRegion.BLANK))
		.tint(backColor));
		
		warningDialog.setWidth(dogRunner.GAME_WIDTH);
	}
	
	@Override
	public void show() {
		
		super.show();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		score.setText(String.valueOf(dogRunner.userProfile.score));
		score.setWidth(score.getPrefWidth());
		if (score.getWidth() > dogRunner.GAME_WIDTH) {

			score.setFontScale(dogRunner.GAME_WIDTH / score.getWidth());
		}
		//score.setWidth(score.getPrefWidth());
		
		textField.setText("");
		
		continueButton.setTouchable(Touchable.enabled);
		continueButton.setVisible(true);
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (continueButton.queryClicked()) {
			
			String temp = textField.getText().trim();
			if (temp.length() <= 0) {
				
				continueButton.setTouchable(Touchable.disabled);
				continueButton.setVisible(false);
				warningDialog.show(stage);
			}
			else {
				
				dogRunner.highScoreFM.highScore.updateScores(temp, dogRunner.userProfile.score);
				
				dogRunner.highScoreFM.save();
				
				dogRunner.userProfile.reset();
				
				dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
				
				//return;
			}
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		//score.remove();
		warningDialog.remove();
	}
	
	private class MyWarningDialog extends Dialog {

		MyWarningDialog(String title, WindowStyle windowStyle) {
			
			super(title, windowStyle);	
		}
		
		/*
		public MyWarningDialog(String title, Skin skin) {
			
			super(title, skin);
		}
		*/
		
		@Override
		protected void result(Object result) {
			
			if (Boolean.parseBoolean(String.valueOf(result))) {
				
				dogRunner.userProfile.reset();
				
				InputHighScoreScreen.this.dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
			}
			else {
				
				continueButton.setTouchable(Touchable.enabled);
				continueButton.setVisible(true);
			}
		}
	}
}
