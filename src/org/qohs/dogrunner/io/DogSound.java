package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * All the sound (short sounds)
 * 
 * @author Derek Zhang
 *
 */
public enum DogSound {

	IGNITION_REV("sound/ignition_rev.mp3"),
	CAR_CRASH_BONG("sound/bong.mp3"),
	CRASH_DEATH("sound/crash_death.mp3");
	
	public final String FILE_NAME;
	
	DogSound(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
