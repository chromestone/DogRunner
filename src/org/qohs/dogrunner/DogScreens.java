package org.qohs.dogrunner;

import org.qohs.dogrunner.screens.*;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * I tried to encapsulate the screens accessibility by other screens.
 * This ended up complicated but we will stick to this.
 * 
 * Did you see my use of academic language in the first sentence?
 * 
 * @author Derek Zhang
 *
 */
public class DogScreens {

	//"register" screens here (make sure to add it in the enum Type)
	private final StageScreen startScreen;
	private final StageScreen mainSurvivalScreen;
	private final StageScreen gameOverScreen;

	//don't instantiate this class outside of DogRunner
	protected DogScreens(Batch batch) {
		
		startScreen = new StartScreen(batch);
		mainSurvivalScreen = new MainSurvivalScreen(batch);
		gameOverScreen = new GameOverScreen(batch);
	}
	
	//dispose screens here
	protected void dispose() {
		
		startScreen.dispose();
		mainSurvivalScreen.dispose();
		gameOverScreen.dispose();
	}
	
	public enum Type {
		
		//"register" the screen again here (and "register" it in the switch statement)
		START_SCREEN,
		MAIN_SURVIVE_SCREEN,
		GAME_OVER_SCREEN;
		
		public StageScreen getStageScreen(DogScreens dogScreens) {
			
			StageScreen stageScreen = null;
			
			//"register" here
			switch (this	) {
			
			case START_SCREEN:
				
				stageScreen = dogScreens.startScreen;
				break;
			case MAIN_SURVIVE_SCREEN:
				
				stageScreen = dogScreens.mainSurvivalScreen;
				break;
			case GAME_OVER_SCREEN:
				
				stageScreen = dogScreens.gameOverScreen;
				break;
			default:
				
				stageScreen = null;
				break;
			}
			
			return stageScreen;
		}
	}
}
