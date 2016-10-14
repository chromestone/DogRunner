package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.PhysicsWorld;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

/**
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

	private float meterWidth;
	private float meterHeight;
	
	public Body carBody;
	private float carWidth = 0f;
	private float carHeight = 0f;
	
	//game over
	public boolean carCrashed;
	
	public MainSurvivalWorld(Vector2 gravity, boolean doSleep, Definition def) {
		
		super(gravity, doSleep);
		
		world.setContactListener(new MyContactListener());
		
		this.meterWidth = def.meterWidth;
		this.meterHeight = def.meterHeight;
		
		this.carWidth = def.carWidth;
		this.carHeight = def.carHeight;
		
		carCrashed = false;
		
		init();
	}

	@Override
	protected void init() {
		
		createCarBody();
		
        ////////////////////////////////
		// Create our body definition
		BodyDef lBoundDef = new BodyDef();  
		// Set its world position
		lBoundDef.position.set(0, meterHeight + 2);  
		lBoundDef.type = BodyType.StaticBody;

		// Create a body from the definition and add it to the world
		Body lBoundBody = world.createBody(lBoundDef);  

		// Create a polygon shape
		PolygonShape lowerBox = new PolygonShape();  
		//it's actually a radius
		lowerBox.setAsBox(meterWidth, 1);
		// Create a fixture from our polygon shape and add it to our ground body  
		lBoundBody.createFixture(lowerBox, 0.0f); 
		// Clean up after ourselves
		lowerBox.dispose();
		
        ////////////////////////////////
		// Create our body definition
		BodyDef uBoundDef = new BodyDef();  
		// Set its world position
		uBoundDef.position.set(0, -1);  
		uBoundDef.type = BodyType.StaticBody;

		// Create a body from the definition and add it to the world
		Body uBoundBody = world.createBody(uBoundDef);  

		// Create a polygon shape
		PolygonShape upperBox = new PolygonShape();  
		upperBox.setAsBox(meterWidth, 1);
		// Create a fixture from our polygon shape and add it to our ground body  
		uBoundBody.createFixture(upperBox, 0.0f); 
		// Clean up after ourselves
		upperBox.dispose();
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
		shape.setAsBox(carWidth / 2, carHeight / 2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		carBody.createFixture(fdef);
		shape.dispose();
	}
	
	private class MyContactListener implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			
			if((contact.getFixtureA().getBody() == carBody
					&& BodyType.DynamicBody.equals(contact.getFixtureB().getBody().getType()))
                    ||
                    (BodyType.DynamicBody.equals(contact.getFixtureA().getBody().getType())
                    && contact.getFixtureB().getBody() == carBody)) {
				
				MainSurvivalWorld.this.carCrashed = true;
			}
		}

		@Override
		public void endContact(Contact contact) {
			
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			
		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			
		}
	}
}
