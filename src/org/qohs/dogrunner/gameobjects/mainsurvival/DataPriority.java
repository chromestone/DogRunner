package org.qohs.dogrunner.gameobjects.mainsurvival;

/**
 * 
 * @author Derek Zhang
 *
 */
public class DataPriority {

	public SpawnerBodyData data;
	public int priority;
	
	public DataPriority(SpawnerBodyData data, int priority) {
		
		this.data = data;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		
		return "" + data;
	}
}
