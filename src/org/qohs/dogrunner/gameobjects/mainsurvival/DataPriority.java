package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
class DataPriority {

	public SpawnerBodyData data;
	public int priority;
	
	DataPriority(SpawnerBodyData data, int priority) {
		
		this.data = data;
		this.priority = priority;
	}
	
	/*
	@Override
	public String toString() {
		
		return "" + data;
	}
	*/
}
