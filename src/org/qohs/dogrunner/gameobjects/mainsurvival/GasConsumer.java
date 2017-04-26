package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;

public class GasConsumer {

	private final DogRunner dogRunner;
	private float accumulator;
	
	public GasConsumer() {

		dogRunner = DogRunner.getInstance();
		accumulator = 0f;
	}

	public void act(float delta) {
		
		accumulator += delta;
		int seconds = (int) accumulator;
		if (seconds > 0) {
			
			dogRunner.userProfile.gas -= seconds;
			accumulator -= seconds;
		}
	}
}
