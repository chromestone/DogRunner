package org.qohs.dogrunner.io;

/**
 * File Manager interface
 * 
 * @author Derek Zhang
 *
 */
public interface FileManager {

	/**
	 * USED FOR Gdx.files.local
	 * WEIRD STUFF HAPPENING, ALL THESE THINGS POINT TO DIFFERENT DIRECTORIES ON ANDROID
	 */
	static final String PARENT_DIR = "assets/";

	void load();
	
	void save();
}
