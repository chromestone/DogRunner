package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
class NPCBodyData extends SpawnerBodyData {
	
	public boolean crashed;
	public boolean soundPlayed;

	public NPCBodyData(PhysicsBodyType type, Spawner spawner) {
		
		super(type, spawner);
		
		crashed = false;
		soundPlayed = false;
	}
}
