package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class ReverseSpawner extends Spawner {
	
	private final static int PRIORITY = 80;
	
	private final TextureRegionDrawable textureDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	ReverseSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion textureRegion;
		textureRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.REVERSE.FILE_NAME, Texture.class));
		textureRegion.flip(false, true);
		textureDrawable = new TextureRegionDrawable(textureRegion);
		
		bodydef = new BodyDef(); 
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * textureRegion.getRegionWidth() / textureRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
	}

	@Override
	protected void editSpawnList() {
		
		super.clear();

		if (dogRunner.userProfile.invertControl) {//19

			if (Math.random() * 10 < 1) {
				
				DataPriority data = super.get((int)(Math.random()*2) + 2);
				data.priority = PRIORITY;
				data.data = new SpawnerBodyData(this);
			}
		}
		else if (Math.random() * 30 < 1) {

			DataPriority data = super.get((int)(Math.random()*2) + 2);
			data.priority = PRIORITY;
			data.data = new SpawnerBodyData(this);//PhysicsBodyType.UNICORN, this);
		}
	}
	
	@Override
	void onCrash(SpawnerBodyData data) {
		
		data.destroy = true;
		
		dogRunner.userProfile.invertControl = !dogRunner.userProfile.invertControl;
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
