package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.utils.Timer.Task;

/**
 * 
 * @author Derek Zhang
 *
 */
public final class Invincibility {

	private Invincibility() {}

	public static void scheduleInvinciblity(int delaySeconds) {
		
		scheduleInvinciblity(delaySeconds, 0);
	}
	
	/**
	 * Makes player invincible for time specified
	 * 
	 * @param timer the timer to schedule the task on (Gdx's timer)
	 * @param delaySeconds time to make player invincible for
	 * @param warnSeconds when to set {@code userProfile.invincibile = 1}
	 */
	public static void scheduleInvinciblity(int delaySeconds, int warnSeconds) {
		
		DogRunner dogRunner = DogRunner.getInstance();
		dogRunner.userProfile.invincible = 2;
		if (warnSeconds > 0) {
			
			dogRunner.timer.scheduleTask(new MyTask((byte) 1), delaySeconds - warnSeconds);
		}
		dogRunner.timer.scheduleTask(new MyTask((byte) 0), delaySeconds);
	}
	
	private static class MyTask extends Task {

		private final byte invincibility;
		
		public MyTask(byte i) {
			
			invincibility = i;
		}
		
		@Override
		public void run() {
			
			DogRunner.getInstance().userProfile.invincible = invincibility;
		}
	}
}
