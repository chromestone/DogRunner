package org.qohs.dogrunner.io;

/**
 * Currently matches file name to a enum name
 * this is to ensure that it is harder to screw up file names
 * when retrieving from "Fairies.AssetManager" <- not actual code
 * 
 * @author Derek Zhang
 *
 */
public enum DogAssets {
	
	COMIC_SANS40("placeholder/comic_sans40.ttf"),
	COMIC_SANS5("placeholder/comic_sans5.ttf"),
	COMIC_SANS_GREEN("placeholder/comic_sans_green.ttf"),
	DALMATIAN("placeholder/dalmatian.png"),
	FERRARI_CAR("placeholder/ferrari_car.png"),
	RED_CAR("placeholder/red_car.png"),
	ENGINE_REV("placeholder/vroom.mp3"),
	CAR_CRASH_BONG("placeholder/bong.mp3"),
	GAME_OVER_EXPLOSION("placeholder/explosion.jpg");

	public final String fileName;
	
	private DogAssets(String fileName) {
		
		this.fileName = "assets/" + fileName;
	}
}
