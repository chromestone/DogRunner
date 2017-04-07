package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * All the texture
 * 
 * @author Derek Zhang
 *
 */
public enum DogTexture {

	//DOG("placeholder/dog.png"),
	ROAD_IMG("texture/road.png"),
	GAS_STATION("texture/gas_station_car.png"),
	START_BACKGROUND("texture/start_background.png"),
	DONUT_TYPES("texture/types.png"),
	GHOST("texture/ghost.png"),
	PROHIBITED("texture/prohibited.png"),
	CHECK("texture/check.png"),
	UNICORN("texture/unicorn.png");

	public final String FILE_NAME;

	DogTexture(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
