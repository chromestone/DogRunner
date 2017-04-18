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
	RED_M("RED_M", 50),//40
	RED_S("RED_S", 25),//30
	GOLD_L("GOLD_L", 110),//100
	YELLOW_L("YELLOW_L", 100),//90
	YELLOW_M("YELLOW_M", 55),
	//WHITE_L("WHITE_L", 70),//50
	WHITE_M("WHITE_M", 40),
	WHITE_S("WHITE_S", 25);
	//ARIAL_RED_XS("");

	public static final String FONT_NAME = "new_roman";
	public static final String SUFFIX = ".ttf";
	public static final String ACTUAL_FONT_FILE_NAME = FONT_NAME + SUFFIX;
	
	public static final int MINIMUM_SIZE = 12;
	
	public final String FILE_NAME;
	private int size;
	
	DogFont(String fileName, int size) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + FONT_NAME + "_" + fileName + SUFFIX;
		this.size = size;
	}
	
	public int getSize() {
		
		return size;
	}
	
	/**
	 * Don't use this unless you are Dog Runner
	 * @param width
	 * @param testWidth
	 */
	public void scale(int width, int testWidth) {
		
		int newSize = (int) (( (double) width * size ) / testWidth);
		size = Math.max(MINIMUM_SIZE, newSize);
	}
	
	/**
	 * Don't use this unless you are Font Select Screen
	 * @param maxSize
	 */
	public static void cap(int maxSize) {
		
		for (DogFont dogFont : values()) {
			
			dogFont.size = Math.min(maxSize, dogFont.size);
		}
	}
}
