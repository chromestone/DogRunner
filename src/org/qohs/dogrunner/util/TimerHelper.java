package org.qohs.dogrunner.util;

import com.badlogic.gdx.utils.TimeUtils;//used for compatibility between different platforms
import com.badlogic.gdx.utils.Timer;

/**
 * Helps actually pause the GDX timer
 * 
 * @author Derek Zhang
 *
 */
public class TimerHelper {

	private final Timer timer;
	private long pauseTime;
	private boolean paused;
	
	public TimerHelper(Timer timer) {
		
		this.timer = timer;
		pauseTime = 0L;
		paused = false;
	}

	/**
	 * stops the timer
	 * and also resets the pause time
	 */
	public void pause() {
		
		timer.stop();
		pauseTime = TimeUtils.nanoTime();
		paused = true;
	}
	
	/**
	 * if pause has been called
	 * then timer is delayed to account for pausing
	 * and the timer's start method will be called
	 * otherwise timer's start is just called
	 */
	public void resume() {
		
		if (!paused) {
			
			timer.start();
			return;
		}
		timer.delay(TimeUtils.nanosToMillis(TimeUtils.nanoTime() - pauseTime));
		timer.start();
		paused = false;
	}
}
