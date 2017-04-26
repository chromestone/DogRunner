package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.ColorInterrupter;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.DogAtlasRegion;
import org.qohs.dogrunner.io.DogCustomGraphic;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.*;

/**
 * 
 * FSS
 * 
 * @author Derek Zhang
 *
 */
public class FontSelectScreen extends StageScreen {
	
	private static final String ASSET_NAME = "FontSelectScreen_" + DogFont.ACTUAL_FONT_FILE_NAME;
	
	private boolean instructionDone;
	
	private Label loadingLabel;
	
	private Label instruction;
	private MyClickListener listener;
	
	private Label characterLabel;
	private ScrollPane sP;
	
	private ProgressBar progressBar;
	
	private QueryButton noButton;
	private QueryButton yesButton;
	
	//private AssetManager assetManager;
	private Renderable renderable;
	
	/**
	 * the target font size when displaying the character set sample
	 * (because we don't want fonts to get too wacky or big)
	 */
	private final int targetSize;
	/**
	 * Used for a "binary search" through fonts
	 */
	private int maxSize;
	private int currentSize;
	private int minSize;
	
	public FontSelectScreen() {
		
		super();
		
		loadingLabel = new Label("Loading...",
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		loadingLabel.setColor(Color.WHITE);
		loadingLabel.setWrap(true);
		loadingLabel.setFontScale(2f * dogRunner.GAME_WIDTH / 800f);
		loadingLabel.setX(0);
		loadingLabel.setY(0);
		loadingLabel.setWidth(dogRunner.GAME_WIDTH);

		instruction = new Label("Welcome to Dog Runner.\n"
				+ "This is a first time start up font setup menu."
				+ "\nText will be shown.\n"
				+ "Please press the red no sign if the text shows any defects. (There will be non-alphanumerical characters)\n"
				+ "Otherwise, press the green check button."
				+ "\nTap to continue.",
				//+ "(You may reconfigure in the options menu)",
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		instruction.setColor(Color.WHITE);
		instruction.setWrap(true);
		instruction.setFontScale(2f * dogRunner.GAME_WIDTH / 800f);
		instruction.setX(0);
		instruction.setY(0);
		instruction.setWidth(dogRunner.GAME_WIDTH);
		
		sP = new ScrollPane(instruction);
		sP.setScrollingDisabled(true, false);
		sP.setX(0f);
		sP.setY(0f);
		sP.setWidth(dogRunner.GAME_WIDTH);
		sP.setHeight(dogRunner.GAME_HEIGHT);
		sP.validate();
		sP.setScrollPercentY(100f);

		stage.addActor(sP);
		
		listener = new MyClickListener();
		stage.addListener(listener);
		
		/*
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME);
		characterLabel = new Label(dogRunner.charsetFM.charset,
				labelStyle);//dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		characterLabel.setColor(Color.WHITE);
		characterLabel.setWrap(true);
		characterLabel.setFontScale(2f * dogRunner.GAME_WIDTH / 800f);
		characterLabel.setX(0);
		characterLabel.setY(0);
		characterLabel.setWidth(dogRunner.GAME_WIDTH);*/
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		
		progressBar = new ProgressBar(0f, 1f, .01f, false,
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		progressBar.setX(-dogRunner.GAME_WIDTH * .25f);
		progressBar.setWidth(dogRunner.GAME_WIDTH * 1.25f);
		progressBar.setY(dogRunner.GAME_HEIGHT * .875f - progressBar.getHeight() / 2f);
		
		stage.addActor(progressBar);
		
		////////////////////////////////

		TextureRegion temp = new TextureRegion(dogRunner.assetManager.get(DogTexture.PROHIBITED.FILE_NAME, Texture.class));
		temp.flip(false, true);
		noButton = new QueryButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT * .25f,
				dogRunner.GAME_HEIGHT * .75f,
				dogRunner.GAME_HEIGHT * .25f, dogRunner.GAME_HEIGHT *.25f, 
				temp);
		
		stage.addActor(noButton);
		
		temp = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.CHECK));
		temp.flip(false, true);
		yesButton = new QueryButton(0f, dogRunner.GAME_HEIGHT * .75f,
				dogRunner.GAME_HEIGHT * .25f, dogRunner.GAME_HEIGHT *.25f, 
				temp);
		
		stage.addActor(yesButton);
		
		////////////////////////////////
		
		/*assetManager = new AssetManager();
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, DogFont.SUFFIX, new FreetypeFontLoader(resolver));
		*/
		////////////////////////////////
		
		renderable = null;
		
		////////////////////////////////

		targetSize = (int) (34f * dogRunner.GAME_WIDTH / 800f);
		
		maxSize = 0;
		minSize = 0;
		currentSize = 0;
	}
	
	@Override
	public void show() {
		
		super.show();
		
		updateTextScrollingPlace(instruction);
		
		progressBar.setVisible(false);
		noButton.setVisible(false);
		yesButton.setVisible(false);
		
		renderable = new Loading();
		
		DogFont[] dogFonts = DogFont.values();
		maxSize = dogFonts[0].getSize();
		for (int i = 1; i < dogFonts.length; i++) {
			
			if (dogFonts[i].getSize() > maxSize) {
				
				maxSize = dogFonts[i].getSize();
			}
		}
		
		minSize = DogFont.MINIMUM_SIZE;
		currentSize = (maxSize + minSize) / 2;
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (!instructionDone) {
			
			return;
		}
		
		renderable.render();
	}
	
