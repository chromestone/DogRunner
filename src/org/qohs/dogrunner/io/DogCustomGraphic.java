package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * Skins but I didn't want the class name to be weird
 * 
 * @author Derek Zhang
 *
 */
public enum DogCustomGraphic {

	UI_SKIN("uiskin/uiskin.json");
	
	public final String FILE_NAME;
	
	DogCustomGraphic (String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
