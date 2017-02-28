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
	private final StageScreen highScoreScreen;
	private final StageScreen inputHighScoreScreen;
	private final StageScreen storylineScreen;
	private final StageScreen clickerScreen;

	//don't instantiate this class outside of DogRunner
	protected DogScreens(Batch batch) {
		
		startScreen = new StartScreen();
		mainSurvivalScreen = new MainSurvivalScreen();
		highScoreScreen = new HighScoreScreen();
		inputHighScoreScreen = new InputHighScoreScreen();
		storylineScreen = new StorylineScreen();
		clickerScreen = new ClickerScreen();
	}
	
	//dispose screens here
	protected void dispose() {
		
		startScreen.dispose();
		mainSurvivalScreen.dispose();
		highScoreScreen.dispose();
		inputHighScoreScreen.dispose();
		storylineScreen.dispose();
		clickerScreen.dispose();
	}
	
	public enum Type {
		
		//"register" the screen again here (and "register" it in the switch statement)
		START_SCREEN,
		MAIN_SURVIVE_SCREEN,
		HIGH_SCORE_SCREEN,
		INPUT_HIGH_SCORE_SCREEN,
		STORYLINE_SCREEN,
		CLICKER_SCREEN;		
		/**
		 * 
		 * @param dogScreens
		 * @return
		 */
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
			case HIGH_SCORE_SCREEN:
				
				stageScreen = dogScreens.highScoreScreen;
				break;
			case INPUT_HIGH_SCORE_SCREEN:
				
				stageScreen = dogScreens.inputHighScoreScreen;
				break;
			case STORYLINE_SCREEN:
				
				stageScreen = dogScreens.storylineScreen;
				break;
			case CLICKER_SCREEN:
				
				stageScreen = dogScreens.clickerScreen;
				break;
			default:
				
				stageScreen = null;
				break;
			}
			
			return stageScreen;
		}
	}
}
