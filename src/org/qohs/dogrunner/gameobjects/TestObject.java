package org.qohs.dogrunner.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * A primitive example class
 * displays a blue rectangle
 * 
 * @author Derek Zhang
 *
 */
public class TestObject extends GameObject {
	
	private Color color;

	public TestObject(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		color = Color.BLUE;
	}

	
	@Override
	public void act(float delta) {
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.end();
		
		dogRunner.renderer.begin(ShapeType.Filled);
		dogRunner.renderer.setColor(color);
		dogRunner.renderer.rect(x, y, width, height);
		dogRunner.renderer.end();
		
		batch.begin();
	}
	
	public void animateDown() {
		
		color = Color.GREEN;
	}
	
	public void animateUp() {
		
		color = Color.BLUE;
	}
	
	public void clicked() {
		
		System.out.println("clicked!");
	}
}
