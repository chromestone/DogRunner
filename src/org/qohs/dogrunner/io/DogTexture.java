package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * All the texture
 * 
 * @author Derek Zhang
 *
 */
public enum DogTexture {

	DOG("placeholder/dog.png"),
	ROAD_IMG("texture/road.png");

	public final String FILE_NAME;

	DogTexture(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
