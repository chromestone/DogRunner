package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.TextureRegionGameObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GasIndicator extends TextureRegionGameObject {

	public GasIndicator(float x, float y, float width, float height,
			TextureRegion tRegion) {
		
		super(x, y, width, height, tRegion);
	}

	@Override
	public void clicked() {

		if (dogRunner.userProfile.score == 114) {
			
			dogRunner.userProfile.spin = true;
			dogRunner.userProfile.multiplier = 10;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = batch.getColor();
		batch.setColor(getColor());
		super.draw(batch, parentAlpha);
		batch.setColor(color);
	}
}
