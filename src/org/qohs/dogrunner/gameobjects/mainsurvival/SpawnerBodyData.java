package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
public class SpawnerBodyData extends PhysicsBodyData {

	public final Spawner spawner;
	public boolean crashed;
	public boolean destroy;
	
	public SpawnerBodyData(PhysicsBodyType type, Spawner spawner) {
		
		super(type);
		
		this.spawner = spawner;
		crashed = false;
		destroy = false;
	}

}
