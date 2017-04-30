package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * All the texture
 * 
 * @author Derek Zhang
 *
 */
public enum DogTexture {

	ROAD_IMG("texture/road.png"),
	GAS_STATION("texture/gas_station_car.png"),
	START_BACKGROUND("texture/start_background.png"),
	DONUT_TYPES("texture/types.png"),
	PROHIBITED("texture/prohibited.png"),
	UNICORN("texture/unicorn.png"),
	GAS_INDICATOR("texture/gas_indicator.png"),
	BUTTERFLY("texture/butterfly.png"),
	BONE("texture/bone.png"),
	PAW("texture/paw.png"),
	REVERSE("texture/reverse.png");

	public final String FILE_NAME;

	DogTexture(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
