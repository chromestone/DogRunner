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
	 * On Android, local is a whole private directory that starts at "/" and is empty
	 * for organization, assets is used.
	 * It also makes it easier on Desktop tests as local and internal are the same
	 * so both start at the root of the project and the assets are in the asset folder
	 */
	String PARENT_DIR = "assets/";

	void load();
	
	void save();
}
