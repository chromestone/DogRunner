package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * 
 * @author Derek Zhang
 *
 */
public class HealthIndicator extends GameObject {
	
	private final static Color m_color = new Color(1f, 1f, 1f, .6f);
	
	private final TextureRegion health;

	public HealthIndicator(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		health = new TextureRegion(dogRunner.assetManager.get(DogTexture.BONE.FILE_NAME, Texture.class));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		Color color = batch.getColor();
		batch.setColor(m_color);
		float m_x = 0f;
		for (byte i = 0; i < dogRunner.userProfile.lives; i++) {
			
			batch.draw(health, m_x, y, width, height);
			m_x += width;
		}
		batch.setColor(color);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {

		return null;
	}
}
