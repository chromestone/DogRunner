package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.utils.Timer.Task;

public final class Multiplier {

	private Multiplier() {}

	/**
	 * Increments multiplier by one with time sepcified
	 * 
	 * @param delaySeconds time the multiplier effect lasts
	 */
	public static void scheduleMultiplier(int delaySeconds) {
		
		DogRunner dogRunner = DogRunner.getInstance();
		dogRunner.userProfile.multiplier += 1;
		dogRunner.timer.scheduleTask(new MyTask(dogRunner), delaySeconds);
	}
	
	private static class MyTask extends Task {

		final DogRunner dogRunner;
		
		MyTask(DogRunner dR) {
			
			this.dogRunner = dR;
		}
		
		@Override
		public void run() {
			
			if (dogRunner.userProfile.multiplier > 1) {
				
				dogRunner.userProfile.multiplier -= 1;
			}
		}
	}
}
