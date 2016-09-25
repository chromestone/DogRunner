package org.qohs.dogrunner;

/**
 * Attributes that pertain to the player
 * Useful for "sending" info across screens
 * Essentially the "state" of the game
 * 
 * @author Derek Zhang
 *
 */
public class UserProfile {
	
	public int score;

	//don't instantiate this class outside of Fairies
	protected UserProfile() {
		
		score = 0;
	}
}
