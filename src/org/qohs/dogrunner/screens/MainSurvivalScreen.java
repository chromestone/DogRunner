package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.gameobjects.mainsurvival.*;
import org.qohs.dogrunner.io.*;
import org.qohs.dogrunner.text.*;
import org.qohs.dogrunner.text.mainsurvival.*;
import org.qohs.dogrunner.util.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Where the action at. This screen displays the main runner game.
 * 
 * @author Derek Zhang
 * @author Sam Mansfield
 *
 */
public class MainSurvivalScreen extends StageScreen {
	
	private enum GameState {
		
		PAUSED, COUNTDOWN, RESUMED, GAME_OVER
	}
	
	private OrthographicCamera cam;

	private MainSurvivalWorld physicsWorld;
	
	//100 meters across no matter screen size
	private static final float meterHeight = 100f;
	private final float meterWidth;
	
	//
	private UpperClickHandler upperClickHandler;
	private LowerClickHandler lowerClickHandler;
	private ClickListener upperHandler;
	private ClickListener lowerHandler;
	
	private GameState gameState;
	
	private QueryButton pauseButton;
	private QueryButton playButton;
	
	private TextureRegion car;
	private final float carHeight;
	private final float carWidth;
	
	//private CarSpawner carSpawner;
	
	private TextRenderer textRenderer;
	
	private TextObject scoreText;
	
	private Countdown countdown;
	private CenteredText countdownText;
	
	//private RoadManager roadManager;
	
