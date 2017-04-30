package org.qohs.dogrunner.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author Derek Zhang
 *
 */
public class FontSelectFileManager implements FileManager {

	public boolean validatedSize = false;
	public int fontSizeMax = -1;
	
	@Override
	public void load() {
		

		FileHandle readFile = Gdx.files.local(FileManager.PARENT_DIR + "font_size_max.txt");
		if (!readFile.exists()) {
			
			defaultMax();
			return;
		}
				
		try {
			
			fontSizeMax = Integer.parseInt(readFile.readString());
			validatedSize = true;
		}
		catch (NumberFormatException e) {
			
			Gdx.app.log("DogRunner-chromestone-FSFM", e.getMessage());
			defaultMax();
		}
	}
	
	private void defaultMax() {
		
		DogFont[] dogFonts = DogFont.values();
		fontSizeMax = dogFonts[0].getSize();
		for (int i = 1; i < dogFonts.length; i++) {
			
			if (dogFonts[i].getSize() > fontSizeMax) {
				
				fontSizeMax = dogFonts[i].getSize();
			}
		}
		
		validatedSize = false;
	}
	
	@Override
	public void save() {
		
		if (!validatedSize) {
			
			return;
		}
		FileHandle readFile = Gdx.files.local(FileManager.PARENT_DIR + "font_size_max.txt");
		readFile.writeString("" + fontSizeMax, false);
	}
}
