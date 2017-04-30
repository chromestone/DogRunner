package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class MultiplierSpawner extends Spawner {
	
	private static final int PRIORITY = 80;
	
	private final Drawable textureDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	private int waveCount;
	
	MultiplierSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion textureRegion;
		textureRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.PAW.FILE_NAME, Texture.class));
		textureRegion.flip(false, true);
		TextureRegionDrawable tRegionDraw = new TextureRegionDrawable(textureRegion);
		textureDrawable = tRegionDraw.tint(Color.RED);
		
		bodydef = new BodyDef();
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * textureRegion.getRegionWidth() / textureRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		waveCount = 0;
	}

	@Override
	protected void editSpawnList() {
		
		super.clear();
		
		if (dogRunner.userProfile.invincible == 0) {

			waveCount += 1;
			
			if (waveCount > 19) {//19
				
				DataPriority data = super.get((int) (Math.random() * SpawnManager.ROWS));
				data.priority = PRIORITY;
				data.data = new SpawnerBodyData(this);
				waveCount = 0;
			}
			else if (Math.random() * 30 < 1) {

				DataPriority data = super.get((int) (Math.random() * SpawnManager.ROWS));
				data.priority = PRIORITY;
				data.data = new SpawnerBodyData(this);
			}
		}
		else {
			
			waveCount = 0;
		}
	}
	
	@Override
	void onCrash(SpawnerBodyData data) {
		
		if (data.crashed) {
			
			return;
		}
		
		data.crashed = true;
		
		//20 seconds of invisibility
		Multiplier.scheduleMultiplier(20);
		
		data.destroy = true;
	}

	@Override
	Drawable getDrawable(SpawnerBodyData data) {

		return textureDrawable;
	}

	@Override
	float getWidth(SpawnerBodyData data) {
		
		return width;
	}

	@Override
	float getHeight(SpawnerBodyData data) {
		
		return height;
	}

	@Override
	BodyDef getBodyDef() {

		return bodydef;
	}

	@Override
	Shape getShape() {

		return shape;
	}

	@Override
	protected void mDispose() {

		shape.dispose();
	}
}