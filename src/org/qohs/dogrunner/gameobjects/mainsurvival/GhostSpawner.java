package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.io.DogSound;
import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class GhostSpawner extends Spawner {
	
	private final TextureRegionDrawable textureDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	GhostSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion textureRegion;
		textureRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.GHOST.FILE_NAME, Texture.class));
		textureRegion.flip(false, true);
		textureDrawable = new TextureRegionDrawable(textureRegion);
		
		bodydef = new BodyDef(); 
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * textureRegion.getRegionWidth() / textureRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		//dogRunner.assetManager.get(DogSound.GHOST_SOUND.FILE_NAME, Sound.class).setVolume(1, 1);
	}

	@Override
	protected void editSpawnList() {
		
		super.get(3).data = null;

		if (dogRunner.userProfile.invincible > 0 ||
				((int) (Math.random() * 10)) != 0) {
			
			return;
		}
		
		DataPriority data = super.get(3);
		data.priority = 9999;
		data.data = new SpawnerBodyData(PhysicsBodyType.GHOST, this);
	}
	
	@Override
	public void act(SpawnerBodyData data) {
		
		if (data.crashed) {
			
			//20 seconds of invisibility
			//Invincibility.scheduleInvinciblity(20, 5);
			data.destroy = true;
			dogRunner.assetManager.get(DogSound.GHOST_SOUND.FILE_NAME, Sound.class).play();
			//ghostData.soundPlayed = true;
		}
		else if (dogRunner.userProfile.invincible > 0) {
			
			data.destroy = true;
		}
	}

	@Override
	public Drawable getDrawable(SpawnerBodyData data) {

		return textureDrawable;
	}

	@Override
	public float getWidth(SpawnerBodyData data) {
		
		return width;
	}

	@Override
	public float getHeight(SpawnerBodyData data) {
		
		return height;
	}

	@Override
	public BodyDef getBodyDef() {

		return bodydef;
	}

	@Override
	public Shape getShape() {

		return shape;
	}

	@Override
	protected void mDispose() {

		shape.dispose();
	}
}
