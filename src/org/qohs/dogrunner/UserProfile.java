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
	
	public long score;
	public byte lives;
	/**
	 * Also equivalent to how many seconds
	 * left until game over.
	 */
	public long gas;
	public boolean spin;
	
	public int gasStops;
	public int previousWave;
	public boolean storylineTime;
	
	/**
	 * 0 for NOT invincible, 1 for ALMOST not invincible, 2 for invincible
	 */
	public byte invincible;
	
	public byte multiplier;
	
	public boolean inGame;
	
	public boolean invertControl;

	//don't instantiate this class outside of DogRunner
	UserProfile() {
		
		reset();
	}
	
	public void reset() {
		
		score = 0;
		lives = 3;
		spin = false;
		gas = 5 * 60;
		gasStops = 0;
		previousWave = 0;
		storylineTime = false;
		invincible = 0;
		multiplier = 1;
		inGame = false;
		invertControl = false;
	}
}
