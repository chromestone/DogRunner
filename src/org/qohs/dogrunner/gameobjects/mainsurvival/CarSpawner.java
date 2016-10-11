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
 * This class spawns the cars in the Main Survival World
 * 
 * @author Derek Zhang
 *
 */
public class CarSpawner {
	
	private static final int VELOCITY = 120;
	
	private final DogRunner dogRunner;
	
	private World world;
	
	private Array<Body> carArray;
	
	//THESE SHOULD BE USED/SET BY THE MAIN SURVIVAL WORLD
	//AND THUS ARE IN --->METERS<---
	private float gameWidth, gameHeight;
	
	private float time;
	
	private float accumulator;
	
	private TextureRegion carTexture;
	private float carWidth, carHeight;
	
	public CarSpawner(World world, float gameWidth, float gameHeight, float occuringLength) {
		
		dogRunner = DogRunner.getInstance();
		
		this.world = world;
		
		carArray = new Array<Body>(10);
		
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		accumulator = 0f;
		
		carTexture = new TextureRegion(dogRunner.assetManager.get(DogAssets.RED_CAR.fileName, Texture.class));
		carTexture.flip(false, true);
		
		carHeight = 7f * gameHeight / 48f;
		carWidth = carHeight / carTexture.getTexture().getHeight() * carTexture.getTexture().getWidth();
		
		time = (occuringLength + carWidth) / VELOCITY;
	}
	
	public void act(float delta) {
		
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		if (accumulator >= time) {
			
			Body body = createCarBody();
			carArray.add(body);

			accumulator = 0;
		}
		Iterator<Body> iterator = carArray.iterator();
		while (iterator.hasNext()) {
			
			Body car = iterator.next();
			if (car.getPosition().x + carWidth / 2 < 0) {
				
				iterator.remove();
				world.destroyBody(car);
				dogRunner.userProfile.score += 10;
			}
		}
	}

	public void render() {
		
		Iterator<Body> iterator = carArray.iterator();
		dogRunner.batch.begin();
		while (iterator.hasNext()) {
			
			Body car = iterator.next();
			dogRunner.batch.draw(carTexture, car.getPosition().x - carWidth / 2, car.getPosition().y - carHeight / 2, carWidth, carHeight);
		}
		dogRunner.batch.end();
		
		/*
		//DEBUGGING
		iterator = carArray.iterator();
		dogRunner.renderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line);
		while (iterator.hasNext()) {
			
			Body car = iterator.next();
			dogRunner.renderer.rect(car.getPosition().x - (carWidth - 7f) / 2, car.getPosition().y - (carHeight - 7f) / 2, carWidth - 7f, carHeight - 7f);
		}
		dogRunner.renderer.end();
		*/
	}
	
	private Body createCarBody() {
		
		BodyDef bodyDef = new BodyDef();  
		// Set its world position
		int choice = (int) (Math.random() * 6);
		float randomFactor = gameHeight / 48;
//		if (choice > 0) {
//			
//			randomFactor += gameHeight / 48 + carHeight;
//			if (choice > 1) {
//				
//				randomFactor += gameHeight / 48 + carHeight;
//			}
//		}
		for (int i = choice; i > 0; i--) {
			
			randomFactor += gameHeight / 48 + carHeight;
		}
		bodyDef.position.set(gameWidth + (carWidth - gameWidth * (21f / 500f)) / 2, randomFactor + (carHeight - 4f) / 2);  
		bodyDef.linearVelocity.set(-VELOCITY, 0);
		bodyDef.type = BodyType.DynamicBody;

		// Create a body from the definition and add it to the world
		Body body = world.createBody(bodyDef);  

		// Create a polygon shape
		PolygonShape shape = new PolygonShape();  
		//it's actually a radius
		shape.setAsBox((carWidth - gameWidth * (21f / 500f)) / 2, (carHeight - 4f) / 2);
		// Create a fixture from our polygon shape and add it to our ground body  
		body.createFixture(shape, 0.0f); 
		// Clean up after ourselves
		shape.dispose();
		
		return body;
	}
}
