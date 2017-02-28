package org.qohs.dogrunner.io;

import java.util.*;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Does not do blocking IO
 * Requires assets to be loaded!
 * 
 * @author Derek Zhang
 * @author Gudrun T.
 *
 */
public class DonutThingManager implements FileManager {

	private final DogRunner dogRunner;
	
	public List<TextureRegion> donutThings;
	
	public DonutThingManager() {

		dogRunner = DogRunner.getInstance();
		donutThings = new LinkedList<>();
	}

	@Override
	public void load() {
		
		FileHandle file = Gdx.files.local(FileManager.PARENT_DIR + "types.txt");
		String mapdata = file.readString();
		mapdata = mapdata.replaceAll("\r", "");
		String[] mappings = mapdata.split("\n");
		
		Texture type_texture = dogRunner.assetManager.get(DogTexture.DONUT_TYPES.FILE_NAME, Texture.class);
		
		for (String str : mappings) {
			str = str.replaceAll(" =", "");
			String[] coords = str.split(" ");
			TextureRegion temp = new TextureRegion(type_texture, 
					Integer.valueOf(coords[1])*250/4096,
					Integer.valueOf(coords[2])*250/4096,
					Integer.valueOf(coords[3])*250/4096,
					Integer.valueOf(coords[4])*250/4096);
			temp.flip(false, true);
			donutThings.add(temp);
		}
	}

	@Override
	public void save() {
	}

}
