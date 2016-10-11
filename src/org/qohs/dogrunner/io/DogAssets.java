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
	
	COMIC_SANS40("comic_sans40.ttf"),
	COMIC_SANS10("comic_sans10.ttf"),
	COMIC_SANS70("comic_sans70.ttf"),
	COMIC_SANS_CYAN("comic_sans_cyan.ttf"),
	DALMATIAN("placeholder/dalmatian.png"),
	FERRARI_CAR("placeholder/ferrari_car.png"),
	RED_CAR("placeholder/red_car.png"),
	ENGINE_REV("placeholder/vroom.mp3"),
	CAR_CRASH_BONG("placeholder/bong.mp3"),
	GAME_OVER_EXPLOSION("placeholder/explosion.jpg"),
	PAUSE_IMAGE("placeholder/pause.png"),
	RESUME_IMAGE("placeholder/resume.png");

	public final String fileName;
	
	private DogAssets(String fileName) {
		
		this.fileName = "assets/" + fileName;
	}
}
