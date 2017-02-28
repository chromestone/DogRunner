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
	 * left until game over. (i.e. 30 seconds left, gas would be 1 still)
	 */
	public int gas;
	public boolean spin = false;
	public int gasStops;
	public int previousGasScore;
	/**
	 * 0 for NOT invincible, 1 for ALMOST not invincible, 2 for invincible
	 */
	public byte invincible;

	//don't instantiate this class outside of DogRunner
	protected UserProfile() {
		
		reset();
	}
	
	public void reset() {
		
		score = 0;
		lives = 3;
		gas = 10;
		gasStops = 0;
		previousGasScore = 100;
		invincible = 0;
	}
}
