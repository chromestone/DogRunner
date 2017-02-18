package org.qohs.dogrunner;

/**
 * Attributes that pertain to the player
 * Useful for "sending" info across screens
 * Essentially the "state" of the game
 * 
 * @author Derek Zhang
 *
 */
public final class UserProfile {
	
	public int score;
	public int lives;
	/**
	 * Also equivalent to the ceiling of how many minutes
	 * left until game over. (i.e. 30 minutes displays at
	 */
	public int gas;
	public boolean spin = true;

	//don't instantiate this class outside of DogRunner
	protected UserProfile() {
		
		reset();
	}
	
	public void reset() {
		
		score = 0;
		lives = 3;
		gas = 10;
	}
}
