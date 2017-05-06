package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.ColorInterrupter;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.gameobjects.selectstoryline.NumberedLabel;
import org.qohs.dogrunner.io.DogAtlasRegion;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.io.StorylineFileManager.DialogImageList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 *
 */
public class SelectStorylineScreen extends StageScreen {
	
	private final MyClickListener listener;
	
	private Table scrollTable;
	private ScrollPane sP;
	
	private QueryButton continueButton;

	//private final float initialY;
	
	private final LabelStyle labelStyle;
	private final LabelStyle whiteStyle;
	
	public SelectStorylineScreen() {
		
		super();
		
		listener = new MyClickListener();
		
		////////////////////////////////
		
		LabelStyle titleStyle;
		
		////////////////////////////////

		titleStyle = new LabelStyle();
		titleStyle.font = dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME, BitmapFont.class);

		Label title = new Label("Storyline", titleStyle);
		title.setColor(Color.GOLD);
		title.setX((dogRunner.GAME_WIDTH - title.getWidth()) / 2f);
		title.setY(0f);
		title.setHeight(title.getPrefHeight() * 1.5f);
		
		//initialY = title.getHeight();
		
		////////////////////////////////
		
		float size = title.getHeight() * .75f;
		//float size = dogRunner.GAME_HEIGHT / 10f;
		continueButton = new QueryButton(dogRunner.GAME_WIDTH - size, 0f, size, size, new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		
		////////////////////////////////
		
		scrollTable = new Table();

		sP = new ScrollPane(scrollTable);
		sP.setScrollingDisabled(true, false);
		sP.setX(0f);
		sP.setY(title.getHeight());
		sP.setWidth(dogRunner.GAME_WIDTH);
		sP.setHeight(dogRunner.GAME_HEIGHT - title.getHeight());
		sP.validate();
		sP.setScrollPercentY(100f);
		
		////////////////////////////////
		//setup stage
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		
		stage.addActor(sP);
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		
		stage.addActor(title);
		stage.addActor(continueButton);
		
		////////////////////////////////
		
		BitmapFont font = dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		
		labelStyle = new LabelStyle();
		labelStyle.font = font;
		
		whiteStyle = new LabelStyle();
		whiteStyle.font = font;
		whiteStyle.fontColor = Color.BLACK;
		//TextureRegionDrawable temp = new TextureRegionDrawable(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));
		whiteStyle.background = new TextureRegionDrawable(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));
	}
	
	@Override
	public void show() {
		
		super.show();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		dogRunner.userProfile.inGame = false;//make sure
		
		int unlocked = dogRunner.storyFM.getUnlockedPosition();
		//float y = 0f;
		int count = 0;
		for (int i = unlocked - 1; i >= 0; i--) {
			
			if (i >= dogRunner.storyFM.storylineDialogList.size()) {

				continue;
			}

			DialogImageList storyline = dogRunner.storyFM.storylineDialogList.get(i);

			//if there is a chapter but it is empty
			if (storyline.dialogs.size() == 0) {
				
				continue;
			}
			
			LabelStyle mLabelStyle = new LabelStyle(i % 2 == 0 ? whiteStyle : labelStyle);
			
			NumberedLabel nLabel = new NumberedLabel("Chapter " + (i + 1), mLabelStyle, i);
			
			//nLabel.setY(y);
			
			//nLabel.setHeight(nLabel.getPrefHeight() * 1.5f);
			//y += nLabel.getHeight();
			
			//nLabel.setWidth(dogRunner.GAME_WIDTH);
			
			nLabel.addListener(listener);
			
			//stage.addActor(nLabel);
			scrollTable.columnDefaults(count).width(dogRunner.GAME_WIDTH);
			scrollTable.columnDefaults(count).height(nLabel.getPrefHeight() * 1.5f);
			scrollTable.add(nLabel);
			scrollTable.row();
			
			count++;
		}
		
		sP.validate();
		sP.setScrollPercentY(100f);
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (continueButton.queryClicked()) {
			
			dogRunner.setScreen(DogScreens.Type.OPTIONS_SCREEN);
			
			//return;
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		dogRunner.batch.setColor(Color.WHITE);
		
		scrollTable.clear();
	}

	private class MyClickListener extends ClickListener {
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

			if (event.getTarget() instanceof Label) {
				
				LabelStyle style = ((Label) event.getTarget()).getStyle();
				style.fontColor = style.background == null ? Color.YELLOW : Color.PURPLE;
			}
			
			return super.touchDown(event, x, y, pointer, button);
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			if (event.getTarget() instanceof Label) {
				
				LabelStyle style = ((Label) event.getTarget()).getStyle();
				style.fontColor = style.background == null ? Color.WHITE : Color.BLACK;
			}
			
			super.touchUp(event, x, y, pointer, button);
		}
		
		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

			if (event.getTarget() instanceof Label) {
				
				LabelStyle style = ((Label) event.getTarget()).getStyle();
				style.fontColor = style.background == null ? Color.WHITE : Color.BLACK;
			}
			
			super.exit(event, x, y, pointer, toActor);
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			if (event.getTarget() instanceof NumberedLabel) {
				
				//hacky but works :)
				dogRunner.userProfile.gasStops = ((NumberedLabel) event.getTarget()).num;
				
				dogRunner.setScreen(DogScreens.Type.STORYLINE_SCREEN);
			}
		}
	}
}
