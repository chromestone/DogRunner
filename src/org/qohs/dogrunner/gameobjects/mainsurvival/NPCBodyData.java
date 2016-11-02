package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
class NPCBodyData extends PhysicsBodyData {
	
	public boolean crashed;
	public boolean soundPlayed;

	public NPCBodyData(PhysicsBodyType type) {
		
		super(type);
		
		crashed = false;
		soundPlayed = false;
	}
}
