package org.qohs.dogrunner.gameobjects.mainsurvival;

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
	public boolean playerCarTotalled;

	//public CarSpawner carSpawner;
	public RoadManager roadManager;
	public SpawnManager spawnManager;
	
	public int crashes;
	
	public MainSurvivalWorld(Vector2 gravity, boolean doSleep, Definition def) {
		
		super(gravity, doSleep);
		
		world.setContactListener(new MyContactListener());
		
		this.meterWidth = def.meterWidth;
		this.meterHeight = def.meterHeight;
		
		this.carWidth = def.carWidth;
		this.carHeight = def.carHeight;
		
		playerCarTotalled = false;
		
		crashes = 0;

		//carSpawner = new CarSpawner(world, meterWidth, meterHeight, carWidth * 1.5f);
		roadManager = new RoadManager(meterWidth, meterHeight, 165f);
		
		init();
		
		spawnManager = new SpawnManager(this, meterWidth, meterHeight, (carWidth - meterWidth * (12f / 500f)) * 1.5f);
	}

	@Override
	protected void init() {
		
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
	protected void perAct() {

		//carSpawner.act(STEP_RATE);
		roadManager.act(STEP_RATE);
		spawnManager.act(STEP_RATE);
	}

	@Override
	protected void postAct(float delta) {

		spawnManager.clean();
		//carSpawner.clean();
	}
	

	@Override
	public void dispose() {
		
		super.dispose();
		
		if (isDisposed()) {
			
			spawnManager.dispose();
		}
	}
	
	private void createCarBody() {
		
		BodyDef bdef = new BodyDef();
		//bdef.position.set(5f * carWidth / 8f, (meterHeight - carHeight) / 2);
		bdef.position.set(carWidth / 2f, (meterHeight - carHeight) / 2f);
		bdef.type = BodyType.DynamicBody;
		bdef.bullet = true;
		
		carBody = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		//it's actually a "radius"
		shape.setAsBox(carWidth / 2f, carHeight / 2f);
		carBody.createFixture(shape, 0f);
		shape.dispose();
		
		carBody.setUserData(PhysicsBodyType.PLAYER_CAR);
	}
	
	public Body createBody(BodyDef bodyDef) {
		
		return world.createBody(bodyDef);
	}
	
	public void destroyBody(Body body) {
		
		world.destroyBody(body);
	}
	
	private class MyContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			
			boolean isCarBodyA = contact.getFixtureA().getBody() == MainSurvivalWorld.this.carBody;

			//if this is a contact between the player's car and a non static body
			if (isCarBodyA ||contact.getFixtureB().getBody() == MainSurvivalWorld.this.carBody) {

				Body crashedBody = isCarBodyA ? contact.getFixtureB().getBody() : contact.getFixtureA().getBody();
				
				PhysicsBodyData data = (PhysicsBodyData) crashedBody.getUserData();
				PhysicsBodyType type = data.type;
				
				switch (type) {
				
				case NPC_CAR: {
					
					//MainSurvivalWorld.this.playerCarTotalled = true;
					MainSurvivalWorld.this.crashes++;
					NPCBodyData npcData = ((NPCBodyData) data);
					npcData.crashed = true;
					
					
					if (MainSurvivalWorld.this.crashes >= 3) {
						
						MainSurvivalWorld.this.playerCarTotalled = true;
					}
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
			}
			
			/*
			if((contact.getFixtureA().getBody() == carBody
					&& BodyType.DynamicBody.equals(contact.getFixtureB().getBody().getType()))
                    ||
                    (BodyType.DynamicBody.equals(contact.getFixtureA().getBody().getType())
                    && contact.getFixtureB().getBody() == carBody)) {
				
				MainSurvivalWorld.this.carCrashed = true;
			}
			*/
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
			}
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			
		}
	}
}
