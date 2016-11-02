package org.qohs.dogrunner;

import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2D;

/**
 * I wrote this class; thug life.
 * 
 * @author Derek Zhang
 *
 */
public class DogRunner extends Game {
	
	//since the screen cannot be resized these can be considered as constants
	//DO NOT CHANGE THESE CONSTANTS outside of this.create()
	public int GAME_WIDTH;
	public int GAME_HEIGHT;
	
	public AssetManager assetManager;
		
	//DO NOT MODIFY THIS; use it as a default pixel by pixel reference cam
	public OrthographicCamera cam;
	
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	
	//screens should not be able to use this
	//since screens should only communicate by switching screens
	//and the user profile
	private DogScreens fairyScreens;
	
	public UserProfile userProfile;
	
	//FPSLogger log = new FPSLogger();
	
	@Override
	public void create() {

		GAME_WIDTH = Gdx.graphics.getWidth();
		GAME_HEIGHT = Gdx.graphics.getHeight();
		
		////////////////////////////////
		//Setup assets and load
		assetManager = new AssetManager();
		
		load();

		assetManager.finishLoading();
		
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
		
		////////////////////////////////
		//Instantiates the Box2D "engine"
		Box2D.init();
		
		////////////////////////////////
		//Instantiates the screens
		fairyScreens = new DogScreens(batch);
		
		////////////////////////////////
		//Sets the first screen users will see
		this.setScreen(DogScreens.Type.START_SCREEN.getStageScreen(fairyScreens));
		
		userProfile = new UserProfile();
		
		//sets the initial clear color for the screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
	}
	
	//load all the "assets" here (pictures, fonts, etc.)
	private void load() {

		//sets the loader so that the AssetManager actually knows how to load data
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		String arial = "Arial.ttf";
		
		////////////////////////////////
		//setups parameters and loads fonts with "Free Type Font"
		FreeTypeFontLoaderParameter fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file (must be root, i.e. not in a folder other than assets)
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 40d / 800d);
		fTFLP.fontParameters.color = Color.RED;
		fTFLP.fontParameters.flip = true;
		
		//name of this does not matter, however must end in .ttf
		assetManager.load(DogAssets.ARIAL_RED_M.FILE_NAME, BitmapFont.class, fTFLP);
		
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 20d / 800d);
		fTFLP.fontParameters.color = Color.RED;
		fTFLP.fontParameters.flip = true;
		
		assetManager.load(DogAssets.ARIAL_RED_S.FILE_NAME, BitmapFont.class, fTFLP);
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 100d / 800d);
		fTFLP.fontParameters.color = Color.GOLD;
		fTFLP.fontParameters.flip = true;

		assetManager.load(DogAssets.ARIAL_GOLD_L.FILE_NAME, BitmapFont.class, fTFLP);
		
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = arial;
		fTFLP.fontParameters.size = (int) (GAME_WIDTH * 90d / 800d);
		fTFLP.fontParameters.color = Color.YELLOW;//new Color(128f/255f, 128f/255f, 128f/255f, 1f);
		fTFLP.fontParameters.borderColor = Color.BLACK;
		fTFLP.fontParameters.borderWidth = (float) (GAME_WIDTH / 800);
		fTFLP.fontParameters.flip = true;
		//fTFLP.fontParameters.genMipMaps = true;
		//fTFLP.fontParameters.magFilter = Texture.TextureFilter.MipMapLinearLinear;
		//fTFLP.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearLinear;

		assetManager.load(DogAssets.ARIAL_BLACK_L.FILE_NAME, BitmapFont.class, fTFLP);

		////////////////////////////////
		//loads picture assets

		//name in this case (pictures) matters [must reflect actual path]
		assetManager.load(DogAssets.DOG_IMG.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.PORSCHE_CAR.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.WHITE_CAR.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.GAME_OVER_EXPLOSION.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.PAUSE_IMG.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.RESUME_IMG.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.TRIANGLE_GRAY_IMG.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.TRIANGLE_GOLD_IMG.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.ROAD_IMG.FILE_NAME, Texture.class);
		assetManager.load(DogAssets.EXPLODE.FILE_NAME, Texture.class);
		
		////////////////////////////////
		//loads sound assets
		assetManager.load(DogAssets.IGNITION_REV.FILE_NAME, Sound.class);
		assetManager.load(DogAssets.CAR_CRASH_BONG.FILE_NAME, Sound.class);
	}

	@Override
	public void render() {
		
		//log.log();
		
		//clears the screen (with set color)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//sets color for next clear and can be overridden in the screens
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
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
		
		assetManager.dispose();
		
		batch.dispose();
		renderer.dispose();
		
		////////////////////////////////
		//Disposes screens
		fairyScreens.dispose();
	}
	
	public void setScreen(DogScreens.Type type) {
		
		super.setScreen(type.getStageScreen(fairyScreens));
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
}
