package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;

/**
 * Handles clicks for a specific section of screen
 * note that this does nothing and is meant to be queried
 * by it's "parent"/using screen [this.isPressed()]
 * 
 * @author Derek Zhang
 *
 */
public class ClickHandler extends GameObject {
	

	public ClickHandler(int x, int y, int width, int height) {
		
		super(x, y, width, height);
	}
}
