package org.qohs.dogrunner.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Wrapper class for the Box2D World
 * @author Derek Zhang
 *
 */
public abstract class PhysicsWorld {

	public World world;
	//private Box2DDebugRenderer b2DRenderer;
	
	private boolean disposed;
	private float accumulator;
	
	/**
	 * Note to children classes: call init!!! (if you want it to run)
	 * 
	 * @param gravity
	 * @param doSleep
	 */
	public PhysicsWorld(Vector2 gravity, boolean doSleep) {
		
		world = new World(gravity, doSleep);
		//b2DRenderer = new Box2DDebugRenderer();
		
		disposed = false;
		accumulator = 0f;
	}
	
	//add bodies to the world and stuff
	//must call this manually from children constructor
	protected abstract void init();
	
	public void render(float delta) {
		
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(delta, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= 1/60f) {
			//b2DRenderer.render(world, cam.combined);
	        world.step(1/60f, 6, 2);
	        accumulator -= 1/60f;
	    }
	}
	
	public void dispose() {
		
		if (!disposed) {
			
			world.dispose();
			world = null;
			disposed = true;
		}
	}
}
