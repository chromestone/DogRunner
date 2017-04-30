package org.qohs.dogrunner.screens;

import java.text.DecimalFormat;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.ColorInterrupter;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.gameobjects.clicker.*;
import org.qohs.dogrunner.io.DogAtlasRegion;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.util.Countdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * 
 * @author Derek Zhang
 *
 */
public class ClickerScreen extends StageScreen {

	private final static DecimalFormat myFormatter;
	static {
		
		myFormatter = new DecimalFormat("00");
	}
	
	private enum GameState {
		
		BEGIN, RESUMED, END
	}
	
	private GameState state;
	
	private Label instructions;
	
	private Donut donut;
	private PlusNumberManager manager;
	
	private Group resumedGroup;
	
	private Countdown countdown;
	private Label time;
	
	private Label gas;
	
	private QueryButton pauseButton;
	private QueryButton nextButton;
	
	private Label repairedLabel;
	
	private final MyClickListener listener;
	
	public ClickerScreen() {
		
		super();
		
		////////////////////////////////
		state = GameState.BEGIN;
		
		donut = null;
		manager = null;
		////////////////////////////////

		LabelStyle labelStyle;
		
		////////////////////////////////
		labelStyle = new LabelStyle();
		labelStyle.font =  dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.BLACK;
		
		////////////////////////////////
		resumedGroup = new Group();
		
		////////////////////////////////
		countdown = new Countdown(60f);//a minute
		
		time = new Label("0:00", labelStyle);
		time.setX(dogRunner.GAME_WIDTH - time.getWidth());
		time.setAlignment(Align.right);
		
		////////////////////////////////
		Label tGLabel = new Label("Time\nGas: ", labelStyle);
		tGLabel.setX(time.getX() - tGLabel.getWidth());
		
		////////////////////////////////
		pauseButton = new QueryButton(dogRunner.GAME_WIDTH - time.getHeight(), 0f,
				time.getHeight(), time.getHeight(),
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.PAUSE_IMG)));
		
		////////////////////////////////
		nextButton = new QueryButton(dogRunner.GAME_WIDTH - time.getHeight(), 0f,
				time.getHeight(), time.getHeight(),
				new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		
		////////////////////////////////
		instructions = new Label("Click\nDonut\nTo\nGet\nGas!", labelStyle);
		instructions.setX(tGLabel.getX());
//		instructions.setAlignment(Align.left);
		
		stage.addActor(instructions);
		////////////////////////////////
		
		stage.addActor(nextButton);
		////////////////////////////////
		
		resumedGroup.addActor(pauseButton);
		
		time.setY(pauseButton.getHeight());
		resumedGroup.addActor(time);
		
		tGLabel.setY(time.getY());
		resumedGroup.addActor(tGLabel);
		
		/*Label gasLabel = new Label("Gas: ", labelStyle);
		gasLabel.setX(timeLabel.getX());
		gasLabel.setY(timeLabel.getY() + timeLabel.getHeight());
		
		stage.addActor(gasLabel);*/
		
		////////////////////////////////
		repairedLabel = new Label("DONE!\n&\nCar Repaired", labelStyle);
		repairedLabel.setX((dogRunner.GAME_HEIGHT - repairedLabel.getWidth()) / 2f);
		repairedLabel.setY((dogRunner.GAME_HEIGHT - repairedLabel.getHeight()) / 2f);
		repairedLabel.setAlignment(Align.center);

		stage.addActor(repairedLabel);
		
		////////////////////////////////
		
		labelStyle = new LabelStyle();
		labelStyle.font =  dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.BLACK;
		
		////////////////////////////////
		
		gas = new Label("", labelStyle);
		gas.setWrap(true);
		//gas.setWidth();
		//gas.setX(tGLabel.getX());
		//gas.setY(tGLabel.getY() + tGLabel.getHeight() * 1.25f);
		
		ScrollPane sP = new ScrollPane(gas);
		sP.setScrollingDisabled(true, false);
		sP.setX(tGLabel.getX());
		sP.setY(tGLabel.getY() + tGLabel.getHeight());
		sP.setWidth(dogRunner.GAME_WIDTH - sP.getX());
		sP.setHeight(dogRunner.GAME_HEIGHT - sP.getY());
		sP.validate();
		sP.setScrollPercentY(100f);

		gas.setWidth(sP.getWidth());
		
		resumedGroup.addActor(sP);
		resumedGroup.addActor(new ColorInterrupter(dogRunner.batch.getColor()));
		//resumedGroup.addActor(gas);
		
		////////////////////////////////
		
		stage.addActor(resumedGroup);
		
		////////////////////////////////
		listener = new MyClickListener();
	}

	@Override
	public void show() {
		
		super.show();
		
		manager = new PlusNumberManager();
		donut = new Donut(0f, 0f, dogRunner.GAME_HEIGHT, dogRunner.GAME_HEIGHT, manager);
		stage.addActor(donut);
		
		dogRunner.userProfile.lives = 3;
		
		countdown.reset();
		time.setText("1:00");
		
		state = GameState.BEGIN;
		
		resumedGroup.setVisible(false);
		instructions.setVisible(true);
		
		pauseButton.setTouchable(Touchable.disabled);
		
		nextButton.setVisible(false);
		nextButton.setTouchable(Touchable.disabled);
		
		repairedLabel.setVisible(false);
		
		donut.setVisible(true);
		
		stage.addListener(listener);
		
		//rgb(29, 125, 146)
		Gdx.gl.glClearColor(135f / 256f, 195f / 256f, 235f / 256f, 1f);
	}
	
	@Override
	public void render(float delta) {

		switch (state) {

		case BEGIN: {
			
			super.render(delta);
			
			break;
		}
		case RESUMED: {

			gas.setText(Long.toString(dogRunner.userProfile.gas));

			super.render(delta);

			dogRunner.batch.begin();
			manager.render(delta);
			dogRunner.batch.end();

			float seconds = countdown.update(delta);
			if (seconds > 0f) {

				double number = Math.ceil(seconds);
				if (number < 60) {

					time.setText("0:" + myFormatter.format(number));
				}
			}
			else {
				
				state = GameState.END;
				pauseButton.setVisible(false);
				pauseButton.setTouchable(Touchable.disabled);
				nextButton.setVisible(true);
				nextButton.setTouchable(Touchable.enabled);
				
				donut.setTouchable(Touchable.disabled);
				donut.setVisible(false);
				
				time.setText("0:00");
				
				repairedLabel.setVisible(true);
				
				//dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
				
				break;
			}
			
			if (pauseButton.queryClicked()) {
				
				state = GameState.BEGIN;
				
				resumedGroup.setVisible(false);
				instructions.setVisible(true);
				
				stage.addListener(listener);
			}
			
			break;
		}
		case END: {
			
			super.render(delta);
			
			if (nextButton.queryClicked()) {
				
				dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
			}
			
			break;
		}
		default: {
			
			super.render(delta);
			
			break;
		}
		}
	}
	
	@Override
	public void hide() {
		
		donut.remove();
		donut = null;
		manager = null;
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
	}
	
	@Override
	public void pause() {
		
		if (state != GameState.RESUMED) {
			
			return;
		}
		
		state = GameState.BEGIN;
		
		resumedGroup.setVisible(false);
		instructions.setVisible(true);
		
		pauseButton.setTouchable(Touchable.disabled);
		
		nextButton.setVisible(false);
		nextButton.setTouchable(Touchable.disabled);
		
		stage.addListener(listener);
	}
	
	private class MyClickListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			if (x < dogRunner.GAME_HEIGHT && y < dogRunner.GAME_HEIGHT) {
				
				state = GameState.RESUMED;
				
				instructions.setVisible(false);
				resumedGroup.setVisible(true);
				
				pauseButton.setTouchable(Touchable.enabled);
		
				stage.removeListener(this);
			}
		}
	}
}
