package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.gameobjects.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
 * Encapsulates all the physics (Box2D) code for the Main Survival Screen
 * 
 * @author Derek Zhang
 *
 */
public class MainSurvivalWorld extends PhysicsWorld {
	
	/**
	 * This class consolidates all the constructor parameters of MainSurvivalWorld
	 * 
	 * @author Derek Zhang
	 *
	 */
	public static class Definition {
		
		public float meterWidth = 0f;
		public float meterHeight = 0f;
		
		public float carWidth = 0f;
		public float carHeight = 0f;
	}
	
	////////////////////////////////

	private float meterWidth;
	private float meterHeight;
	
	public Body carBody;
	private float carWidth = 0f;
	private float carHeight = 0f;
	
	//game over
	//public boolean playerCarTotalled;

	//public CarSpawner carSpawner;
	private RoadManager roadManager;
	private SpawnManager spawnManager;
	private GasConsumer gasConsumer;
	
	//public int crashes;
	
	//private Box2DDebugRenderer b2DRenderer = new Box2DDebugRenderer();
	
	public MainSurvivalWorld(Vector2 gravity, boolean doSleep, Definition def) {
		
		super(gravity, doSleep);
		
		world.setContactListener(new MyContactListener());
		
		this.meterWidth = def.meterWidth;
		this.meterHeight = def.meterHeight;
		
		this.carWidth = def.carWidth;
		this.carHeight = def.carHeight;
		
		//playerCarTotalled = false;
		
		//crashes = 0;

		//carSpawner = new CarSpawner(world, meterWidth, meterHeight, carWidth * 1.5f);
		roadManager = new RoadManager(meterWidth, meterHeight, 165f);
		
		init();
		
		spawnManager = new SpawnManager(this, meterWidth, meterHeight, carWidth * 1.5f);
		gasConsumer = new GasConsumer();
	}

	private void init() {
		
		createCarBody();
		
        ////////////////////////////////
		//create the walls
		
        ////////////////////////////////
		//lower wall
		// Create our body definition
		BodyDef lBoundDef = new BodyDef();  
		// Set its world position
		lBoundDef.position.set(0f, meterHeight + 2f);  
		lBoundDef.type = BodyType.StaticBody;

		// Create a body from the definition and add it to the world
		Body lBoundBody = world.createBody(lBoundDef); 
		lBoundBody.setUserData(new PhysicsBodyData(PhysicsBodyType.WALL));

		// Create a polygon shape
		PolygonShape lowerBox = new PolygonShape();  
		//it's actually a radius
		lowerBox.setAsBox(meterWidth, 1f);
		// Create a fixture from our polygon shape and add it to our ground body  
		lBoundBody.createFixture(lowerBox, 0.0f);
		// Clean up after ourselves
		lowerBox.dispose();
		
        ////////////////////////////////
		//upper wall
		// Create our body definition
		BodyDef uBoundDef = new BodyDef();
		// Set its world position
		uBoundDef.position.set(0f, -1f);  
		uBoundDef.type = BodyType.StaticBody;

		// Create a body from the definition and add it to the world
		Body uBoundBody = world.createBody(uBoundDef);  
		uBoundBody.setUserData(new PhysicsBodyData(PhysicsBodyType.WALL));

		// Create a polygon shape
		PolygonShape upperBox = new PolygonShape();  
		upperBox.setAsBox(meterWidth, 1f);
		// Create a fixture from our polygon shape and add it to our ground body  
		uBoundBody.createFixture(upperBox, 0.0f); 
		// Clean up after ourselves
		upperBox.dispose();
	}
	
	@Override
	protected void preAct(float delta) {
		
		spawnManager.act(delta);
		gasConsumer.act(delta);
	}

	@Override
	protected void perAct() {

		//carSpawner.act(STEP_RATE);
		roadManager.act();//STEP_RATE);
		//spawnManager.act(STEP_RATE);
	}

	/*
	@Override
	protected void postAct(float delta) {

		spawnManager.clean();
		//carSpawner.clean();
	}
	*/
	
	@Override
	public void render() {
		
		roadManager.render();
		spawnManager.render();
	}

