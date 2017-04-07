package org.qohs.dogrunner.gameobjects.clicker;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Derek Zhang
 *
 */
public class Donut extends TextureRegionGameObject {

	public Donut(float x, float y, float width, float height, TextureRegion tRegion) {
		
		super(x, y, width, height, tRegion);
	}

	@Override
	public void clicked() {
		
		dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
	}
}
