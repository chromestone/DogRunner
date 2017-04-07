package org.qohs.dogrunner.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author Derek Zhang
 *
 */
public class CharsetFileManager implements FileManager {
	
	public String charset = null;

	@Override
	public void load() {
		
		FileHandle file = Gdx.files.local(FileManager.PARENT_DIR + "charset.txt");
		charset = file.readString();
	}

	@Override
	public void save() {
	}
}