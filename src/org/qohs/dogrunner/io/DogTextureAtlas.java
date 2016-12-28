package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

public enum DogTextureAtlas {
	
	ATLAS_1("dog_textures1.atlas");
	
	private static final String DIRECTORY = DogRunner.PARENT_DIR + "texture/";

	public final String FILE_NAME;
	
	DogTextureAtlas(String fileName) {
		
		this.FILE_NAME = DIRECTORY + fileName;
	}
}
