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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
	
	private ClickListener upperHandler;
	private ClickListener lowerHandler;
	
	private GameState gameState;
	
	private QueryButton pauseButton;
	private QueryButton playButton;
	
	private TextureRegion car;
	private final float carHeight;
	private final float carWidth;
//	private final float carRatio;
	
	private CarSpawner carSpawner;
	
	private TextRenderer textRenderer;
	
	private Countdown countdown;
	private CenteredText countdownText;
	
	public MainSurvivalScreen(Batch batch) {
		
		super(batch);
		
		//calculated width
		meterWidth = meterHeight / dogRunner.GAME_HEIGHT * dogRunner.GAME_WIDTH;
		
		//load car texture
		car = new TextureRegion(dogRunner.assetManager.get(DogAssets.FERRARI_CAR.fileName, Texture.class));
		car.flip(false, true);
		
		//fit car into screen
		carHeight = meterHeight / 8f;
		carWidth = carHeight / car.getTexture().getHeight() * car.getTexture().getWidth();
//		carRatio = carWidth / carHeight;
		
		//configure screen units
		cam = new OrthographicCamera();
		cam.setToOrtho(true, meterWidth, meterHeight);
		stage.getViewport().setCamera(cam);
		
		//set up text
		textRenderer = new TextRenderer(cam);
		textRenderer.add(new ScoreText(dogRunner.assetManager.get(DogAssets.COMIC_SANS40.fileName, BitmapFont.class)));
	
		float upperBound = (meterHeight - (meterHeight * .25f)) / 2;
		float lowerBound = upperBound + (meterHeight * .25f);
		
		ClickHandler clickHandler = new ClickHandler(0, 0, meterWidth, upperBound);
		upperHandler = (ClickListener) clickHandler.getListeners().get(0);
		
		stage.addActor(clickHandler);
		
		clickHandler = new ClickHandler(0, lowerBound, meterWidth, meterHeight - lowerBound);
		lowerHandler = (ClickListener) clickHandler.getListeners().get(0);
		
		stage.addActor(clickHandler);
		
		pauseButton = new QueryButton(meterWidth - meterHeight / 10, meterHeight / 2 - meterHeight / 20, meterHeight / 10, meterHeight / 10, 
				new TextureRegion(dogRunner.assetManager.get(DogAssets.PAUSE_IMAGE.fileName, Texture.class)));
		stage.addActor(pauseButton);
		
		playButton = new QueryButton(meterWidth / 2 - meterHeight / 2, 0f, meterHeight, meterHeight, 
				new TextureRegion(dogRunner.assetManager.get(DogAssets.RESUME_IMAGE.fileName, Texture.class)));
		stage.addActor(playButton);
		
		//3.4-"3"-3.0-"2"-2.0-"1"-1.0-"GO"-0.0
		countdown = new Countdown(3.4f);
		countdownText = new CenteredText(dogRunner.assetManager.get(DogAssets.COMIC_SANS70.fileName, BitmapFont.class));
		
		physicsWorld = null;
		gameState = null;
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
		def.carWidth = carWidth - meterWidth * (21f / 500f);//7f on a 5:3 ratio; I simplified the equation
		def.carHeight = carHeight - 4f;
		
		physicsWorld = new MainSurvivalWorld(new Vector2(0f, 0f), true, def);
		
		carSpawner = new CarSpawner(physicsWorld.world, meterWidth, meterHeight, carWidth * 1.5f);//with three cars was 1.75//old values//1.5f//4f
		
		//resume();
		gameState = GameState.COUNTDOWN;
		countdown.reset();
		playButton.setVisible(false);
		playButton.setTouchable(Touchable.disabled);
		textRenderer.add(countdownText);
	}
	
	@Override
	public void render(float delta) {
		
		switch (gameState) {
		
		case RESUMED: {
			
			act(delta);

			if (physicsWorld.carCrashed) {

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
			
			float seconds = countdown.update(delta);
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

			if (physicsWorld.carCrashed) {

				dogRunner.assetManager.get(DogAssets.CAR_CRASH_BONG.fileName, Sound.class).play();
				dogRunner.setScreen(DogScreens.Type.GAME_OVER_SCREEN);
				return;
			}
			break;
		}
		}
		
		////////////////////////////////
		//Draws the background roads
		dogRunner.renderer.begin(ShapeType.Filled);
		dogRunner.renderer.setColor(Color.BLACK);
		dogRunner.renderer.rect(0, 0, meterWidth, meterHeight / 6);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0, meterHeight / 6, meterWidth, meterHeight / 6);
		dogRunner.renderer.setColor(Color.BLACK);
		dogRunner.renderer.rect(0, meterHeight / 3, meterWidth, meterHeight / 6);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0, meterHeight / 2, meterWidth, meterHeight / 6);
		dogRunner.renderer.setColor(Color.BLACK);
		dogRunner.renderer.rect(0, meterHeight * 2 / 3, meterWidth, meterHeight / 6);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0, meterHeight * 5 / 6, meterWidth, meterHeight / 6);
		dogRunner.renderer.end();
		////////////////////////////////
		
		carSpawner.render();
		
		//draws the player's car
		dogRunner.batch.begin();
		dogRunner.batch.draw(car, physicsWorld.carBody.getPosition().x - carWidth / 2, physicsWorld.carBody.getPosition().y - carHeight / 2, carWidth, carHeight);
		dogRunner.batch.end();
		
		/*
		//DEBUGGING
		dogRunner.renderer.begin(ShapeType.Line);
		dogRunner.renderer.rect(physicsWorld.carBody.getPosition().x - (carWidth - 7f) / 2, physicsWorld.carBody.getPosition().y - (carHeight - 4f) / 2, carWidth - 7f, carHeight - 4f);
		dogRunner.renderer.end();
		*/
		
		stage.draw();
		
		textRenderer.render();
	}
	
	@Override
	public void act(float delta) {
		
		//stage acting is currently not needed
		//stage.act();
		
		physicsWorld.act(delta);
		carSpawner.act(delta);
		
		//exclusive or
		if (upperHandler.isPressed() ^ lowerHandler.isPressed()) {
			
			if (upperHandler.isPressed()) {
				
				physicsWorld.carBody.setLinearVelocity(0, -120);
			}
			else {
				
				physicsWorld.carBody.setLinearVelocity(0, 120);
			}
		}
		else {
			
			physicsWorld.carBody.setLinearVelocity(0, 0);
		}
	}

	@Override
	public void hide() {
		
		super.hide();
		
		//set display units back to normal
		dogRunner.batch.setProjectionMatrix(dogRunner.cam.combined);
		dogRunner.renderer.setProjectionMatrix(dogRunner.cam.combined);

		physicsWorld.dispose();
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
