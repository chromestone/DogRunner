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
	
	//S-Small; M-Medium; L-Large; XL-Extra Large; XXL-Extra Extra Large; etc.
	COMIC_SANS_RED_M("comic_sans_red_40.ttf"),
	COMIC_SANS_RED_S("comic_sans_red_20.ttf"),
	COMIC_SANS_GOLD_L("comic_sans_gold_100.ttf"),
	COMIC_SANS_BLACK_L("comic_sans_black_90.ttf"),
	DOG_IMG("placeholder/dog.png"),
	PORSCHE_CAR("placeholder/porsche_car.png"),
	WHITE_CAR("placeholder/white_car.png"),
	ENGINE_REV("placeholder/vroom.mp3"),
	CAR_CRASH_BONG("placeholder/bong.mp3"),
	GAME_OVER_EXPLOSION("placeholder/explosion.jpg"),
	PAUSE_IMG("placeholder/pause.png"),
	RESUME_IMG("placeholder/resume.png"),
	TRIANGLE_GRAY_IMG("placeholder/triangle_gray.png"),
	TRIANGLE_GOLD_IMG("placeholder/triangle_gold.png");

	public final String fileName;
	
	DogAssets(String fileName) {
		
		this.fileName = "assets/" + fileName;
	}
}
