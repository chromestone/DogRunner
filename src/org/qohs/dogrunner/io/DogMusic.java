package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * All the music (long sounds)
 * 
 * @author Derek Zhang
 *
 */
public enum DogMusic {

	BACKGROUND_THEME("sound/backtheme.wav");
	
	public final String FILE_NAME;
	
	DogMusic(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
