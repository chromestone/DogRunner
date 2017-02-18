package org.qohs.dogrunner.io;

/**
 * 
 * @author Derek Zhang
 *
 */
public enum DogAtlasRegion {

	PORSCHE_CAR("porsche_car", DogTextureAtlas.ATLAS_1),
	WHITE_CAR("white_car", DogTextureAtlas.ATLAS_1),
	PAUSE_IMG("pause", DogTextureAtlas.ATLAS_1),
	RESUME_IMG("resume", DogTextureAtlas.ATLAS_1),
	TRIANGLE_GRAY_IMG("triangle_gray", DogTextureAtlas.ATLAS_1),
	TRIANGLE_GOLD_IMG("triangle_gold", DogTextureAtlas.ATLAS_1),
	EXPLODE("car_explosion", DogTextureAtlas.ATLAS_1),
	BLANK("white", DogTextureAtlas.ATLAS_1),
	GAME_OVER_EXPLOSION("explosion", DogTextureAtlas.ATLAS_1);
	
	public final String NAME;
	public final DogTextureAtlas ENCLOSING_ATLAS;

	DogAtlasRegion(String name, DogTextureAtlas enclosingAtlas) {
		
		this.NAME = name;
		this.ENCLOSING_ATLAS = enclosingAtlas;
	}
}
