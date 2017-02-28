package org.qohs.dogrunner.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Wrapper class for the Box2D World
 * 
 * @author Derek Zhang
 *
 */
public abstract class PhysicsWorld {

	public static final float STEP_RATE = 1/120f;

	protected World world;
	
	private boolean disposed;
	private float accumulator;
	
	//private Box2DDebugRenderer b2DRenderer;
	//private Camera cam = new OrthographicCamera(800f / 400f * 100f, 100f);
	
	/**
	 * Note to children classes: call init in the constructor!!! (if you want it to run)
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
	protected void init() {
	}
	
	/**
	 * Will act (step) for the world if not disposed
	 * the world has a constant "tick" rate
	 * if the time elapsed is too high then
	 * anti lag measures will be used
	 * 
	 * @param delta time elapsed
	 */
	public final void act(float delta) {

		if (disposed) {
			
			return;
		}
		
		preAct(delta);
		
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(delta, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= STEP_RATE) {
//			b2DRenderer.render(world, cam.combined);
	        world.step(STEP_RATE, 1, 1);
	        accumulator -= STEP_RATE;

			perAct();
	    }

		postAct(delta);
	}

	/**
	 * Called before the world act (step) is called
	 * 
	 * @param delta
	 */
	protected void preAct(float delta) {

	}

	/**
	 * Called each time the world act (step) is called
	 * 
	 */
	protected void perAct() {

	}

	/**
	 * Called after all the world act (step) is called
	 * 
	 * @param delta
	 */
	protected void postAct(float delta) {

	}
	
	public boolean isDisposed() {
		
		return disposed;
	}
	
	public void dispose() {
		
		if (!disposed) {
			
			world.dispose();
			world = null;
			disposed = true;
		}
	}
}
