package org.qohs.dogrunner.io;

import org.qohs.dogrunner.DogRunner;

/**
 * Currently matches file name to a enum name
 * this is to ensure that it is harder to screw up file names
 * when retrieving from "Fairies.AssetManager" <- not actual code
 * 
 * @author Derek Zhang
 *
 */
public enum DogAsset {
	
	//It's not arial but whatever
	//S-Small; M-Medium; L-Large; XL-Extra Large; XXL-Extra Extra Large; etc.
	ARIAL_RED_M("arial_red_40.ttf"),
	ARIAL_RED_S("arial_red_20.ttf"),
	ARIAL_GOLD_L("arial_gold_100.ttf"),
	ARIAL_YELLOW_L("arial_black_90.ttf"),
	ARIAL_YELLOW_M("arial_black_45.ttf"),
	ARIAL_WHITE_M("arial_white_40.ttf"),
	ARIAL_WHITE_S("arial_white_20.ttf");
	//ARIAL_RED_XS("");

	public final String FILE_NAME;
	
	DogAsset(String fileName) {
		
		this.FILE_NAME = DogRunner.PARENT_DIR + fileName;
	}
}
