package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
class SpawnerBodyData extends PhysicsBodyData {

	final Spawner spawner;
	boolean crashed;
	boolean destroy;
	
	SpawnerBodyData(Spawner spawner) {
		
		super(PhysicsBodyType.SPAWNER_ENTITY);
		
		this.spawner = spawner;
		crashed = false;
		destroy = false;
	}
}
