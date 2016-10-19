package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.mainsurvival.*;
import org.qohs.dogrunner.io.DogAssets;
import org.qohs.dogrunner.text.CenteredText;
import org.qohs.dogrunner.text.TextRenderer;
import org.qohs.dogrunner.text.mainsurvival.ScoreText;
import org.qohs.dogrunner.util.Countdown;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Where the action at
 * 
 * @author Derek Zhang
 *
 */
public class MainSurvivalScreen extends StageScreen {
	
	private enum GameState {
		
		PAUSED, COUNTDOWN, RESUMED
	}
	
	private OrthographicCamera cam;

	private MainSurvivalWorld physicsWorld;
	
	//100 meters across no matter screen size
	private static final float meterHeight = 100f;
	private final float meterWidth;
	
	private UpperClickHandler upperClickHandler;
	private LowerClickHandler lowerClickHandler ;
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
	
	private Countdown countdown;
	private CenteredText countdownText;
	
	//private RoadManager roadManager;
	
	public MainSurvivalScreen(Batch batch) {
		
		super(batch);
		
		////////////////////////////////
		//calculated width
		meterWidth = meterHeight / dogRunner.GAME_HEIGHT * dogRunner.GAME_WIDTH;
		
		////////////////////////////////
		//player's car size calculations
		
		//load car texture
		car = new TextureRegion(dogRunner.assetManager.get(DogAssets.PORSCHE_CAR.fileName, Texture.class));
		car.flip(false, true);
		
		//fit car into screen
		carHeight = meterHeight / 10f;
		carWidth = carHeight / car.getTexture().getHeight() * car.getTexture().getWidth();
		
		////////////////////////////////
		//configure screen units
		cam = new OrthographicCamera();
		cam.setToOrtho(true, meterWidth, meterHeight);
		stage.getViewport().setCamera(cam);
		
		////////////////////////////////
		//set up text
		textRenderer = new TextRenderer(cam);
		textRenderer.add(new ScoreText(dogRunner.assetManager.get(DogAssets.COMIC_SANS_RED_M.fileName, BitmapFont.class)));
	
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
				new TextureRegion(dogRunner.assetManager.get(DogAssets.PAUSE_IMG.fileName, Texture.class)));
		stage.addActor(pauseButton);
		
		playButton = new QueryButton(meterWidth / 2f - meterHeight / 2f, 0f, meterHeight, meterHeight, 
				new TextureRegion(dogRunner.assetManager.get(DogAssets.RESUME_IMG.fileName, Texture.class)));
		stage.addActor(playButton);
		
		////////////////////////////////
		//count down
		//3.4-"3"-2.4-"2"-1.4-"1"-0.4-"GO"-0.0
		countdown = new Countdown(3.4f);
		countdownText = new CenteredText(dogRunner.assetManager.get(DogAssets.COMIC_SANS_GOLD_L.fileName, BitmapFont.class));

		////////////////////////////////
		//
		physicsWorld = null;
		gameState = null;
		//roadManager = null;
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
		
		//carSpawner = new CarSpawner(physicsWorld.world, meterWidth, meterHeight, carWidth * 1.5f);//with three cars was 1.75//old values//1.5f//4f
		
		//roadManager = new RoadManager(meterWidth, meterHeight, 120f);
		
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

			if (physicsWorld.playerCarCrashed) {

				dogRunner.assetManager.get(DogAssets.CAR_CRASH_BONG.fileName, Sound.class).play();
				dogRunner.setScreen(DogScreens.Type.GAME_OVER_SCREEN);
				return;
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
			
			float seconds = countdown.update(delta) + .4f;
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
			}
			
			break;
		}
		default: {
			
			act(delta);

			if (physicsWorld.playerCarCrashed) {

				dogRunner.assetManager.get(DogAssets.CAR_CRASH_BONG.fileName, Sound.class).play();
				dogRunner.setScreen(DogScreens.Type.GAME_OVER_SCREEN);
				return;
			}
			break;
		}
		}
		
		////////////////////////////////
		//rendering starts here
		
		////////////////////////////////
		//this is the section where the shape renderer is used
		//note that the shape renderer should EVENTUALLY BE REPLACED (by textures/pictures/sprites)
		
		/*
		//Draws the background roads
		dogRunner.renderer.begin(ShapeType.Filled);
		dogRunner.renderer.setColor(Color.BLACK);
		dogRunner.renderer.rect(0f, 0f, meterWidth, meterHeight / 6f);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0f, meterHeight / 6f, meterWidth, meterHeight / 6f);
		dogRunner.renderer.setColor(Color.BLACK);
		dogRunner.renderer.rect(0f, meterHeight / 3f, meterWidth, meterHeight / 6f);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0f, meterHeight / 2f, meterWidth, meterHeight / 6f);
		dogRunner.renderer.setColor(Color.BLACK);
		dogRunner.renderer.rect(0f, meterHeight * 2f / 3f, meterWidth, meterHeight / 6f);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0f, meterHeight * 5f / 6f, meterWidth, meterHeight / 6f);
		dogRunner.renderer.end();
		*/
		
		////////////////////////////////
		//SpriteBatch is used to render starting here
		
		//roadManager.render();
		
		//carSpawner.render();
		physicsWorld.roadManager.render();
		physicsWorld.carSpawner.render();
		
		//draws the player's car
		dogRunner.batch.begin();
		//dogRunner.batch.draw(car, physicsWorld.carBody.getPosition().x - carWidth / 2, physicsWorld.carBody.getPosition().y - carHeight / 2, carWidth, carHeight);
		dogRunner.batch.draw(car, 0f, physicsWorld.carBody.getPosition().y - carHeight / 2, carWidth, carHeight);
		dogRunner.batch.end();
		
		/*
		//DEBUGGING
		dogRunner.renderer.begin(ShapeType.Line);
		dogRunner.renderer.rect(physicsWorld.carBody.getPosition().x - (carWidth - 4f) / 2, physicsWorld.carBody.getPosition().y - (carHeight - 2f) / 2, carWidth - 4f, carHeight - 2f);
		dogRunner.renderer.end();
		*/
		
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
		//carSpawner.act(delta);
		//roadManager.act(delta);
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
		//carSpawner = null;
		//roadManager = null;
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
		
		textRenderer.remove(countdownText);
		
		gameState = GameState.PAUSED;
		
		pauseButton.setTouchable(Touchable.disabled);
		pauseButton.setVisible(false);
		playButton.setTouchable(Touchable.enabled);
		playButton.setVisible(true);
	}

	@Override
	public void dispose() {

		if (physicsWorld != null) {
			
			physicsWorld.dispose();
		}
	}

}
