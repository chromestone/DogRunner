package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 * @author Gudrun T.
 *
 */
class GhostSpawner extends Spawner {
	
	private static final int PRIORITY = 100;
	
	private final TextureRegionDrawable textureDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	private Sound sound;
	
	private int waveCount;
	
	private final SpawnerBodyData theData;
	
	GhostSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion textureRegion;
		textureRegion = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.GHOST));
		textureRegion.flip(false, true);
		textureDrawable = new TextureRegionDrawable(textureRegion);
		
		bodydef = new BodyDef(); 
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * textureRegion.getRegionWidth() / textureRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		sound = dogRunner.assetManager.get(DogSound.GHOST_SOUND.FILE_NAME, Sound.class);
		
		waveCount = 0;
		
		//dogRunner.assetManager.get(DogSound.GHOST_SOUND.FILE_NAME, Sound.class).setVolume(1, 1);
		
		theData = new SpawnerBodyData(this);
	}
	
	private void resetData() {
		
		theData.crashed = false;
		theData.destroy = false;
	}

	@Override
	protected void editSpawnList() {
		
		super.get(1).data = null;
		super.get(4).data = null;
		
		if (dogRunner.userProfile.invincible == 0) {

			waveCount += 1;
			
			if (waveCount > 38) {
				
				DataPriority data = super.get(randomIndex());//(int)(Math.random()*2) + 2);
				data.priority = PRIORITY;
				resetData();
				data.data = theData;//PhysicsBodyType.GHOST, this);
				waveCount = 0;
			}
			else if (Math.random() * 30 < 1) {
				
				DataPriority data = super.get(randomIndex());
				data.priority = PRIORITY;
				resetData();
				data.data = theData;//PhysicsBodyType.GHOST, this);
			}
		}
		else {
			
			waveCount = 0;
		}
	}
	
	private int randomIndex() {
		
		return (Math.random()*2) >= 1 ? 1 : 4;
	}
	
	/*
	@Override
	void act(SpawnerBodyData data) {
		
		/*if (data.crashed) {
			
			//20 seconds of invisibility
			//Invincibility.scheduleInvinciblity(20, 5);
			data.destroy = true;
			sound.play();
			//ghostData.soundPlayed = true;
		}
		else*//*
		if (dogRunner.userProfile.invincible > 0) {
			
			data.destroy = true;
		}
	}*/
	
	@Override
	void onCrash(SpawnerBodyData data) {
		
		if (data.crashed) {
			
			return;
		}
		
		data.crashed = true;
		
		//20 seconds of invisibility
		Invincibility.scheduleInvinciblity(15, 5);
		
		data.destroy = true;
		sound.play();
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
