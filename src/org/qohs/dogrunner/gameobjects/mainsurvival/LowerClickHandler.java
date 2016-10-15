package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Handles clicks for a specific section of screen
 * note that this does nothing and is meant to be queried
 * by it's "parent"/using screen [this.isPressed()]
 * 
 * The "Upper" in the class name indicates the direction of the
 * triangles that will be drawn
 * 
 * @author Derek Zhang
 *
 */
public class LowerClickHandler extends GameObject {

	public boolean activated;
	
	private TextureRegion greyTriangle;
	private TextureRegion goldTriangle;
	//triangle is encapsulated in a square picture
	//this is how long one side of such square is
	private float triangleLength;
	
	private float triangleY;

	public LowerClickHandler(float x, float y, float width, float height, float triangleLength) {
		
		super(x, y, width, height);
		
		activated = false;
		
		greyTriangle = new TextureRegion(dogRunner.assetManager.get(DogAssets.TRIANGLE_GRAY_IMG.fileName, Texture.class));
		goldTriangle = new TextureRegion(dogRunner.assetManager.get(DogAssets.TRIANGLE_GOLD_IMG.fileName, Texture.class));
		this.triangleLength = triangleLength;
		
		triangleY = y + height - triangleLength;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		Color color = batch.getColor();
		batch.setColor(1f, 1f, 1f, .5f);
		
		if (activated) {
			
			for (float i = x; i <= x + width - triangleLength; i += triangleLength * 2f) {

				batch.draw(goldTriangle, i, triangleY, triangleLength, triangleLength);
			}
		}
		else {
			
			for (float i = x; i <= x + width - triangleLength; i += triangleLength * 2f) {

				batch.draw(greyTriangle, i, triangleY, triangleLength, triangleLength);
			}
		}
		
		batch.setColor(color);
	}
}
