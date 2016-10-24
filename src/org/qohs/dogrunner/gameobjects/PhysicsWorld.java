package org.qohs.dogrunner.gameobjects;

//import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Wrapper class for the Box2D World
 * 
 * @author Derek Zhang
 *
 */
public abstract class PhysicsWorld {

	protected static final float STEP_RATE = 1/60f;

	public World world;
	
	private boolean disposed;
	private float accumulator;
	
//	private Box2DDebugRenderer b2DRenderer;
//	private Camera cam = new OrthographicCamera(800f / 400f * 100f, 100f);
	
	/**
	 * Note to children classes: call init in the constructor!!! (if you want it to run)
	 * 
	 * @param gravity
	 * @param doSleep
	 */
	public PhysicsWorld(Vector2 gravity, boolean doSleep) {
		
		world = new World(gravity, doSleep);
//		b2DRenderer = new Box2DDebugRenderer();
		
		disposed = false;
		accumulator = 0f;
	}
	
	//add bodies to the world and stuff
	//must call this manually from children constructor
	protected void init() {
	}
	
	public final void act(float delta) {

		preAct(delta);
		
	    // fixed time step
	    // max frame time to avoid spiral of death (on slow devices)
	    float frameTime = Math.min(delta, 0.25f);
	    accumulator += frameTime;
	    while (accumulator >= STEP_RATE) {
//			b2DRenderer.render(world, cam.combined);
	        world.step(STEP_RATE, 8, 4);
	        accumulator -= STEP_RATE;

			perAct();
	    }

		postAct(delta);
	}

	protected void preAct(float delta) {

	}

	protected void perAct() {

	}

	protected void postAct(float delta) {

	}
	
	public void dispose() {
		
		if (!disposed) {
			
			world.dispose();
			world = null;
			disposed = true;
		}
	}
}
