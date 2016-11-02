package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.*;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

/**
 * This class spawns the cars in the Main Survival World
 * Note that this class should be used in accordance
 * and inside a physics world (see act and clean)
 * 
 * @author Derek Zhang
 *
 */
public class CarSpawner {
	
	private static final float VELOCITY = 75f;
	
	private final DogRunner dogRunner;
	
	private World world;
	
	private Array<Body> carArray;
	
	//THESE SHOULD BE USED/SET BY THE MAIN SURVIVAL WORLD
	//AND THUS ARE IN --->METERS<---
	private float gameWidth, gameHeight;
	
	private float time;
	
	/*
	 * used to accumulate time
	 * is "subtracted" from when a car is spawned
	 * this ensures accuracy of spawning
	*/
	private float accumulator;
	
	private TextureRegion carTexture;
	private float carWidth, carHeight;
	
	private TextureRegion crashedTexture;
	
	private boolean[] prevFormation;
	private boolean[] currentFormation;
	private byte[] openRows;
	
	public CarSpawner(World world, float gameWidth, float gameHeight, float occuringLength) {
		
		dogRunner = DogRunner.getInstance();
		
		this.world = world;
		
		carArray = new Array<Body>(10);
		
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		accumulator = 0f;
		
		carTexture = new TextureRegion(dogRunner.assetManager.get(DogAssets.WHITE_CAR.FILE_NAME, Texture.class));
		carTexture.flip(false, true);
		
		carHeight = 7f * gameHeight / 48f;
		carWidth = carHeight / carTexture.getTexture().getHeight() * carTexture.getTexture().getWidth();
		
		crashedTexture = new TextureRegion(dogRunner.assetManager.get(DogAssets.EXPLODE.FILE_NAME, Texture.class));
		
		time = (occuringLength + carWidth) / VELOCITY;
		
		prevFormation = new boolean[6];
		currentFormation = new boolean[6];
		openRows = new byte[6];
		Arrays.fill(openRows, (byte) - 1);
		
		ArrayList<Byte> firstFormation = new ArrayList<Byte>();
		for (byte i = 0; i < 6; i++) {
			
			firstFormation.add(new Byte(i));
		}
		Collections.shuffle(firstFormation);
		//leave at least one row empty
		int rowsFilled = (int) (Math.random() * 5);
		for (int i = 0; i < rowsFilled; i++) {
			
			if (Math.random() * 2 >= 1) {
				
				byte row = firstFormation.get(i).byteValue();
				prevFormation[row] = true;
				carArray.add(createCarBody(row));
			}
		}
	}

	/**
	 * Note that this should be called within the Physics World per act method
	 * @param delta
     */
	public void act(float delta) {

		//float frameTime = Math.min(delta, .25f);
		accumulator += delta;//frameTime;
		if (accumulator >= time) {
			
			int openRowCount = 0;
			
			//int i = 0;
			for (byte row = 0; row < 6; row++) {
				
				//if the previous wave of cars did spawn a car at this row
				if (prevFormation[row]) {
					
					if (Math.random() * 2 >= 1) {
						
						currentFormation[row] = true;
						carArray.add(createCarBody(row));
					}
					else {
						
						currentFormation[row] = false;
					}
				}
				else {
					
					currentFormation[row] = false;
					openRows[openRowCount++] = row;
				}
			}
			
			boolean previousFilled = false, currentFilled = false;
			for (byte i = 0; i < openRowCount && openRows[i] >= 0; i++) {
				
				previousFilled = (openRows[i] - 1 >= 0) ? currentFormation[openRows[i] - 1]: true;
				currentFilled = (openRows[i] + 1 < 6) ? currentFormation[openRows[i] + 1]: true;
				
				if (!previousFilled || ! currentFilled) {
					
					if (Math.random() * 2 >= 1) {
						
						currentFormation[openRows[i]] = true;
						carArray.add(createCarBody(openRows[i]));
					}
				}
			}

			//Body body = createCarBody((int) (Math.random() * 6));
			//carArray.add(body);
			
			for (byte i = 0; i < 6; i++) {
				
				prevFormation[i] = currentFormation[i];
			}
			
			//Arrays.fill(currentFormation, false);
			//Arrays.fill(openRows, -1);
			
			accumulator %= time;
		}
	}

	/**
	 * Should be called once per render
	 * recommended to be called in the post act
	 * in the Physics World
	 */
	public void clean() {

		Iterator<Body> iterator = carArray.iterator();
		while (iterator.hasNext()) {

			Body car = iterator.next();
			//if the car is no longer on the screen
			//delete it to stop wasting memory
			if (car.getPosition().x + carWidth / 2 < 0) {

				iterator.remove();
				world.destroyBody(car);
				dogRunner.userProfile.score += 1;
			}
		}
	}

	public void render() {

		Iterator<Body> iterator = carArray.iterator();
		dogRunner.batch.begin();
		while (iterator.hasNext()) {
			
			Body car = iterator.next();
			
			NPCBodyData npcData = (NPCBodyData) car.getUserData();
				dogRunner.batch.draw(crashedTexture, car.getPosition().x - carWidth / 2f, car.getPosition().y - carHeight / 2f, carWidth, carHeight);
			}
			else {

				dogRunner.batch.draw(carTexture, car.getPosition().x - carWidth / 2f, car.getPosition().y - carHeight / 2f, carWidth, carHeight);
			}
		}
		dogRunner.batch.end();
		
		/*
		//DEBUGGING
		iterator = carArray.iterator();
		dogRunner.renderer.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line);
		while (iterator.hasNext()) {
			
			Body car = iterator.next();
			dogRunner.renderer.rect(car.getPosition().x - (carWidth - 7f) / 2, car.getPosition().y - (carHeight - 5f) / 2, carWidth - 7f, carHeight - 5f);
		}
		dogRunner.renderer.end();
		*/
	}
	
	private Body createCarBody(byte row) {
		
		BodyDef bodyDef = new BodyDef(); 
		
		// Set its world position
		bodyDef.position.set(gameWidth + (carWidth - gameWidth * (21f / 500f)) / 2f, (row * 2f + 1f) * gameHeight / 12f);//randomFactor + (carHeight - 5f) / 2);  
		bodyDef.linearVelocity.set(-VELOCITY, 0f);
		bodyDef.type = BodyType.DynamicBody;

		// Create a body from the definition and add it to the world
		Body body = world.createBody(bodyDef);
		body.setUserData(PhysicsBodyType.NPC_CAR);

		// Create a polygon shape
		PolygonShape shape = new PolygonShape();  
		//it's actually a radius
		shape.setAsBox((carWidth - gameWidth * (21f / 500f)) / 2, carHeight / 2f - 2f);
		// Create a fixture from our polygon shape and add it to our ground body  
		body.createFixture(shape, 0.0f); 
		// Clean up after ourselves
		shape.dispose();
		
		return body;
	}
}
