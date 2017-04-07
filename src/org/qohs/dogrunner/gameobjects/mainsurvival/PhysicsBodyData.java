package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * Used a "metadata" or attached data to a Body in the Box2D
 * physics world
 * 
 * @author Derek Zhang
 *
 */
class PhysicsBodyData {

	final PhysicsBodyType type;
	
	PhysicsBodyData(PhysicsBodyType type) {
		
		this.type = type;
	}
}
