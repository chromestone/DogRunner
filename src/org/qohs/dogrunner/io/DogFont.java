package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * Currently matches file name to a enum name
 * this is to ensure that it is harder to screw up file names
 * when retrieving from "DogRunner.AssetManager" <- not actual code
 * 
 * NOTE: ONLY TRUE TYPE FONTS USED :)
 * 
 * @author Derek Zhang
 *
 */
public enum DogFont {
	
	//S-Small; M-Medium; L-Large; XL-Extra Large; XXL-Extra Extra Large; etc.
	ARIAL_RED_M("red_40"),
	ARIAL_RED_S("red_20"),
	ARIAL_GOLD_L("gold_100"),
	ARIAL_YELLOW_L("black_90"),
	ARIAL_YELLOW_M("black_45"),
	ARIAL_WHITE_M("white_40"),
	ARIAL_WHITE_S("white_20");
	//ARIAL_RED_XS("");

	public static final String FONT_NAME = "new_roman";
	public static final String SUFFIX = ".ttf";
	public static final String ACTUAL_FONT_FILE_NAME = FONT_NAME + SUFFIX;
	
	public final String FILE_NAME;
	
	DogFont(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + FONT_NAME + "_" + fileName + SUFFIX;
	}
}
