package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * 
 * @author Derek Zhang
 *
 */
public enum DogTextureAtlas {
	
	ATLAS_1("texture/dog_textures1.atlas");
	//UI_SKIN("uiskin/uiskin.atlas");
	
	//private static final String DIRECTORY = DogRunner.PARENT_DIR + "texture/";

	public final String FILE_NAME;
	
	DogTextureAtlas(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
