package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * A spawner does not directly spawn bodies (entities)
 * Instead it is requested by a spawn manager to generate
 * spawn requests per cycle in the form of metadata (user object or body data)
 * encased in the data priority class. 
 * <br><br>
 * A body created by a spawner should have a constant body definition
 * and a constant shape.
 * <br><br>
 * For memory conservation purposes, NONE of the abstract methods
 * of this class should be implemented with creating "new instances" of classes.
 * Except the editSpawnList method when a new "metadata" should be created.
 * It is recommended not to do heavy calculations or create new numbers (width/height).
 * 
 * @see SpawnManager
 * 
 * @author Derek Zhang
 *
 */
abstract class Spawner {
	
	protected final DogRunner dogRunner;

	//private final BodyDef bodyDef;
	//private final Shape shape;
	private final DataPriority[] spawnList;
	
	protected final float gameWidth, gameHeight;
	
	private boolean disposed;
	
	Spawner(float gameWidth, float gameHeight) {
		
		dogRunner = DogRunner.getInstance();
		
		//bodyDef = createBodyDef();
		
		//shape = createShape();
		
		spawnList = new DataPriority[SpawnManager.ROWS];
		
		for (int i = 0; i < SpawnManager.ROWS; i++) {
			
			//spawnList.add(createDefaultDataPriority());
			spawnList[i] = new DataPriority(null, 0);
		}
		
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		disposed = false;
	}
	
	/**
	 * each index in the returned array represents a lane from top to bottom.
	 * for example: 0 is the first lane, 1 is the second lane ... length - 1 is the last lane
	 * 
	 * to represent an "empty" request, this method will return a data priority with a null
	 * data field (the spawner body data will be null)
	 * 
	 * @return an array of data priority representing the spawn requests in each row
	 */
	final DataPriority[] requestSpawnList() {
		
		editSpawnList();
		
		return spawnList;
	}
	
	/**
	 * This method gets the data priority in the spawn list
	 * at the specified index
	 * 
	 * @param index from where to retrieve the data priority from the spawn list
	 * @return the data priority in the spawn list at the index
	 */
	protected final DataPriority get(int index) {
		
		return spawnList[index];
	}
	
	protected final void clear() {
		
		for (DataPriority dataPriority : spawnList) {
			
			dataPriority.data = null;
		}
	}
	
	/**
	 * Called whenever a spawn list is requested
	 * Change the data priority returned by the indices from 
	 * the get method
	 * 
	 */
	abstract protected void editSpawnList();
	
	/*
	/**
	 * Call once for EVERY entity corresponding to
	 * this spawner EVERY frame cycle.
	 * 
	 * @param data
	 *//*
	void act(SpawnerBodyData data) {
		
	}*/
	
	/**
	 * When a specific entity spawned by
	 * the spawner has been crashed into
	 * 
	 * @param data
	 */
	void onCrash(SpawnerBodyData data) {
		
		
	}
	
	/**
	 * Called whenever an entity corresponding to the spawner
	 * is destroyed. More specifically WHEN THE ENTITY IS
	 * OFF THE SCREEN.
	 * <br><br>
	 * NOTE: If destroying was due to data.destroyed = true,
	 * then this method will NOT be called.
	 * 
	 * @param data
	 */
	void onDestroy(SpawnerBodyData data) {
		
	}
	
	/**
	 * What I look like. lolz :)
	 * 
	 * @param data
	 * @return drawable (sprite of what I look like)
	 */
	abstract Drawable getDrawable(SpawnerBodyData data);
	
	/**
	 * How wide I am.
	 * 
	 * @param data
	 * @return width of me
	 */
	abstract float getWidth(SpawnerBodyData data);
	
	/**
	 * How tall I am.
	 * 
	 * @param data
	 * @return height of me
	 */
	abstract float getHeight(SpawnerBodyData data);
	
	abstract BodyDef getBodyDef();
	
	/**
	 * Used by the spawn manager when an entity is to be spawned.
	 * <br><br>
	 * Are you in love with this method? Get the reference? (Ed Sheeran)
	 * 
	 * @return shape of me
	 */
	abstract Shape getShape();
	
	final void dispose() {

		if (!disposed) {
			
			mDispose();
			
			disposed = true;
		}
	}
	
	/**
	 * You should dispose shape and other disposable stuff
	 */
	abstract protected void mDispose();
}
