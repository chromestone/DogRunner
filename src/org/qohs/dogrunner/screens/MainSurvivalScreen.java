package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.ColorInterrupter;
import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.gameobjects.mainsurvival.*;
import org.qohs.dogrunner.io.*;
import org.qohs.dogrunner.util.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

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
	
	private QueryButton quitButton;
	private QueryDialog quitDialog;
	
	private TextureRegion car;
	private final float carHeight;
	private final float carWidth;
	
	//private CarSpawner carSpawner;
	
	//private TextRenderer textRenderer;
	
	//private TextObject scoreText;
	
	private ScoreText scoreText;
	
	private Countdown countdown;
	private Label countdownText;
	
	//private RoadManager roadManager;
	
	////////////////////////////////
	
	//game over (end game) fields
	private TextureRegion background;
	private final static Color backgroundColor = new Color(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, 0.5f);
	
	private Label gameOver;
	private Label gOScore;
	
	/*/**
	 * Displays game over text
	 */
	//private GameOverScore gOS;
	
	////////////////////////////////
	
	private Music backMusic;
	
	public MainSurvivalScreen() {
		
		super();
		
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
		//stage.getViewport().setCamera(cam);
		
		////////////////////////////////
		//set up text
		//textRenderer = new TextRenderer(cam);
		//textRenderer.add((scoreText = new ScoreText(dogRunner.assetManager.get(DogFont.RED_M.FILE_NAME, BitmapFont.class))));
		
		////////////////////////////////
		
		stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));

		////////////////////////////////
		
		GameObject ghost = new InvincibilityIndicator(0, 0, dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT);
		ghost.setTouchable(Touchable.disabled);
		stage.addActor(ghost);
		
		////////////////////////////////
		
		LabelStyle labelStyle;
		
		////////////////////////////////
		//count down
		//3.4-"3"-2.4-"2"-1.4-"1"-0.4-"GO"-0.0
		countdown = new Countdown(3.4f);
		labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.GOLD_L.FILE_NAME, BitmapFont.class);
		
		countdownText = new Label("_", labelStyle);
		
		countdownText.setX((dogRunner.GAME_WIDTH - countdownText.getWidth()) / 2f);
		countdownText.setY((dogRunner.GAME_HEIGHT - countdownText.getHeight()) / 2f);
		countdownText.setAlignment(Align.center);
		countdownText.setText("");
		countdownText.setTouchable(Touchable.disabled);

		stage.addActor(countdownText);
		
		////////////////////////////////
		
		labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.YELLOW_L.FILE_NAME, BitmapFont.class);
		
		gameOver = new Label("GAME OVER\nSCORE:", labelStyle);
		
		gameOver.setX((dogRunner.GAME_WIDTH - gameOver.getWidth()) / 2f);
		gameOver.setY(dogRunner.GAME_HEIGHT / 2f - gameOver.getHeight());
		
		gameOver.setTouchable(Touchable.disabled);
		gameOver.setAlignment(Align.center);

		stage.addActor(gameOver);
		
		gOScore = new Label("_", labelStyle);
		
		gOScore.setX((dogRunner.GAME_WIDTH - gOScore.getWidth()) / 2f);
		gOScore.setY(dogRunner.GAME_HEIGHT / 2f);
		
		gOScore.setTouchable(Touchable.disabled);
		gOScore.setAlignment(Align.center);
		gOScore.setText("");
		
		stage.addActor(gOScore);
		
		////////////////////////////////
		//sets up inputs for movement of player's car
		
		float upperBound = (dogRunner.GAME_HEIGHT - (dogRunner.GAME_HEIGHT * .25f)) / 2;
		float lowerBound = upperBound + (dogRunner.GAME_HEIGHT * .25f);
		
		upperClickHandler = new UpperClickHandler(0f, 0f, dogRunner.GAME_WIDTH, upperBound, dogRunner.GAME_HEIGHT / 12f);
		upperHandler = (ClickListener) upperClickHandler.getListeners().get(0);
		
		stage.addActor(upperClickHandler);
		
		lowerClickHandler = new LowerClickHandler(0f, lowerBound, dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT - lowerBound, dogRunner.GAME_HEIGHT / 12f);
		lowerHandler = (ClickListener) lowerClickHandler.getListeners().get(0);
		
		stage.addActor(lowerClickHandler);
		
		////////////////////////////////
		scoreText = new ScoreText(0f, 0f, dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT / 6f);
		scoreText.setTouchable(Touchable.disabled);
		stage.addActor(scoreText);
		
		////////////////////////////////
		//pause and play buttons
		pauseButton = new QueryButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f,
				dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, 
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.PAUSE_IMG)));
		stage.addActor(pauseButton);
		
		playButton = new QueryButton(dogRunner.GAME_WIDTH - dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f,
				dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, 
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		/*new QueryButton(dogRunner.GAME_WIDTH / 2f - dogRunner.GAME_HEIGHT / 2f, 0f, dogRunner.GAME_HEIGHT, dogRunner.GAME_HEIGHT, 
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));*/
		stage.addActor(playButton);
		
		////////////////////////////////
		
		//stage.addActor(new ColorInterrupter(dogRunner.batch.getColor()));

		////////////////////////////////
		
		TextureRegion temp = new TextureRegion(dogRunner.assetManager.get(DogTexture.PROHIBITED.FILE_NAME, Texture.class));
		temp.flip(false, true);
		quitButton = new QueryButton(0f, dogRunner.GAME_HEIGHT / 2f - dogRunner.GAME_HEIGHT / 20f,
				dogRunner.GAME_HEIGHT / 10f, dogRunner.GAME_HEIGHT / 10f, temp);
		quitButton.setTouchable(Touchable.disabled);
		
		stage.addActor(quitButton);
		
		////////////////////////////////

		BitmapFont titleFont = dogRunner.assetManager.get(DogFont.RED_M.FILE_NAME, BitmapFont.class);
		
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = titleFont;//dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
	
		//apparently titles don't work very well
		quitDialog = new QueryDialog("", windowStyle);//, skin);
		
		labelStyle = new LabelStyle();
		labelStyle.font = dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.RED;
		
		quitDialog.text("Are you sure you want to quit?", labelStyle);
		
		TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = titleFont;
		buttonStyle.downFontColor = Color.BLUE;
		buttonStyle.fontColor = titleFont.getColor();
		
		quitDialog.button("No     ", false, buttonStyle);  //sends "false" as the result
		quitDialog.button("     Yes", true, buttonStyle); //sends "true" as the result
		
		Color backColor = new Color(Color.DARK_GRAY);
		backColor.a = 0.85f;
		
		quitDialog.setBackground(new TextureRegionDrawable(
				dogRunner.getAtlasRegion(DogAtlasRegion.BLANK))
		.tint(backColor));
		
		//quitDialog.setX((dogRunner.GAME_WIDTH - quitDialog.getPrefWidth()) / 2f);
		//quitDialog.setY((dogRunner.GAME_HEIGHT - quitDialog.getPrefHeight()) / 2f);
		//quitDialog.setWidth(dogRunner.GAME_WIDTH);

		////////////////////////////////
		//
		physicsWorld = null;
		gameState = null;
		//roadManager = null;
		
		////////////////////////////////
		//end game stuff
		background = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.BLANK));
		
		////////////////////////////////
	
		backMusic = dogRunner.assetManager.get(DogMusic.BACKGROUND_THEME.FILE_NAME, Music.class);
		backMusic.setLooping(true);
	}
	
	@Override
	public void show() {
		
		super.show();
				
		//set screen units
		//note that these directly apply to drawing and must be set back once screen changes [this.hide()]
		//dogRunner.batch.setProjectionMatrix(cam.combined);
		dogRunner.renderer.setProjectionMatrix(cam.combined);
		
		//set up parameters for the physics world
		MainSurvivalWorld.Definition def = new MainSurvivalWorld.Definition();
		def.meterWidth = meterWidth;
		def.meterHeight = meterHeight;
		def.carWidth = carWidth - meterWidth * (12f / 500f);//5f on a 5:3 ratio; I simplified the equation
		def.carHeight = carHeight - 2f;
		
		upperClickHandler.setVisible(true);
		upperClickHandler.setTouchable(Touchable.enabled);
		lowerClickHandler.setVisible(true);
		lowerClickHandler.setTouchable(Touchable.enabled);
		
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
		
		quitButton.setVisible(false);
		
		//textRenderer.add(countdownText);
		//stage.addActor(countdownText);
		
		countdownText.setVisible(true);
		
		scoreText.setVisible(true);
		//scoreText.setText("SCORE: " + dogRunner.userProfile.score);
		
		gameOver.setVisible(false);
		gOScore.setVisible(false);
		
		dogRunner.assetManager.get(DogMusic.START_THEME.FILE_NAME, Music.class).stop();
		
		dogRunner.assetManager.get(DogSound.IGNITION_REV.FILE_NAME, Sound.class).play();//.play(1f);
		
		removePowerUps();
		
		com.badlogic.gdx.Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
	}
	
	private void removePowerUps() {
		
		dogRunner.userProfile.invincible = 0;
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

			doGameResumed();
			
			break;
		}
		case PAUSED: {
			
			QueryDialog.State state = quitDialog.getState();
			switch (state) {
			
			case INPUTTED: {
				
				String result = quitDialog.queryResult();
				
				if (Boolean.parseBoolean((result))) {
					
					dogRunner.setScreen(DogScreens.Type.START_SCREEN);
					return;
				}
					
				quitButton.setVisible(true);
				quitButton.setTouchable(Touchable.enabled);
				playButton.setVisible(true);
				playButton.setTouchable(Touchable.enabled);
				
				//quitDialog.remove();
				
				break;
			}
			case WAITING: {

				break;
			}
			case IDLE: {

				doGamePaused();
				
				break;
			}
			default: {

				doGamePaused();
				
				break;
			}
			}
			
			break;
		}
		case COUNTDOWN: {
			
			float seconds = countdown.update(delta) + .6f;
			if (seconds > 0f) {
				
				int number = (int) seconds;
				if (number > 0) {
					
					//countdownText.text = "" + number;
					countdownText.setText("" + number);
					/*if (countdownText.getPrefWidth() != countdownText.getWidth()) {

						countdownText.setWidth(countdownText.getPrefWidth());
						countdownText.setX((dogRunner.GAME_WIDTH - countdownText.getWidth()) / 2f);
					}*/
					/*countdownText.layout();
					if (countdownText.getPrefWidth() != countdownText.getWidth()) {
						System.out.println("hi");
						System.out.println(countdownText.getWidth());
						
					}*/
				}
				else {

					//countdownText.text = "GO";
					countdownText.setText("GO");
					/*if (countdownText.getPrefWidth() != countdownText.getWidth()) {

						countdownText.setWidth(countdownText.getPrefWidth());
						countdownText.setX((dogRunner.GAME_WIDTH - countdownText.getWidth()) / 2f);
					}*/
					/*countdownText.invalidate();
					countdownText.setY((dogRunner.GAME_HEIGHT - countdownText.getHeight()) / 2f);
					countdownText.setX((dogRunner.GAME_WIDTH - countdownText.getWidth()) / 2f);
				*/}
				
				//dogRunner.timer.delay((long) (delta * 1000L));
			}
			else {
				
				//textRenderer.remove(countdownText);
				//countdownText.remove();
				countdownText.setVisible(false);
				
				gameState = GameState.RESUMED;
				
				playButton.setTouchable(Touchable.disabled);
				playButton.setVisible(false);
				pauseButton.setTouchable(Touchable.enabled);
				pauseButton.setVisible(true);
				
				backMusic.play();
				
				dogRunner.timerHelper.resume();
			}
			
			break;
		}
		case GAME_OVER: {

			if (playButton.queryClicked()) {//nextButton.queryClicked()) {
				
				/*pauseButton.setVisible(true);
				pauseButton.setTouchable(Touchable.enabled);*/
				/*
				upperClickHandler.setVisible(true);
				upperClickHandler.setTouchable(Touchable.enabled);
				lowerClickHandler.setVisible(true);
				lowerClickHandler.setTouchable(Touchable.enabled);
				*/
				
				//nextButton.remove();
				
				//textRenderer.remove(gOS);
				//textRenderer.add(scoreText);
				
				if (dogRunner.highScoreFM.highScore.isHighScore(dogRunner.userProfile.score)) {
					
					dogRunner.setScreen(DogScreens.Type.INPUT_HIGH_SCORE_SCREEN);
				}
				else {
					
					dogRunner.userProfile.reset();
					dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
				}
				
				//return;
			}
	
			break;
		}
		default: {
			
			act(delta);

			doGameResumed();
			
			break;
		}
		}
		
		////////////////////////////////
		//rendering starts here

		////////////////////////////////
		//SpriteBatch is used to render starting here
		
		//the stage will always mutate the batch
		dogRunner.batch.setProjectionMatrix(cam.combined);
		Color color = dogRunner.batch.getColor();
		dogRunner.batch.setColor(Color.WHITE);
		dogRunner.batch.begin();
		
		//physicsWorld.roadManager.render();
		//physicsWorld.carSpawner.render();
		//physicsWorld.spawnManager.render();
		physicsWorld.render();
		
		boolean invincible = dogRunner.userProfile.invincible > 0;
		if (invincible) {
			
			dogRunner.batch.setColor(1f, 1f, 1f, .5f);
		}
		
		//draws the player's car
		dogRunner.batch.draw(car, physicsWorld.carBody.getPosition().x - carWidth / 2,
				physicsWorld.carBody.getPosition().y - carHeight / 2, carWidth, carHeight);

		/*if (invincible) {

			dogRunner.batch.setColor(color);
		}*/
		
		/*dogRunner.batch.draw(car,
				physicsWorld.carBody.getPosition().x - carWidth / 2, physicsWorld.carBody.getPosition().y - carHeight / 2,
				carWidth / 2, carHeight / 2,
				carWidth, carHeight,
				1f, 1f,
				0f);//(float) (physicsWorld.carBody.getAngle() / Math.PI) * 180f);

		/*
		 * draw(TextureRegion region,
                 float x,
                 float y,
                 float originX,
                 float originY,
                 float width,
                 float height,
                 float scaleX,
                 float scaleY,
                 float rotation)
		 */
		//dogRunner.batch.end();
		
		/*
		//DEBUGGING
		dogRunner.renderer.begin(ShapeType.Line);
		dogRunner.renderer.rect(physicsWorld.carBody.getPosition().x - (carWidth - 4f) / 2, physicsWorld.carBody.getPosition().y - (carHeight - 2f) / 2, carWidth - 4f, carHeight - 2f);
		dogRunner.renderer.end();
		*/
		
		if (gameState == GameState.GAME_OVER || gameState == GameState.PAUSED) {
			
			dogRunner.batch.setColor(backgroundColor);
			
			dogRunner.batch.draw(background, 0, 0, meterWidth, meterHeight);
		}
		
		//dogRunner.batch.draw(dogRunner.gudrunThingFM.donutThings.get(3), 0, 0);//(int) (Math.random() * dogRunner.gudrunThingFM.donutThings.size())), 0, 0);
		
		dogRunner.batch.end();
		dogRunner.batch.setColor(color);
		
		//DEBUGGING
		//physicsWorld.method(cam);
		
		//scoreText.setText("SCORE: " + dogRunner.userProfile.score);
		/*stage.act(delta);
		stage.draw();*/

		super.render(delta);

		//textRenderer.render();
	}
	
	/**
	 * Called every cycle the game is resumed.
	 * I really just implemented it for convenience since both the default and RESUMED case
	 * runs this segment of code.
	 */
	private void doGameResumed() {

		if (dogRunner.userProfile.lives <= 0){//physicsWorld.playerCarTotalled) {

			//dogRunner.assetManager.get(DogSound.CAR_CRASH_BONG.FILE_NAME, Sound.class).play();
			dogRunner.assetManager.get(DogSound.CRASH_DEATH.FILE_NAME, Sound.class).play();

			//dogRunner.setScreen(DogScreens.Type.GAME_OVER_SCREEN);
			//return;
			
			pauseButton.setVisible(false);
			pauseButton.setTouchable(Touchable.disabled);
			upperClickHandler.setVisible(false);
			upperClickHandler.setTouchable(Touchable.disabled);
			lowerClickHandler.setVisible(false);
			lowerClickHandler.setTouchable(Touchable.disabled);
			
			//stage.addActor(nextButton);
			
			playButton.setVisible(true);
			playButton.setTouchable(Touchable.enabled);
			
			//textRenderer.remove(scoreText);
			//textRenderer.add(gOS = new GameOverScore(dogRunner.assetManager.get(DogFont.YELLOW_L.FILE_NAME, BitmapFont.class)));
			
			scoreText.setVisible(false);
			
			gameOver.setVisible(true);
			gOScore.setVisible(true);
			if (dogRunner.userProfile.score != 4) {
				
				gOScore.setText("" + dogRunner.userProfile.score);
			}
			//easter egg of the year
			else {

				gOScore.setText("Go Gudrun!");
			}
			
			//gOScore.setWidth(gOScore.getPrefWidth());
			if (gOScore.getPrefWidth() > dogRunner.GAME_WIDTH) {

				gOScore.setFontScale(dogRunner.GAME_WIDTH / gOScore.getWidth());
			}
			
			gameState = GameState.GAME_OVER;
			
			backMusic.stop();
			
			if (dogRunner.userProfile.spin) {

				cam.setToOrtho(true, meterWidth, meterHeight);
			}
			
			return;
		}
		
		if (dogRunner.userProfile.storylineTime) {
			
			dogRunner.userProfile.storylineTime = false;
			dogRunner.setScreen(DogScreens.Type.STORYLINE_SCREEN);
			return;
		}
		
		if (pauseButton.queryClicked()) {
			
			pause();
			
			return;
		}
		
		if (dogRunner.userProfile.spin) {

			cam.rotate(.1f);
		}
	}
	
	private void doGamePaused() {
		
		if (quitButton.queryClicked()) {
			
			quitButton.setVisible(false);
			quitButton.setTouchable(Touchable.disabled);
			playButton.setVisible(false);
			playButton.setTouchable(Touchable.disabled);
			
			quitDialog.show(stage);//, null);
		}
		else if (playButton.queryClicked()) {
			
			gameState = GameState.COUNTDOWN;
			countdown.reset();
			
			
			playButton.setVisible(false);
			playButton.setTouchable(Touchable.disabled);
			
			quitButton.setTouchable(Touchable.disabled);
			quitButton.setVisible(false);
			
			//countdownText.text = "";
			//textRenderer.add(countdownText);
			//countdownText.setText("");
			//stage.addActor(countdownText);
		}
	}
	
	//@Override
	private void act(float delta) {
		
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
		if ((upperHandler.isPressed() || com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) ^ (lowerHandler.isPressed() || com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN))) {
			
			if (upperHandler.isPressed() || com.badlogic.gdx.Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) {
				
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
		
		//stage.act(delta);
	}

	@Override
	public void hide() {
		
		super.hide();
		
		//set display units back to normal
		dogRunner.batch.setProjectionMatrix(dogRunner.cam.combined);
		dogRunner.renderer.setProjectionMatrix(dogRunner.cam.combined);

		//textRenderer.remove(countdownText);
		//countdownText.remove();
		
		backMusic.stop();
		
		physicsWorld.dispose();
		//physicsWorld = null;
				
		dogRunner.timer.clear();
		
		quitDialog.remove();
		
		com.badlogic.gdx.Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
	}
	
	@Override
	public void resume() {
		
		//***not sure if this is true***needs to be tested***
		//**as of now apparently not needed**
		//on android assets need to be reloaded (after for example exiting)
		//car = new TextureRegion(dogRunner.assetManager.get(DogAssets.FERRARI_CAR.fileName, Texture.class));
		//car.flip(false, true);
	}
	
	@Override
	public void pause() {
		
		if (GameState.RESUMED == gameState || GameState.COUNTDOWN == gameState) {
			
			//textRenderer.remove(countdownText);
			//countdownText.remove();

			gameState = GameState.PAUSED;

			pauseButton.setTouchable(Touchable.disabled);
			pauseButton.setVisible(false);
			playButton.setTouchable(Touchable.enabled);
			playButton.setVisible(true);
			
			quitButton.setTouchable(Touchable.enabled);
			quitButton.setVisible(true);
			
			countdownText.setText("PAUSED");
			/*countdownText.setWidth(countdownText.getPrefWidth());
			countdownText.setX((dogRunner.GAME_WIDTH - countdownText.getWidth()) / 2f);*/
			//stage.addActor(countdownText);
			countdownText.setVisible(true);
			
			dogRunner.timerHelper.pause();
			
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