	//game over (end game) fields
	private TextureRegion background;
	private final static Color backgroundColor = new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.5f);
	
	private QueryButton nextButton;
	
	private GameOverScore gOS;
	
	private Music backMusic;
	
	public MainSurvivalScreen(Batch batch) {
		
		super(batch);
		
		////////////////////////////////
		//calculated width
		meterWidth = meterHeight / dogRunner.GAME_HEIGHT * dogRunner.GAME_WIDTH;
		
		////////////////////////////////
		//player's car size calculations
		
		//load car texture
		car = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.PORSCHE_CAR));
		car.flip(false, true);
		
		//fit car into screen
		carHeight = meterHeight / 10f;
		carWidth = carHeight / car.getRegionHeight() * car.getRegionWidth();
		
		////////////////////////////////
		//configure screen units
		cam = new OrthographicCamera();
		cam.setToOrtho(true, meterWidth, meterHeight);
		stage.getViewport().setCamera(cam);
		
		////////////////////////////////
		//set up text
		textRenderer = new TextRenderer(cam);
		textRenderer.add((scoreText = new ScoreText(dogRunner.assetManager.get(DogAsset.ARIAL_RED_M.FILE_NAME, BitmapFont.class))));
	
		////////////////////////////////
		//sets up inputs for movement of player's car
		
		float upperBound = (meterHeight - (meterHeight * .25f)) / 2;
		float lowerBound = upperBound + (meterHeight * .25f);
		
		upperClickHandler = new UpperClickHandler(0f, 0f, meterWidth, upperBound, meterHeight / 12f);
		upperHandler = (ClickListener) upperClickHandler.getListeners().get(0);
		
		stage.addActor(upperClickHandler);
		
		lowerClickHandler = new LowerClickHandler(0f, lowerBound, meterWidth, meterHeight - lowerBound, meterHeight / 12f);
		lowerHandler = (ClickListener) lowerClickHandler.getListeners().get(0);
		
		stage.addActor(lowerClickHandler);
		
		////////////////////////////////
		//pause and play buttons
		pauseButton = new QueryButton(meterWidth - meterHeight / 10f, meterHeight / 2f - meterHeight / 20f, meterHeight / 10f, meterHeight / 10f, 
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.PAUSE_IMG)));
		stage.addActor(pauseButton);
		
		playButton = new QueryButton(meterWidth / 2f - meterHeight / 2f, 0f, meterHeight, meterHeight, 
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		stage.addActor(playButton);
		
		////////////////////////////////
		//count down
		//3.4-"3"-2.4-"2"-1.4-"1"-0.4-"GO"-0.0
		countdown = new Countdown(3.4f);
		countdownText = new CenteredText(dogRunner.assetManager.get(DogAsset.ARIAL_GOLD_L.FILE_NAME, BitmapFont.class));

		////////////////////////////////
		//
		physicsWorld = null;
		gameState = null;
		//roadManager = null;
		
		////////////////////////////////
		//end game stuff
		background = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));
	
		nextButton = new QueryButton(meterWidth - meterHeight / 10f, meterHeight / 2f - meterHeight / 20f, meterHeight / 10f, meterHeight / 10f, 
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
	
		backMusic = dogRunner.assetManager.get(DogMusic.BACKGROUND_THEME.FILE_NAME, Music.class);
		backMusic.setLooping(true);
	}
	
	@Override
	public void show() {
		
		super.show();
		
		//set screen units
		//note that these directly apply to drawing and must be set back once screen changes [this.hide()]
		dogRunner.batch.setProjectionMatrix(cam.combined);
		dogRunner.renderer.setProjectionMatrix(cam.combined);
		
		//set up parameters for the physics world
		MainSurvivalWorld.Definition def = new MainSurvivalWorld.Definition();
		def.meterWidth = meterWidth;
		def.meterHeight = meterHeight;
		def.carWidth = carWidth - meterWidth * (12f / 500f);//5f on a 5:3 ratio; I simplified the equation
		def.carHeight = carHeight - 2f;
		
		upperClickHandler.activated = false;
		lowerClickHandler.activated = false;
		
		physicsWorld = new MainSurvivalWorld(new Vector2(0f, 0f), true, def);

		
		////////////////////////////////
		//the game will start in a state counting down the to the start of the game
		gameState = GameState.COUNTDOWN;
		countdown.reset();
		playButton.setVisible(false);
		playButton.setTouchable(Touchable.disabled);
		pauseButton.setVisible(false);
		pauseButton.setTouchable(Touchable.disabled);
		textRenderer.add(countdownText);
	}
	
	@Override
	public void render(float delta) {
		
		/*
		 * In a resumed state the game will run (act),
		 * check if the player has crashed,
		 * and check if the player has clicked the pause button.
		 * 
		 * In a paused state, the game will check
		 * if the play button has been clicked
		 * and start the countdown sequence
		 * 
		 * In a count down state, the game will count down
		 * and resume the game once the timer goes to 0
		 */
		switch (gameState) {
		
		case RESUMED: {
			
			act(delta);

			if (physicsWorld.playerCarTotalled) {

				dogRunner.assetManager.get(DogSound.CAR_CRASH_BONG.FILE_NAME, Sound.class).play();
				dogRunner.assetManager.get(DogSound.CRASH_DEATH.FILE_NAME, Sound.class).play();

				//dogRunner.setScreen(DogScreens.Type.GAME_OVER_SCREEN);
				//return;
				
				pauseButton.setVisible(false);
				pauseButton.setTouchable(Touchable.disabled);
				upperClickHandler.setVisible(false);
				upperClickHandler.setTouchable(Touchable.disabled);
				lowerClickHandler.setVisible(false);
				lowerClickHandler.setTouchable(Touchable.disabled);
				
				stage.addActor(nextButton);
				
				textRenderer.remove(scoreText);
				textRenderer.add(gOS = new GameOverScore(dogRunner.assetManager.get(DogAsset.ARIAL_YELLOW_L.FILE_NAME, BitmapFont.class)));
				
				gameState = GameState.GAME_OVER;
				
				backMusic.stop();
				
				break;
			}
			
			if (pauseButton.queryClicked()) {
				
				pause();
			}
			
			break;
		}
		case PAUSED: {
			
			if (playButton.queryClicked()) {
				
				gameState = GameState.COUNTDOWN;
				countdown.reset();
				playButton.setVisible(false);
				playButton.setTouchable(Touchable.disabled);
				textRenderer.add(countdownText);
			}
			
			break;
		}
		case COUNTDOWN: {
			
			float seconds = countdown.update(delta) + .6f;
			if (seconds > 0f) {
				
				int number = (int) seconds;
				if (number > 0) {
					
					countdownText.text = "" + number;
				}
				else {
					
					countdownText.text = "GO";
				}
			}
			else {
				
				textRenderer.remove(countdownText);
				
				gameState = GameState.RESUMED;
				
				playButton.setTouchable(Touchable.disabled);
				playButton.setVisible(false);
				pauseButton.setTouchable(Touchable.enabled);
				pauseButton.setVisible(true);
				
				backMusic.play();
			}
			
			break;
		}
		case GAME_OVER: {
			
			if (nextButton.queryClicked()) {
				
				pauseButton.setVisible(true);
				pauseButton.setTouchable(Touchable.enabled);
				upperClickHandler.setVisible(true);
				upperClickHandler.setTouchable(Touchable.enabled);
				lowerClickHandler.setVisible(true);
				lowerClickHandler.setTouchable(Touchable.enabled);
				
				nextButton.remove();
				
				textRenderer.remove(gOS);
				textRenderer.add(scoreText);
				
				if (dogRunner.highScoreFM.highScore.isHighScore(dogRunner.userProfile.score)) {
					
					dogRunner.setScreen(DogScreens.Type.INPUT_HIGH_SCORE_SCREEN);
				}
				else {
					
					dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
				}
				
				return;
			}
	
			break;
		}
		default: {
			
			act(delta);

			if (physicsWorld.playerCarTotalled) {

				dogRunner.assetManager.get(DogSound.CAR_CRASH_BONG.FILE_NAME, Sound.class).play();
				
				pauseButton.setVisible(false);
				upperClickHandler.setVisible(false);
				lowerClickHandler.setVisible(false);
				
				textRenderer.remove(scoreText);
				textRenderer.add(new GameOverScore(dogRunner.assetManager.get(DogAsset.ARIAL_YELLOW_L.FILE_NAME, BitmapFont.class)));
				
				gameState = GameState.GAME_OVER;
				break;
			}
			
			if (pauseButton.queryClicked()) {
				
				pause();
			}
			
			break;
		}
		}
		
		////////////////////////////////
		//rendering starts here
		
		////////////////////////////////
		//SpriteBatch is used to render starting here
		
		dogRunner.batch.begin();
		
		physicsWorld.roadManager.render();
		//physicsWorld.carSpawner.render();
		physicsWorld.spawnManager.render();
		
		//draws the player's car
		//dogRunner.batch.begin();
		dogRunner.batch.draw(car, 0f, physicsWorld.carBody.getPosition().y - carHeight / 2, carWidth, carHeight);
		//dogRunner.batch.end();
		
		/*
		//DEBUGGING
		dogRunner.renderer.begin(ShapeType.Line);
		dogRunner.renderer.rect(physicsWorld.carBody.getPosition().x - (carWidth - 4f) / 2, physicsWorld.carBody.getPosition().y - (carHeight - 2f) / 2, carWidth - 4f, carHeight - 2f);
		dogRunner.renderer.end();
		*/
		
		if (GameState.GAME_OVER == gameState) {
			
			Color color = dogRunner.batch.getColor();
			dogRunner.batch.setColor(backgroundColor);
			
			dogRunner.batch.draw(background, 0, 0, meterWidth, meterHeight);
			
			dogRunner.batch.setColor(color);
		}
		
		dogRunner.batch.end();
		
		stage.draw();
		
		textRenderer.render();
	}
	
	@Override
	public void act(float delta) {
		
		////////////////////////////////
		//stage acting is currently not needed
		//stage.act();
		
		////////////////////////////////
		/*
		 * If one hemisphere of the screen is pressed, then the player's car
		 * will move in that direction
		 * 
		 * Otherwise if both hemispheres are pressed or the player has not
		 * pressed, then the player's car will stop moving
		 */
		//exclusive or
		if (upperHandler.isPressed() ^ lowerHandler.isPressed()) {
			
			if (upperHandler.isPressed()) {
				
				physicsWorld.carBody.setLinearVelocity(0f, -120f);
				upperClickHandler.activated = true;
			}
			else {
				
				physicsWorld.carBody.setLinearVelocity(0f, 120f);
				lowerClickHandler.activated = true;
			}
		}
		else {
			
			physicsWorld.carBody.setLinearVelocity(0f, 0f);
			upperClickHandler.activated = false;
			lowerClickHandler.activated = false;
		}
		
		////////////////////////////////
		//physics (Box2D) related acting
		
		physicsWorld.act(delta);
	}

	@Override
	public void hide() {
		
		super.hide();
		
		//set display units back to normal
		dogRunner.batch.setProjectionMatrix(dogRunner.cam.combined);
		dogRunner.renderer.setProjectionMatrix(dogRunner.cam.combined);

		textRenderer.remove(countdownText);
		
		physicsWorld.dispose();
		physicsWorld = null;
	}

	@Override
	public void resume() {
		
		//***not sure if this is true***needs to be tested***
		//on android assets need to be reloaded (after for example exiting)
		//car = new TextureRegion(fairies.assetManager.get(DogAssets.FERRARI_CAR.fileName, Texture.class));
		//car.flip(false, true);
	}
	
	@Override
	public void pause() {
		
		if (GameState.RESUMED == gameState || GameState.COUNTDOWN == gameState) {
			
			textRenderer.remove(countdownText);

			gameState = GameState.PAUSED;

			pauseButton.setTouchable(Touchable.disabled);
			pauseButton.setVisible(false);
			playButton.setTouchable(Touchable.enabled);
			playButton.setVisible(true);
			
			backMusic.pause();
		}
	}

	@Override
	public void dispose() {

		super.dispose();
		if (physicsWorld != null) {
			
			physicsWorld.dispose();
		}
	}

}
