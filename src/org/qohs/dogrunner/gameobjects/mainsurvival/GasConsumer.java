package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;

/**
 * 
 * @author Derek Zhang
 *
 */
class GasConsumer {

	private final DogRunner dogRunner;
	private float accumulator;
	
	GasConsumer() {

		dogRunner = DogRunner.getInstance();
		accumulator = 0f;
	}

	public void act(float delta) {
		
		if (dogRunner.userProfile.gas <= 0) {
			
			return;
		}
		
		accumulator += delta;
		int seconds = (int) accumulator;
		if (seconds > 0) {
			
			dogRunner.userProfile.gas -= seconds;
			accumulator -= seconds;
		}
	}
}
