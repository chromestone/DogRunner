package org.qohs.dogrunner.screens;

import java.text.DecimalFormat;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.gameobjects.clicker.*;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.util.Countdown;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
		
		BEGIN, RESUMED, PAUSED, END
	}
	
	private GameState state;
	
	private Donut donut;
	private PlusNumberManager manager;
	
	private Countdown countdown;
	private Label time;
	
	private Label gas;
	
	private QueryButton pauseButton;
	private QueryButton resumeButton;
	
	public ClickerScreen() {
		
		super();
		
		donut = null;
		manager = null;
		
		////////////////////////////////

		LabelStyle labelStyle;
		
		////////////////////////////////

		labelStyle = new LabelStyle();
		labelStyle.font =  dogRunner.assetManager.get(DogFont.YELLOW_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.BLACK;
		
		////////////////////////////////
		
		
		////////////////////////////////
		countdown = new Countdown(60f);//about a minute
		
		time = new Label("0:00", labelStyle);
		time.setX(dogRunner.GAME_WIDTH - time.getWidth());
		time.setAlignment(Align.right);
		stage.addActor(time);
		
		Label timeLabel = new Label("Time\nGas: ", labelStyle);
		timeLabel.setX(time.getX() - timeLabel.getWidth());
		stage.addActor(timeLabel);
		
		/*Label gasLabel = new Label("Gas: ", labelStyle);
		gasLabel.setX(timeLabel.getX());
		gasLabel.setY(timeLabel.getY() + timeLabel.getHeight());
		
		stage.addActor(gasLabel);*/
		
		labelStyle = new LabelStyle();
		labelStyle.font =  dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		labelStyle.fontColor = Color.BLACK;
		
		gas = new Label("", labelStyle);
		gas.setX(timeLabel.getX());
		gas.setY(timeLabel.getY() + timeLabel.getHeight() * 1.25f);
		
		stage.addActor(gas);
		
		state = null;
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
		
		state = GameState.RESUMED;
	}
	
	@Override
	public void render(float delta) {

		switch (state) {

		case RESUMED: {

			gas.setText(""+ dogRunner.userProfile.gas);

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

				dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
			}
			
			break;
		}
		default: {
			
			break;
		}
		}
	}
	
	@Override
	public void hide() {
		
		donut.remove();
		donut = null;
		manager = null;
	}
}
