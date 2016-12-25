package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * All the texture
 * 
 * @author Derek Zhang
 *
 */
public enum DogTexture {

	DOG(	"placeholder/dog.png"),
	PORSCHE_CAR("placeholder/porsche_car.png"),
	WHITE_CAR("placeholder/white_car.png"),
	PAUSE_IMG("placeholder/pause.png"),
	RESUME_IMG("placeholder/resume.png"),
	TRIANGLE_GRAY_IMG("placeholder/triangle_gray.png"),
	TRIANGLE_GOLD_IMG("placeholder/triangle_gold.png"),
	ROAD_IMG("placeholder/road.png"),
	EXPLODE("placeholder/car_explosion.png"),
	HEALTH("placeholder/health.png"),
	BLANK("texture/white.png"),
	GAME_OVER_EXPLOSION("placeholder/explosion.jpg");

	public final String FILE_NAME;
	
	DogTexture(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
