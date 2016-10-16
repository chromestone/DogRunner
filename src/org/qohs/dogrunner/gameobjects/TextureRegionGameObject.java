package org.qohs.dogrunner.gameobjects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The fancy long name of this class really just means:
 * a game object that displays a sprite/picture and detects clicks (if needed)
 * 
 * @author Derek Zhang
 *
 */
public class TextureRegionGameObject extends GameObject {
	
	private TextureRegion tRegion;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param tRegion
	 */
	public TextureRegionGameObject(float x, float y, float width, float height, TextureRegion tRegion) {
		
		super(x, y, width, height);
		
		if (tRegion == null) {
			
			throw new IllegalArgumentException("TextureRegion cannot be null.");
		}
		
		this.tRegion = tRegion;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.draw(tRegion, x, y, width, height);
	}
}
