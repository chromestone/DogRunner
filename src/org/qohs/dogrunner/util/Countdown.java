package org.qohs.dogrunner.util;

/**
 * Simple class that tracks time elapsed
 * 
 * @author Derek Zhang
 *
 */
public class Countdown {

	private final float startTime;
	private float time;
	
	/**
	 * The start time is actually how many seconds to count down from
	 * 
	 * @param startTime in seconds
	 */
	public Countdown(float startTime) {
		
		this.startTime = startTime;
		time = startTime;
	}
	
	public float update(float delta) {
		
		return (time -= delta);
	}
	
	public float getStartTime() {
		
		return startTime;
	}
	
	public void reset() {
		
		time = startTime;
	}
}
