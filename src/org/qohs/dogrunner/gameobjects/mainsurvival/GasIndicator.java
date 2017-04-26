package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.TextureRegionGameObject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GasIndicator extends TextureRegionGameObject {

	public GasIndicator(float x, float y, float width, float height,
			TextureRegion tRegion) {
		
		super(x, y, width, height, tRegion);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color color = batch.getColor();
		batch.setColor(getColor());
		super.draw(batch, parentAlpha);
		batch.setColor(color);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		
		Actor actor = super.hit(x, y, touchable);
		if (actor != null && dogRunner.userProfile.score == 522) {
			
			return actor;
		}
		return null;
	}

	@Override
	public void clicked() {

		dogRunner.userProfile.spin = true;
		dogRunner.userProfile.multiplier = 10;
	}
}
