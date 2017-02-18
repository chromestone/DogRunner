package org.qohs.dogrunner;

import org.qohs.dogrunner.io.*;
import org.qohs.dogrunner.screens.LoadingScreen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * I wrote all of this class; thug life.
 * 
 * @author Derek Zhang
 *
 */
public class DogRunner extends Game {
	
	//since the screen cannot be resized these can be considered as constants
	//DO NOT CHANGE THESE CONSTANTS outside of this.create()
	public int GAME_WIDTH;
	public int GAME_HEIGHT;
	
	/**
	 * DUE TO PROJECT CONFIGURATION, THIS IS ONLY USED IN DESKTOP MODE
	 * ON ANDROID, EMPTY STRING
	 */
	public static final String PARENT_DIR = "assets/";
	
	public AssetManager assetManager;
		
	//DO NOT MODIFY THIS; use it as a default pixel by pixel reference cam
	public OrthographicCamera cam;
	
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	
	private Creator creator;
	
	//screens should not be able to use this
	//since screens should only communicate by switching screens
	//and the user profile
	private DogScreens dogScreens;
	
	public UserProfile userProfile;
	
	public HighScoreFileManager highScoreFM;
	public StorylineFileManager storyFM;
	public DonutThingManager gudrunThingFM;
	
	//FPSLogger log = new FPSLogger();
	
	@Override
	public void create() {

		GAME_WIDTH = Gdx.graphics.getWidth();
		GAME_HEIGHT = Gdx.graphics.getHeight();
		
		////////////////////////////////
		//Setup assets and load
		assetManager = new AssetManager();
		
		forceLoad();
		
		load();

		//assetManager.finishLoading();
		
		////////////////////////////////
		//Camera setup
		cam = new OrthographicCamera();
		cam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		////////////////////////////////
		//Instantiate "drawers" here
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		
		renderer = new ShapeRenderer();
		renderer.setProjectionMatrix(cam.combined);
		
		this.setScreen(new LoadingScreen(creator = new Creator()));
		
		//sets the initial clear color for the screen
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
	}
	
	/**
	 * Blocking IO
	 */
	private void createWithIO() {
	
		////////////////////////////////
		//Instantiates the Box2D "engine"
		//Box2D.init();
	
		////////////////////////////////
		//Instantiates the screens
		//dogScreens = new DogScreens(batch);
	
		////////////////////////////////
		//Sets the first screen users will see
		//this.setScreen(new LoadingScreen(new Creator()));
	
		////////////////////////////////
		//userProfile = new UserProfile();
	
		////////////////////////////////
		highScoreFM = new HighScoreFileManager();
		highScoreFM.load();
	
		storyFM = new StorylineFileManager();
		storyFM.load();
	
		gudrunThingFM = new DonutThingManager();
		gudrunThingFM.load();
	}
	
	private void m_create() {
		
		////////////////////////////////
		//Instantiates the Box2D "engine"
		Box2D.init();
		
		////////////////////////////////
		userProfile = new UserProfile();
	
		////////////////////////////////
		//Instantiates the screens
		dogScreens = new DogScreens(batch);
	
		////////////////////////////////
		//Sets the first screen users will see
		//this.setScreen(new LoadingScreen(new Creator()));
	}

	private void forceLoad() {
		
		assetManager.load(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas", TextureAtlas.class);

		assetManager.load(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class,
				new SkinLoader.SkinParameter(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas"));
		
		assetManager.finishLoading();
	}
	
	//load all the "assets" here (pictures, fonts, etc.)
	private void load() {

		//sets the loader so that the AssetManager actually knows how to load data
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		String arial = "square.ttf";
		
		////////////////////////////////
		//setups parameters and loads fonts with "Free Type Font"
		FreeTypeFontLoaderParameter fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file (must be root, i.e. not in a folder other than assets)
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 50d / 800d);
		fTFLP.fontParameters.color = Color.RED;
		fTFLP.fontParameters.flip = true;
		
		//name of this does not matter, however must end in .ttf
		assetManager.load(DogAsset.ARIAL_RED_M.FILE_NAME, BitmapFont.class, fTFLP);
		
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 30d / 800d);
		fTFLP.fontParameters.color = Color.RED;
		fTFLP.fontParameters.flip = true;
		
		assetManager.load(DogAsset.ARIAL_RED_S.FILE_NAME, BitmapFont.class, fTFLP);
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 110d / 800d);
		fTFLP.fontParameters.color = Color.GOLD;
		fTFLP.fontParameters.flip = true;

		assetManager.load(DogAsset.ARIAL_GOLD_L.FILE_NAME, BitmapFont.class, fTFLP);
		
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 100d / 800d);
		fTFLP.fontParameters.color = Color.YELLOW;//new Color(128f/255f, 128f/255f, 128f/255f, 1f);
		fTFLP.fontParameters.borderColor = Color.BLACK;
		fTFLP.fontParameters.borderWidth = (float) (GAME_WIDTH / 800);
		fTFLP.fontParameters.flip = true;
		//fTFLP.fontParameters.genMipMaps = true;
		//fTFLP.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearLinear;
		//fTFLP.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearLinear;

