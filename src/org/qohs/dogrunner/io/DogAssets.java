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
	DOG_IMG("placeholder/dog.png"),
	PORSCHE_CAR("placeholder/porsche_car.png"),
	WHITE_CAR("placeholder/white_car.png"),
	ENGINE_REV("placeholder/vroom.mp3"),
	CAR_CRASH_BONG("placeholder/bong.mp3"),
	GAME_OVER_EXPLOSION("placeholder/explosion.jpg"),
	PAUSE_IMG("placeholder/pause.png"),
	RESUME_IMG("placeholder/resume.png");

	public final String fileName;
	
	DogAssets(String fileName) {
		
		this.fileName = "assets/" + fileName;
	}
}
