package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * Data for the NPC cars (main incoming cars)
 * 
 * @author Derek Zhang
 *
 */
class NPCBodyData extends SpawnerBodyData {

	public boolean soundPlayed;

	public NPCBodyData(Spawner spawner) {
		
		super(PhysicsBodyType.NPC_CAR, spawner);

		soundPlayed = false;
	}
}
