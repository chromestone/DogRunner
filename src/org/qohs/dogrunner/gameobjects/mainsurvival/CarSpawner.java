package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.Iterator;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Derek Zhang
 *
 */
public class CarSpawner {
	
	private static final int VELOCITY = 120;
	
	private final DogRunner dogRunner;
	
	private World world;
	
	private Array<Car> carArray;
	
	private float gameWidth, gameHeight;
	
	private float time;
	
	private float accumulator;
	
	private TextureRegion carTexture;
	private float carWidth, carHeight;
	
	public CarSpawner(World world, float gameWidth, float gameHeight, float occuringLength) {
		
		dogRunner = DogRunner.getInstance();
		
		this.world = world;
		
		carArray = new Array<Car>(10);
		
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		accumulator = 0f;
		
		carTexture = new TextureRegion(dogRunner.assetManager.get(DogAssets.RED_CAR.fileName, Texture.class));
		carTexture.flip(false, true);
		
		carHeight = gameHeight / 4;
		carWidth = carHeight / carTexture.getTexture().getHeight() * carTexture.getTexture().getWidth();
		
		time = (occuringLength + carWidth) / VELOCITY;
	}

	public void render(float delta) {

		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		if (accumulator >= time) {
			
			Body body = createCarBody();
			carArray.add(new Car(body));

			accumulator = 0;
		}
		
		Iterator<Car> iterator = carArray.iterator();
		dogRunner.batch.begin();
		while (iterator.hasNext()) {
			
			Car car = iterator.next();
			if (car.render()) {
				
				iterator.remove();
				world.destroyBody(car.body);
				dogRunner.userProfile.score += 10;
			}
		}
		dogRunner.batch.end();
	}
	
	private Body createCarBody() {
		
		BodyDef bodyDef = new BodyDef();  
		// Set its world position
		int choice = (int) (Math.random() * 3);
		float randomFactor = gameHeight / 6;
		if (choice > 0) {
			
			randomFactor += gameHeight / 12 + carHeight;
			if (choice > 1) {
				
				randomFactor += gameHeight / 12 + carHeight;
			}
		}
		bodyDef.position.set(gameWidth + carWidth / 2, randomFactor);  
		bodyDef.linearVelocity.set(-VELOCITY, 0);
		bodyDef.type = BodyType.DynamicBody;

		// Create a body from the definition and add it to the world
		Body body = world.createBody(bodyDef);  

		// Create a polygon shape
		PolygonShape shape = new PolygonShape();  
		//it's actually a radius
		shape.setAsBox(carWidth / 2, carHeight / 2);
		// Create a fixture from our polygon shape and add it to our ground body  
		body.createFixture(shape, 0.0f); 
		// Clean up after ourselves
		shape.dispose();
		
		return body;
	}
	
	private class Car {
		
		protected final Body body;
		
		public Car(Body body) {
			
			this.body = body;
		}
		
		//returns true if out of bounds
		public boolean render() {
			
			CarSpawner parent = CarSpawner.this;
			
			if (body.getPosition().x + parent.carWidth / 2 < 0) {
				
				return true;
			}

			dogRunner.batch.draw(parent.carTexture, body.getPosition().x - parent.carWidth / 2, body.getPosition().y - parent.carHeight / 2, parent.carWidth, parent.carHeight);
			return false;
		}
	}
}