		assetManager.load(DogAsset.ARIAL_YELLOW_L.FILE_NAME, BitmapFont.class, fTFLP);

		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 55d / 800d);
		fTFLP.fontParameters.color = Color.YELLOW;//new Color(128f/255f, 128f/255f, 128f/255f, 1f);
		fTFLP.fontParameters.borderColor = Color.BLACK;
		fTFLP.fontParameters.borderWidth = (float) (GAME_WIDTH / 800);
		fTFLP.fontParameters.flip = true;
		
		assetManager.load(DogAsset.ARIAL_YELLOW_M.FILE_NAME, BitmapFont.class, fTFLP);
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file (must be root, i.e. not in a folder other than assets)
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 40d / 800d);
		fTFLP.fontParameters.color = Color.WHITE;
		fTFLP.fontParameters.flip = true;
		
		//name of this does not matter, however must end in .ttf
		assetManager.load(DogAsset.ARIAL_WHITE_M.FILE_NAME, BitmapFont.class, fTFLP);
		
		////////////////////////////////
		//loads picture assets

		//name in this case (pictures) matters [must reflect actual path]
		for (DogTexture dogTexture : DogTexture.values()) {
			
			assetManager.load(dogTexture.FILE_NAME, Texture.class);
		}
		
		////////////////////////////////
		//loads picture assets with Texture Atlas
		for (DogTextureAtlas dogTextureAtlas : DogTextureAtlas.values()) {
			
			assetManager.load(dogTextureAtlas.FILE_NAME, TextureAtlas.class);
		}
		
		////////////////////////////////
		//loads sound assets
		for (DogSound dogSound : DogSound.values()) {
			
			assetManager.load(dogSound.FILE_NAME, Sound.class);
		}
		
		////////////////////////////////
		//loads music assets
		for (DogMusic dogSound : DogMusic.values()) {
			
			assetManager.load(dogSound.FILE_NAME, Music.class);
		}
	}

	@Override
	public void render() {
		
		//log.log();
		
		//clears the screen (with set color)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//sets color for next clear and can be overridden in the screens
		//Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		super.render();
	}
	
	@Override
	public void resize(int width, int height) {
		
		//RESIZE SHOULD NEVER HAPPEN
//		cam.setToOrtho(true, width, height);
//		cam.update();
	}
	
	@Override
	public void dispose() {
		
		batch.dispose();
		renderer.dispose();
		assetManager.dispose();
		
		if (!creator.finished) {
			
			return;
		}
		
		highScoreFM.save();
		
		
		////////////////////////////////
		//Disposes screens
		dogScreens.dispose();
	}
	
	public void setScreen(DogScreens.Type type) {
		
		super.setScreen(type.getStageScreen(dogScreens));
	}
	
	/**
	 * Convenience method to get the instance of the current Game (Fairies).
	 * @return the current Game (Fairies)
	 */
	public static DogRunner getInstance() {
		
		ApplicationListener listener = Gdx.app.getApplicationListener();
		
		//something is wrong if this isn't true
		assert (listener instanceof DogRunner);
			
		return ((DogRunner) listener);
	}
	
	/**
	 * Convenience method
	 * @param dTA the enum for a specific sprite
	 * @return the corresponding atlas region (sprite) to the dTA
	 */
	public AtlasRegion getAtlasRegion(DogAtlasRegion dTA) {
		
		return assetManager.get(dTA.ENCLOSING_ATLAS.FILE_NAME, TextureAtlas.class).findRegion(dTA.NAME);
	}
	
	public class Creator {
		
		private boolean created;
		private boolean finished;
		
		private Creator(){
			
			created = false;
			finished = false;
		}
		
		public void create() {
			
			if (created) {
				
				return;
			}
			
			Thread thread = new Thread(new CallCreate(this));
			
			thread.start();

			created = true;
		}
		
		public boolean finished() {
			
			return finished;
		}
	}
	
	private class CallCreate implements Runnable {

		private final Creator creator;
		
		public CallCreate(Creator creator) {
			
			this.creator = creator;
		}
		
		@Override
		public void run() {
			
			DogRunner.this.createWithIO();

			Gdx.app.postRunnable(new CallCreator(creator));
		}
	}
	
	private class CallCreator implements Runnable {

		private final Creator creator;
		
		public CallCreator(Creator creator) {
			
			this.creator = creator;
		}
		
		@Override
		public void run() {
			
			DogRunner.this.m_create();
			
			creator.finished = true;
		}
	}
}