	/**
	 * Call back when the player has confirmed the instructions
	 */
	private void transitionToSelector() {
		
		instruction.remove();
		stage.removeListener(listener);
		listener = null;
		
		instructionDone = true;
		
		sP.setWidth(dogRunner.GAME_WIDTH);
		sP.setHeight(dogRunner.GAME_HEIGHT * 3f / 4f);
		
//		characterLabel.getStyle().font = dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME, BitmapFont.class);
		
		renderable.show();
	}
	
	@Override
	public void hide() {

		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		try {
			
			dogRunner.assetManager.unload(ASSET_NAME);
		}
		catch(Exception e) {
			
			Gdx.app.log("DogRunner-chromestone-FSS", e.getMessage());
		}
	}
	
	private void updateTextScrollingPlace(Actor widget) {
		
		sP.setWidget(widget);
		sP.validate();
		sP.setScrollPercentY(100f);		
	}
	
	private void updateCharacterLabel() {
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(ASSET_NAME, BitmapFont.class);
		characterLabel = new Label(dogRunner.charsetFM.charset,
				labelStyle);//dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		characterLabel.setColor(Color.WHITE);
		characterLabel.setWrap(true);
		characterLabel.setFontScale((float) targetSize / currentSize);
		characterLabel.setX(0);
		characterLabel.setY(0);
		characterLabel.setWidth(dogRunner.GAME_WIDTH);
	}
	
	private class MyClickListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			transitionToSelector();
		}
	}
	
	private interface Renderable {
		
		void show();
		void render();
	}
	
	private class Loading implements Renderable {

		private final Selecting onDone;
		
		Loading() {
			
			onDone = new Selecting(this);
		}
		
		@Override
		public void show() {
			
			//assetManager.clear();
			try {
				
				dogRunner.assetManager.unload(ASSET_NAME);
			}
			catch(Exception e) {
				
				Gdx.app.log("DogRunner-chromestone-FSS", e.getMessage());
			}
			
			FreeTypeFontLoaderParameter fTFLP = new FreeTypeFontLoaderParameter();
			fTFLP.fontFileName = DogFont.ACTUAL_FONT_FILE_NAME;
			fTFLP.fontParameters.size = currentSize;
			fTFLP.fontParameters.color = Color.WHITE;
			fTFLP.fontParameters.flip = true;

			dogRunner.assetManager.load(ASSET_NAME, BitmapFont.class, fTFLP);
			
			updateTextScrollingPlace(loadingLabel);
			
			progressBar.setValue(dogRunner.assetManager.getProgress());
			progressBar.setVisible(true);
		}
		
		@Override
		public void render() {
			
			if (dogRunner.assetManager.update()) {
				
				updateTextScrollingPlace(loadingLabel);
				progressBar.setVisible(false);
				onDone.show();
				renderable = onDone;
			}
			else {
				
				progressBar.setValue(dogRunner.assetManager.getProgress());
			}
		}
	}
	
	private class Selecting implements Renderable {
		
		private final Loading onLoad;
		
		Selecting(Loading l) {
			
			onLoad = l;
		}

		@Override
		public void show() {
			
			noButton.setVisible(true);
			yesButton.setVisible(true);
			updateCharacterLabel();
			updateTextScrollingPlace(characterLabel);
		}

		@Override
		public void render() {
			
			boolean yes = yesButton.queryClicked();
			if (yes ^ noButton.queryClicked()) {
				
				if (yes) {
					
					minSize = currentSize + 1;
					if (minSize >= maxSize) {
						
						//dogRunner.fontSelectFM.validatedSize = true;
						
						/*
						if (dogRunner.fontSelectFM.fontSizeMax == maxSize) {
							
							dogRunner.setScreen(DogScreens.Type.START_SCREEN);
							return;
						}
						*/
						
						doDatFontThing();
					}
					else {
						
						currentSize = (maxSize + minSize) / 2;
						noButton.setVisible(false);
						onLoad.show();
						renderable = onLoad;
					}
				}
				else {
					
					//bad! font looks weird, make smaller!
					
					maxSize = currentSize - 1;
					if (maxSize <= minSize) {

						maxSize = Math.max(DogFont.MINIMUM_SIZE, maxSize);
						
						//dogRunner.fontSelectFM.validatedSize = true;
						
						doDatFontThing();
					}
					else {
						
						currentSize = (maxSize + minSize) / 2;
						noButton.setVisible(false);
						onLoad.show();
						renderable = onLoad;
					}
				}
			}
		}
		
		private void doDatFontThing() {
			
			dogRunner.fontSelectFM.validatedSize = true;
			dogRunner.fontSelectFM.fontSizeMax = maxSize;
			dogRunner.fontSelectFM.save();
			/*
			for (DogFont dogFont : DogFont.values()) {
				
				if (dogFont.getSize() > maxSize) {
					
					dogRunner.assetManager.unload(dogFont.FILE_NAME);
				}
			}
			*/
			
			DogFont.cap(maxSize);
			//dogRunner.loadFonts();
			dogRunner.requestLoadingScreen();
		}
	}
}
