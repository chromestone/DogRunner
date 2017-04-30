package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 * @author James Mufah
 * 
 */
class GasStationSpawner extends Spawner {
	
	private static final int PRIORITY = 100;
	
	//private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private final TextureRegionDrawable gasDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	private int waves;
	
	private int targetWave;
	private int spawnRetries;//number of spawn retries

	GasStationSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion gasTRegion;
		gasTRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.GAS_STATION.FILE_NAME, Texture.class));
		gasTRegion.flip(false, true);
		gasDrawable = new TextureRegionDrawable(gasTRegion);
		
		bodydef = new BodyDef(); 
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * gasTRegion.getRegionWidth() / gasTRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		//shape.setAsBox((jrockTRegion.getRegionWidth() - gameWidth * (21f / 500f)) / 2, jrockTRegion.getRegionHeight() / 2f - 2f);
		
        ////////////////////////////////
		
		if (dogRunner.userProfile.gasStops == 0) {
			
			dogRunner.userProfile.previousWave = 38;
		}
		else {
			
			dogRunner.userProfile.previousWave += (int) (37.6 * dogRunner.userProfile.gasStops);//(int) (37.6 * Math.pow(1.5, dogRunner.userProfile.gasStops));
		}
		
		dogRunner.userProfile.gasStops++;
		
		targetWave = dogRunner.userProfile.previousWave;
		//System.out.println(targetWave);
		//System.out.println(dateFormat.format(new Date()));
		
		spawnRetries = 0;
		
		waves = 0;
	}

	@Override
	protected void editSpawnList() {
		
		waves++;

		super.get(2).data = null;
		super.get(3).data = null;
		
		if (waves > targetWave) {
			
			//System.out.println(dateFormat.format(new Date()));

			DataPriority data = super.get((int) (Math.random() * 2) + 2);
			data.priority = PRIORITY;
			data.data = new SpawnerBodyData(this);//PhysicsBodyType.GAS_STATION, this);
			
			if (spawnRetries == 0) {
				
				targetWave = dogRunner.userProfile.gasStops * 38;
			}
			else {
				
				targetWave += spawnRetries * 38;
			}
			
			spawnRetries++;
			//System.out.println("Score: "+ dogRunner.userProfile.score);

			//System.out.println(targetWave);
						
			waves = 0;
		}
	}
	
	@Override
	void onCrash(SpawnerBodyData data) {
		
		dogRunner.userProfile.storylineTime = true;
	}

	@Override
	Drawable getDrawable(SpawnerBodyData data) {

		return gasDrawable;
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
