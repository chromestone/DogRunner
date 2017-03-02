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
 */
public class GasStationSpawner extends Spawner {
	
	/**
	 * note: jrock is named after James M. by James
	 */
	private final TextureRegionDrawable jrockDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	//private int spawnScore;
	private int waves;
	
	/*
	private int prevSpawnScore;
	private int spawnAgainTarget;//number of spawn retries
	private int spawnAgainCountdown;
	*/

	public GasStationSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion jrockTRegion;
		jrockTRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.GAS_STATION.FILE_NAME, Texture.class));
		jrockTRegion.flip(false, true);
		jrockDrawable = new TextureRegionDrawable(jrockTRegion);
		
		bodydef = new BodyDef(); 
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * jrockTRegion.getRegionWidth() / jrockTRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		//shape.setAsBox((jrockTRegion.getRegionWidth() - gameWidth * (21f / 500f)) / 2, jrockTRegion.getRegionHeight() / 2f - 2f);
		
		//spawnScore = dogRunner.userProfile.previousGasScore;
			
		//dogRunner.userProfile.previousGasScore += 100 * (int) Math.pow(1.5, dogRunner.userProfile.gasStops);
		//dogRunner.userProfile.gasStops++;
		
		/*
		prevSpawnScore = 0;
		if (dogRunner.storyFM.scoreToStoryline.containsKey(dogRunner.userProfile.score - prevSpawnScore)) {

			prevSpawnScore = spawnScore;
			spawnScore = dogRunner.userProfile.score;
		}
		else {

			Integer score = dogRunner.storyFM.scoreToStoryline.higherKey(dogRunner.userProfile.score - prevSpawnScore);
			if (score == null) {

				spawnScore = -1;
			}
			else {

				prevSpawnScore = spawnScore;
				spawnScore = score.intValue() + dogRunner.userProfile.score;
			}
		}
		
		spawnAgainTarget = 1;
		spawnAgainCountdown = spawnAgainTarget;
		*/
		
		waves = 0;
	}

	@Override
	protected void editSpawnList() {
		
		waves++;

		super.get(3).data = null;
		
		if (waves > 95) {

			DataPriority data = super.get(3);
			data.priority = 10000;
			data.data = new SpawnerBodyData(PhysicsBodyType.GAS_STATION, this);

			//System.out.println(dogRunner.userProfile.score);
			
			waves = 0;
		}
		
		/*
		clear();
		
		spawnAgainCountdown --;
		
		if (spawnAgainCountdown > 0) {
			
			return;
		}
	   
		if (spawnScore != -1 && dogRunner.userProfile.score >= spawnScore) {
			
			DataPriority data = super.get(((int) (Math.random() * 2)) + (SpawnManager.ROWS - 1) / 2);
			data.priority = 10000;
			data.data = new SpawnerBodyData(PhysicsBodyType.GAS_STATION, this);
			
			try {
				
				spawnAgainTarget = Math.multiplyExact(spawnAgainTarget, 2);
				spawnAgainCountdown = spawnAgainTarget;
			}
			catch (ArithmeticException e) {
				
				spawnAgainCountdown = Integer.MAX_VALUE;
			}
		}*/
	}

	@Override
	public Drawable getDrawable(SpawnerBodyData data) {

		return jrockDrawable;
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
