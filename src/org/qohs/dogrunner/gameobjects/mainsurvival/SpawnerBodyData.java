package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
public class SpawnerBodyData extends PhysicsBodyData {

	public Spawner spawner;
	
	public SpawnerBodyData(PhysicsBodyType type, Spawner spawner) {
		
		super(type);
		
		this.spawner = spawner;
	}

}
