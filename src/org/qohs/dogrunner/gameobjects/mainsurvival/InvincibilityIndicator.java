package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Derek Zhang
 *
 */
public class InvincibilityIndicator extends GameObject {

	private TextureRegion ghost;

	private float centerX;
	
	private boolean flashDraw;
	private float accumulator;
	
	private Color alpha;
	
	public InvincibilityIndicator(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		ghost = new TextureRegion(dogRunner.assetManager.get(DogTexture.GHOST.FILE_NAME, Texture.class));
		ghost.flip(false, true);
		
		super.width = super.getHeight() * ghost.getRegionWidth() / ghost.getRegionHeight();
		centerX = (width - super.width) / 2f;
		
		flashDraw = false;
		accumulator = 0f;
		
		alpha = Color.WHITE.cpy();
		alpha.a = .1f;
	}
	
	@Override
	public void act(float delta) {
		
		if (dogRunner.userProfile.invincible != 1) {
			
			return;
		}
		
		accumulator += delta;
		if (accumulator > .25f) {
			
			while (accumulator > .25f) {
				
				flashDraw = !flashDraw;
				accumulator -= .25f;
			}
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (dogRunner.userProfile.invincible > 0) {

			if (dogRunner.userProfile.invincible > 1 || flashDraw) {

				Color color = batch.getColor();
				batch.setColor(alpha);
				batch.draw(ghost, centerX, y, width, height);
				batch.setColor(color);
			}
		}
	}
}
