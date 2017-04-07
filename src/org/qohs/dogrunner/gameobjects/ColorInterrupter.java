package org.qohs.dogrunner.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ColorInterrupter extends GameObject {

	private final Color color;
	
	public ColorInterrupter(Color color) {
		
		super(0f, 0f, 0f, 0f);
		
		this.color = color.cpy();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.setColor(color);
	}
}
