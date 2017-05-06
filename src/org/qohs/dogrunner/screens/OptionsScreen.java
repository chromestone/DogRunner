package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.ColorInterrupter;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.DogAtlasRegion;
import org.qohs.dogrunner.io.DogFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 *
 */
public class OptionsScreen extends StageScreen {

	private QueryButton continueButton;
	private Label readStoryline;
	private Label resetStoryline;
	private Label reconfigFont;
	
	private MyWarningDialog warningDialog;
	
	public OptionsScreen() {
		
		super();
		
		////////////////////////////////
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		
		////////////////////////////////
		
		LabelStyle labelStyle;
		
		////////////////////////////////

		labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME, BitmapFont.class);
		
		Label title = new Label("Options", labelStyle);
		title.setColor(Color.GOLD);
		title.setX((dogRunner.GAME_WIDTH - title.getWidth()) / 2f);
		title.setY(0f);
		title.setHeight(title.getPrefHeight() * 1.5f);
		
		//float size = dogRunner.GAME_HEIGHT / 10f;
		float size = title.getHeight() * .75f;
		continueButton = new QueryButton(dogRunner.GAME_WIDTH - size, 0f, size, size, new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		
		////////////////////////////////
		
		stage.addActor(continueButton);
		
		stage.addActor(title);
		
		////////////////////////////////
		
		BitmapFont font = dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		
		labelStyle = new LabelStyle();
		labelStyle.font = font;
		
		LabelStyle whiteLabel = new LabelStyle();
		whiteLabel.font = font;
		whiteLabel.fontColor = Color.BLACK;
		//TextureRegionDrawable temp = new TextureRegionDrawable(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));
		whiteLabel.background = new TextureRegionDrawable(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));;
		
		////////////////////////////////
		
		readStoryline = new Label("Read Unlocked Storyline", whiteLabel);
		readStoryline.setX(0f);
		readStoryline.setY(title.getHeight());
		readStoryline.setWidth(dogRunner.GAME_WIDTH);
		readStoryline.setHeight(readStoryline.getPrefHeight() * 1.5f);
		
		stage.addActor(readStoryline);
		
		resetStoryline = new Label("Reset Storyline Bookmark", labelStyle);
		resetStoryline.setX(0f);
		resetStoryline.setY(readStoryline.getY() + readStoryline.getHeight());
		resetStoryline.setHeight(resetStoryline.getPrefHeight() * 1.5f);
		resetStoryline.setWidth(dogRunner.GAME_WIDTH);
		
		stage.addActor(resetStoryline);
		
		whiteLabel = new LabelStyle(whiteLabel);
		reconfigFont = new Label("Reconfigure Font", whiteLabel);
		reconfigFont.setX(0f);
		reconfigFont.setY(resetStoryline.getY() + resetStoryline.getHeight());
		reconfigFont.setWidth(dogRunner.GAME_WIDTH);
		reconfigFont.setHeight(resetStoryline.getPrefHeight() * 1.5f);
		
		stage.addActor(reconfigFont);
		
		MyClickListener listener = new MyClickListener();
		readStoryline.addListener(listener);
		resetStoryline.addListener(listener);
		reconfigFont.addListener(listener);
		
		BitmapFont titleFont = dogRunner.assetManager.get(DogFont.RED_M.FILE_NAME, BitmapFont.class);
		
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = titleFont;
		
		////////////////////////////////

		warningDialog = new MyWarningDialog("", windowStyle);//, skin);
		
		labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.RED_S.FILE_NAME, BitmapFont.class);
		
		warningDialog.text("Are you sure you want to reset the bookmark?", labelStyle);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = titleFont;
		buttonStyle.downFontColor = Color.BLUE;
		buttonStyle.fontColor = titleFont.getColor();
		
		warningDialog.button("No     ", false, buttonStyle);  //sends "false" as the result
		warningDialog.button("     Yes", true, buttonStyle); //sends "true" as the result
		
		Color backColor = new Color(Color.DARK_GRAY);
		backColor.a = 0.85f;
		
		warningDialog.setBackground(new TextureRegionDrawable(
				dogRunner.getAtlasRegion(DogAtlasRegion.BLANK))
		.tint(backColor));
		
		//warningDialog.setWidth(dogRunner.GAME_WIDTH);
	}
	
	@Override
	public void show() {
		
		super.show();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
	}
	
	@Override
	public void render(float delta) {
		
		if (continueButton.queryClicked()) {
			
			dogRunner.setScreen(DogScreens.Type.START_SCREEN);
			
			return;
		}
		
		super.render(delta);
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		warningDialog.remove();
		
		dogRunner.batch.setColor(Color.WHITE);
	}

	private class MyClickListener extends ClickListener {
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

			//event.getTarget().setColor(Color.YELLOW);
			if (event.getTarget() instanceof Label) {
				
				LabelStyle style = ((Label) event.getTarget()).getStyle();
				style.fontColor = style.background == null ? Color.YELLOW : Color.PURPLE;
			}
			
			return super.touchDown(event, x, y, pointer, button);
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			//event.getTarget().setColor(Color.WHITE);
			if (event.getTarget() instanceof Label) {
				
				LabelStyle style = ((Label) event.getTarget()).getStyle();
				style.fontColor = style.background == null ? Color.WHITE : Color.BLACK;
			}
			
			super.touchUp(event, x, y, pointer, button);
		}
		
		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

			//event.getTarget().setColor(Color.WHITE);
			if (event.getTarget() instanceof Label) {
				
				LabelStyle style = ((Label) event.getTarget()).getStyle();
				style.fontColor = style.background == null ? Color.WHITE : Color.BLACK;
			}
			
			super.exit(event, x, y, pointer, toActor);
		}
		
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			if (event.getTarget() == reconfigFont) {
				
				dogRunner.fontSelectFM.validatedSize = false;
				for (DogFont dogFont : DogFont.values()) {
					
					dogRunner.assetManager.unload(dogFont.FILE_NAME);
				}
				dogRunner.requestLoadingScreen();
			}
			else if (event.getTarget() == resetStoryline) {
				
				warningDialog.show(stage);
				if (warningDialog.getPrefWidth() > dogRunner.GAME_WIDTH) {

					warningDialog.setScale(dogRunner.GAME_WIDTH / warningDialog.getPrefWidth());
				}
			}
			else {
				
				dogRunner.setScreen(DogScreens.Type.SELECT_STORY_SCREEN);
			}
		}
	}
	
	private class MyWarningDialog extends Dialog {

		MyWarningDialog(String title, WindowStyle windowStyle) {
			
			super(title, windowStyle);	
		}
		
		@Override
		protected void result(Object result) {
			
			if (Boolean.parseBoolean(String.valueOf(result))) {
				
				OptionsScreen.this.dogRunner.storyFM.setReadPosition(0);
			}
		}
	}
}
