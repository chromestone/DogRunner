package org.qohs.dogrunner.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This will keep track of whether the GameObject has been clicked since last check
 * and also draws a texture region
 * 
 * @author Derek Zhang
 *
 */
public class QueryButton extends TextureRegionGameObject {
	
	private boolean clicked;

	public QueryButton(float x, float y, float width, float height, TextureRegion tRegion) {
		
		super(x, y, width, height, tRegion);
		
		clicked = false;	
	}

	@Override
	public void clicked() {
		
		clicked = true;
	}

	/**
	 * 
	 * @return whether or not if this object has been clicked since last query
	 */
	public boolean queryClicked() {
		
		boolean temp = clicked;
		clicked = false;
		return temp;
	}
}
