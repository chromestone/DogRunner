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
	}
	
	//load all the "assets" here (pictures, fonts, etc.)
	private void load() {

		//sets the loader so that the AssetManager actually knows how to load data
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		////////////////////////////////
		//
		FreeTypeFontLoaderParameter fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file (must be root, i.e. not in a folder other than assets)
		fTFLP.fontFileName = "comic_sans.ttf";
		fTFLP.fontParameters.size = 40;
		fTFLP.fontParameters.color = Color.RED;
		fTFLP.fontParameters.flip = true;
		
		//name of this does not matter, however must end in .ttf
		assetManager.load(DogAssets.COMIC_SANS40.fileName, BitmapFont.class, fTFLP);
		
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = "comic_sans.ttf";
		fTFLP.fontParameters.size = 10;
		fTFLP.fontParameters.color = Color.RED;
		fTFLP.fontParameters.flip = true;
		
		assetManager.load(DogAssets.COMIC_SANS10.fileName, BitmapFont.class, fTFLP);
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = "comic_sans.ttf";
		fTFLP.fontParameters.size = 70;
		fTFLP.fontParameters.color = Color.GOLD;
		fTFLP.fontParameters.flip = true;

		assetManager.load(DogAssets.COMIC_SANS70.fileName, BitmapFont.class, fTFLP);
		
		
		fTFLP = new FreeTypeFontLoaderParameter();
		//actual directory of file
		fTFLP.fontFileName = "comic_sans.ttf";
		fTFLP.fontParameters.size = 40;
		fTFLP.fontParameters.color = Color.CYAN;
		fTFLP.fontParameters.flip = true;

		assetManager.load(DogAssets.COMIC_SANS_CYAN.fileName, BitmapFont.class, fTFLP);


		//name in this case (pictures) matters [must reflect actual path]
		assetManager.load(DogAssets.DOG_IMG.fileName, Texture.class);
		assetManager.load(DogAssets.PORSCHE_CAR.fileName, Texture.class);
		assetManager.load(DogAssets.WHITE_CAR.fileName, Texture.class);
		assetManager.load(DogAssets.GAME_OVER_EXPLOSION.fileName, Texture.class);
		assetManager.load(DogAssets.PAUSE_IMG.fileName, Texture.class);
		assetManager.load(DogAssets.RESUME_IMG.fileName, Texture.class);
		
		assetManager.load(DogAssets.ENGINE_REV.fileName, Sound.class);
		assetManager.load(DogAssets.CAR_CRASH_BONG.fileName, Sound.class);
	}

	@Override
	public void render() {
		
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
