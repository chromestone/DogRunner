package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.graphics.*;
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
public class UpperClickHandler extends GameObject {
	
	/**
	 * When this is set true, then the triangle drawn will be gold
	 */
	public boolean activated;
	
	private TextureRegion greyTriangle;
	private TextureRegion goldTriangle;
	//triangle is encapsulated in a square picture
	//this is how long one side of such square is
	private float triangleLength;
	private float padding;

	public UpperClickHandler(float x, float y, float width, float height, float triangleLength) {
		
		super(x, y, width, height);
		
		activated = false;
		
		greyTriangle = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.TRIANGLE_GRAY_IMG));
		greyTriangle.flip(false, true);
		goldTriangle = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.TRIANGLE_GOLD_IMG));
		goldTriangle.flip(false, true);
		this.triangleLength = triangleLength;
		padding = width % triangleLength / 2f;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		Color color = batch.getColor();
		batch.setColor(1f, 1f, 1f, .5f);
		
		if (activated) {
			
			for (float i = x + padding; i <= x + width - triangleLength; i += triangleLength * 2f) {

				batch.draw(goldTriangle, i, y, triangleLength, triangleLength);
			}
		}
		else {
			
			for (float i = x + padding; i <= x + width - triangleLength; i += triangleLength * 2f) {

				batch.draw(greyTriangle, i, y, triangleLength, triangleLength);
			}
		}
		
		batch.setColor(color);
	}
}
