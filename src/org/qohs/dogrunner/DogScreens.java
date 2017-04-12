package org.qohs.dogrunner;

import org.qohs.dogrunner.screens.*;

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
	//private final StageScreen fontSelectScreen;

	//don't instantiate this class outside of DogRunner
	protected DogScreens() {
		
		startScreen = new StartScreen();
		mainSurvivalScreen = new MainSurvivalScreen();
		highScoreScreen = new HighScoreScreen();
		inputHighScoreScreen = new InputHighScoreScreen();
		storylineScreen = new StorylineScreen();
		clickerScreen = new ClickerScreen();
		//fontSelectScreen = new FontSelectScreen();
	}
	
	//dispose screens here
	protected void dispose() {
		
		startScreen.dispose();
		mainSurvivalScreen.dispose();
		highScoreScreen.dispose();
		inputHighScoreScreen.dispose();
		storylineScreen.dispose();
		clickerScreen.dispose();
		//fontSelectScreen.dispose();
	}
	
	public enum Type {
		
		//"register" the screen again here (and "register" it in the switch statement)
		START_SCREEN,
		MAIN_SURVIVE_SCREEN,
		HIGH_SCORE_SCREEN,
		INPUT_HIGH_SCORE_SCREEN,
		STORYLINE_SCREEN,
		CLICKER_SCREEN;
		//FONT_SELECT_SCREEN;		

		/**
		 * 
		 * @param dogScreens the dog screen instance from which to get the screen
		 * @return the corresponding stage screen
		 */
		protected StageScreen getStageScreen(DogScreens dogScreens) {
			
			StageScreen stageScreen;
			
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
			/*case FONT_SELECT_SCREEN:
				
				stageScreen = dogScreens.fontSelectScreen;
				break;*/
			default:
				
				stageScreen = null;
				break;
			}
			
			return stageScreen;
		}
	}
}
