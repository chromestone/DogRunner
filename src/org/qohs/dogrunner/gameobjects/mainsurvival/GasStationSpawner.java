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
	private final TextureRegion jrockTRegion;
	private final TextureRegionDrawable jrockDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;

	public GasStationSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
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
	}

	@Override
	protected void editSpawnList() {
	   
		for (int i = 0; i < 2; i++) {
		DataPriority dataPriority = super.get(i);
		dataPriority.priority = 100;
		dataPriority.data = new SpawnerBodyData(PhysicsBodyType.GAS_STATION, this);}
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