	@Override
	public void dispose() {
		
		if (!isDisposed()) {
			
			spawnManager.dispose();
		}
		
		super.dispose();
	}
	
	private void createCarBody() {
		
		BodyDef bdef = new BodyDef();
		//bdef.position.set(5f * carWidth / 8f, (meterHeight - carHeight) / 2);
		bdef.position.set(carWidth / 2f + carWidth / 5f, (meterHeight - carHeight) / 2f);
		bdef.type = BodyType.DynamicBody;
		//bdef.bullet = true;
		
		carBody = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		
		/*
		//it's actually a "radius"
		shape.setAsBox(carWidth / 2f, carHeight / 2f);
		Vector2 v = new Vector2();
		for (int i = 0; i < shape.getVertexCount(); i++) {
		shape.getVertex(i, v);
		System.out.println(v);
		System.out.println(System.identityHashCode(v));}*/
		
		shape.set(new Vector2[]{new Vector2(-carWidth / 2f, -carHeight / 2f),
				new Vector2(-carWidth / 2f, carHeight / 2f),
				new Vector2(carWidth / 2f, carHeight / 2f),
				new Vector2(carWidth / 2f, 0f),
				new Vector2(0, -carHeight / 2f)});
		
		carBody.createFixture(shape, 0f);
		shape.dispose();
		
		carBody.setUserData(new PhysicsBodyData(PhysicsBodyType.PLAYER_CAR));
	}
	
	/*
	public void method(com.badlogic.gdx.graphics.Camera cam) {
		
		b2DRenderer.render(world, cam.combined);
	}
	*/
	
	Body createBody(BodyDef bodyDef) {
		
		return world.createBody(bodyDef);
	}
	
	void destroyBody(Body body) {
		
		world.destroyBody(body);
	}
	
	private class MyContactListener implements ContactListener {

		private DogRunner dogRunner = DogRunner.getInstance();
		
		@Override
		public void beginContact(Contact contact) {
			
			boolean isCarBodyA = contact.getFixtureA().getBody() == MainSurvivalWorld.this.carBody;

			//if this is a contact between the player's car and a non static body
			if (isCarBodyA ||contact.getFixtureB().getBody() == MainSurvivalWorld.this.carBody) {

				Body crashedBody = isCarBodyA ? contact.getFixtureB().getBody() : contact.getFixtureA().getBody();
				
				PhysicsBodyData data = (PhysicsBodyData) crashedBody.getUserData();
				PhysicsBodyType type = data.type;
				
				if (dogRunner.userProfile.invincible > 0) {
					
					return;
				}
				
				switch (type) {
				
				case WALL: {
					
					break;
				}
				case PLAYER_CAR: {
					
					throw new IllegalStateException("A player car crashed into another player car.");
				}
				case SPAWNER_ENTITY: {
					
					SpawnerBodyData spawnerData = ((SpawnerBodyData) data);
					
					spawnerData.spawner.onCrash(spawnerData);
					
					break;
				}
				default: {
					
					break;
				}
				}
			}
		}

		@Override
		public void endContact(Contact contact) {
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			
			boolean isCarBodyA = contact.getFixtureA().getBody() == MainSurvivalWorld.this.carBody;

			//if this is a contact between the player's car and a non static body
			if ((isCarBodyA ||contact.getFixtureB().getBody() == MainSurvivalWorld.this.carBody)) {

				Body crashedBody = isCarBodyA ? contact.getFixtureB().getBody() : contact.getFixtureA().getBody();
				
				PhysicsBodyData data = (PhysicsBodyData) crashedBody.getUserData();
				PhysicsBodyType type = data.type;
				
				if (type != PhysicsBodyType.WALL) {
					
					contact.setEnabled(false);
				}
				/*
				switch (type) {
				
				case NPC_CAR: {
					
					contact.setEnabled(false);
					break;
				}
				case WALL: {
					
					break;
				}
				case PLAYER_CAR: {
					
					throw new IllegalStateException("A player car crashed into another player car.");
				}
				default: {
					
					break;
				}
				}
				*/
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			
		}
	}
}
