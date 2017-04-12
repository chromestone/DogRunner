package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.ColorInterrupter;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.DogCustomGraphic;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.io.DogAtlasRegion;
import org.qohs.dogrunner.io.StorylineFileManager.DialogListRead;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StorylineScreen extends StageScreen {

	//private static String DIRECTORY = DogRunner.PARENT_DIR + "/storyline/";
	
	private Label storyLabel;
	private ScrollPane sP;
	private QueryButton backButton;
	private QueryButton forwardButton;
	private int index;
	
	private DialogListRead storyline;
	
	private ProgressBar progressBar;
	
	private TextureRegionDrawable imageDrawable;
	private Image image;
	
	private AssetManager assetManager;
	private Renderable renderable;
	
	public StorylineScreen() {
		
		super();
		
		float xPad = dogRunner.GAME_WIDTH / 80f;
		float yPad = dogRunner.GAME_HEIGHT / 80f;
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.BLACK;
		labelStyle.background =  new TextureRegionDrawable(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK)).tint(Color.WHITE);
		storyLabel = new Label("", labelStyle);
		//storyLabel.setText(dogRunner.storyFM.storylineDialogList.get(0).get(0).dialog + dogRunner.storyFM.storylineDialogList.get(0).get(1).dialog);
		storyLabel.setWrap(true);
		//storyLabel.setX(xPad);
		//storyLabel.setY(dogRunner.GAME_HEIGHT * 2f / 3f);
		storyLabel.setWidth(dogRunner.GAME_WIDTH - xPad * 2);
		//storyLabel.setHeight(dogRunner.GAME_HEIGHT / 3f);
		/*float height = dogRunner.GAME_HEIGHT / 3f;
		float scale = .95f;
		while (storyLabel.getPrefHeight() > height && scale > 0) {
			
			storyLabel.setFontScale(scale);
			scale -= .05f;
		}
		storyLabel.setHeight(height);*/
		//storyLabel.debug();
		sP = new ScrollPane(storyLabel);
		sP.setScrollingDisabled(true, false);
		sP.setX(xPad);
		sP.setY(dogRunner.GAME_HEIGHT * 2f / 3f + yPad);
		sP.setWidth(dogRunner.GAME_WIDTH - xPad * 2);
		sP.setHeight(dogRunner.GAME_HEIGHT / 3f - yPad * 2);
		sP.validate();
		sP.setScrollPercentY(100f);
		//sP.act(1f);
		stage.addActor(sP);
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		
		////////////////////////////////

		progressBar = new ProgressBar(0f, 1f, .0001f, false,
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		progressBar.setX(-dogRunner.GAME_WIDTH * .25f);
		progressBar.setWidth(dogRunner.GAME_WIDTH * 1.25f);
		progressBar.setY((dogRunner.GAME_HEIGHT - progressBar.getHeight()) / 2f);
		
		stage.addActor(progressBar);
		
		////////////////////////////////
		
		imageDrawable = new TextureRegionDrawable(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));

		image = new Image(imageDrawable, Scaling.fit);
		image.setHeight(dogRunner.GAME_HEIGHT * 2f / 3f);
		image.setWidth(dogRunner.GAME_WIDTH);
		
		this.image.setColor(Color.BLACK);
		
		stage.addActor(image);

		////////////////////////////////
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		
		////////////////////////////////
		
		AtlasRegion pauseTexture = dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG);
		
		TextureRegion temp = new TextureRegion(pauseTexture);
		temp.flip(true,  false);
		backButton = new QueryButton(0, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f, dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, 
				temp);
		stage.addActor(backButton);
		
		forwardButton = new QueryButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f, dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, 
				new TextureRegion(pauseTexture));
		stage.addActor(forwardButton);
		
		////////////////////////////////
		
		assetManager = new AssetManager();
		
		renderable = null;
	}
	
	@Override
	public void show() {
		
		super.show();
		
		//if there is no chapter corresponding to this gas stop
		if (dogRunner.userProfile.gasStops >=
				dogRunner.storyFM.storylineDialogList.size()) {
			
			if (dogRunner.userProfile.gasStops > 0) {
				
				dogRunner.setScreen(DogScreens.Type.CLICKER_SCREEN);
			}
			else {
				
				dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
			}
			return;
		}
		
		storyline = dogRunner.storyFM.storylineDialogList.get(dogRunner.userProfile.gasStops);
		
		//if this gas stop corresponding chapter has been read
		if (dogRunner.userProfile.gasStops < dogRunner.storyFM.getReadPosition()) {

			//go to clicker if not first stop
			if (dogRunner.userProfile.gasStops > 0) {

				dogRunner.setScreen(DogScreens.Type.CLICKER_SCREEN);
			}
			else {
				
				dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
			}
			
			return;
		}
		
		//if there is a chapter but it is empty
		if (storyline.dialogs.size() == 0) {//storyline.read) {
			
			//dogRunner.storyFM.readPosition = dogRunner.userProfile.gasStops + 1;
			dogRunner.storyFM.setReadPosition(dogRunner.userProfile.gasStops);
			if (dogRunner.userProfile.gasStops > 0) {
				
				dogRunner.setScreen(DogScreens.Type.CLICKER_SCREEN);
			}
			else {
				
				dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
			}
			
			return;
		}
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		index = 0;
		/*storyLabel.setText(storyline.dialogs.get(index).dialog);
		sP.validate();
		sP.setScrollPercentY(100f);*/
		updateText();
		
		for (String image : storyline.images) {
			
			assetManager.load(image, Texture.class);
		}
		
		renderable = new Loader();
		renderable.show();
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		renderable.render();
	}
	
	private void updateText() {
		
		storyLabel.setText(storyline.dialogs.get(index).dialog);
		sP.validate();
		sP.setScrollPercentY(100f);
	}
	
	private void updateImage() {
		
		String imagePath = storyline.dialogs.get(index).image;
		if (imagePath == null) {
			
			return;
		}
		
		TextureRegion region = new TextureRegion(assetManager.get(imagePath, Texture.class));
		region.flip(false, true);
		imageDrawable.setRegion(region);
		image.invalidate();
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		//storyline.read = true;
		//dogRunner.storyFM.readPosition = dogRunner.userProfile.gasStops + 1;
		
		assetManager.clear();
	}
	
	private interface Renderable {
		
		void show();
		void render();
	}
	
	
	private class Loader implements Renderable {

		private final Storyline s;
		
		Loader() {
			
			s = new Storyline();
		}
		
		@Override
		public void show() {
			
			sP.setVisible(false);
			progressBar.setVisible(true);
			
			forwardButton.setVisible(false);
			backButton.setVisible(false);
			
			image.setVisible(false);
			
			progressBar.setValue(assetManager.getProgress());
		}

		@Override
		public void render() {

			if (assetManager.update()) {
				
				renderable = s;
				renderable.show();
			}
				
			progressBar.setValue(assetManager.getProgress());
		}
	}
	
	private class Storyline implements Renderable {

		@Override
		public void show() {
			
			sP.setVisible(true);
			progressBar.setVisible(false);
			
			forwardButton.setVisible(true);
			image.setVisible(true);
			
			String imagePath = storyline.dialogs.get(index).image;
			if (imagePath == null) {
				
				return;
			}
			
			image.setColor(Color.WHITE);
			updateImage();
		}

		@Override
		public void render() {

			boolean back = backButton.queryClicked();
			boolean forward = forwardButton.queryClicked();
			if (forward ^ back) {

				if (forward) {
					
					int size = storyline.dialogs.size();
					if (index < size - 1) {

						index++;
						
						backButton.setVisible(true);
						
						updateText();
						updateImage();
					}
					else {
					
						//dogRunner.storyFM.readPosition = dogRunner.userProfile.gasStops + 1;
						dogRunner.storyFM.setReadPosition(dogRunner.userProfile.gasStops + 1);
						if (dogRunner.userProfile.gasStops > 0) {

							dogRunner.setScreen(DogScreens.Type.CLICKER_SCREEN);
						}
						else {

							dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
						}
					}
				}
				else if (index >= 1) {

					index--;
					
					if (index == 0) {
						
						backButton.setVisible(false);
					}
					forwardButton.setVisible(true);
					
					updateText();
					updateImage();
				}
			}
		}
	}
}
