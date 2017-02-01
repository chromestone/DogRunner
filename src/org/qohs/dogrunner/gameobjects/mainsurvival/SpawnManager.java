package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.Arrays;
import java.util.Iterator;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Derek Zhang
 *
 */
public class SpawnManager {
	
	/**
	 * The velocity of all bodies (entities) spawned by this spawn manager
	 */
	public static final float VELOCITY = 75f;

	public static int ROWS = 6;
	
	private final DogRunner dogRunner;
	
	//THESE SHOULD BE USED/SET BY THE MAIN SURVIVAL WORLD
	//AND THUS ARE IN --->METERS<---
	private final float gameWidth, gameHeight;
	
	//how much time for a car wave (determined by length of NPC car)
	private float carWaveTime;
	//how much time between waves (determined by player car length)
	private float betweenWaveTime;
	
	/*
	 * used to accumulate time
	 * is "subtracted" from when a car is spawned
	 * this ensures accuracy of spawning
	*/
	private float accumulator;
	
	private final MainSurvivalWorld world;
	
	//whether or not a car wave is next
	private boolean pendingCarWave;
	
	private Array<Spawner> carWaveSpawner;
	private Array<Spawner> betweenWaveSpawner;
	
	private DataPriority[] spawnList;
	
	private Array<Body> bodiesArray;
	
	/**
	 * 
	 * @param gameWidth
	 * @param gameHeight
	 * @param occuringLength a synonym for between wave length (should be a function of player car length)
	 */
	public SpawnManager(MainSurvivalWorld world, float gameWidth, float gameHeight, float occuringLength) {
		
		this.dogRunner = DogRunner.getInstance();
		
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		accumulator = 0f;
		
		this.world = world;
		
		pendingCarWave = false;
		
		carWaveSpawner = new Array<Spawner>(10);
		
		betweenWaveSpawner = new Array<Spawner>(10);
		
		//spawnList = new DataPriority[6];
		
		bodiesArray = new Array<Body>(20);
			
		CarSpawner cSp = new CarSpawner(gameWidth, gameHeight);
		
		carWaveTime = cSp.getWidth() / VELOCITY;
		
		betweenWaveTime = occuringLength / VELOCITY;
		
		carWaveSpawner.add(cSp);
		
		spawnList = Arrays.copyOf(cSp.requestSpawnList(), ROWS);
		
		spawnThem();
	}
	
	public void act(float delta) {

		accumulator += delta;
		if (pendingCarWave) {
			
			if (accumulator >= betweenWaveTime) {
				
				requestSpawn(carWaveSpawner);
				
				spawnThem();
				
				accumulator -= betweenWaveTime;
				
				pendingCarWave = !pendingCarWave;
			}
		}
		else {
			
			if (accumulator >= carWaveTime) {
				
				requestSpawn(betweenWaveSpawner);

				spawnThem();
				
				accumulator -= carWaveTime;
				
				pendingCarWave = !pendingCarWave;
			}
		}
		
		for (Body body : bodiesArray) {
			
			SpawnerBodyData data = (SpawnerBodyData) body.getUserData();
			data.spawner.act(data);
		}
	}
	
	public void clean() {
		
		Iterator<Body> iterator = bodiesArray.iterator();
		while (iterator.hasNext()) {

			Body body = iterator.next();
			SpawnerBodyData data = (SpawnerBodyData) body.getUserData();
			//if the car is no longer on the screen
			//delete it to stop wasting memory
			if (body.getPosition().x + data.spawner.getWidth(data) / 2 < 0) {

				iterator.remove();
				
				data.spawner.onDestroy(data);
				
				world.destroyBody(body);
			}
		}
	}
	
	public void render() {
		
		Iterator<Body> iterator = bodiesArray.iterator();
		while (iterator.hasNext()) {

			Body body = iterator.next();
			SpawnerBodyData data = (SpawnerBodyData) body.getUserData();
			Spawner spawner = (data).spawner;
			
			spawner.getDrawable(data).draw(dogRunner.batch, body.getPosition().x - spawner.getWidth(data) / 2f, body.getPosition().y - spawner.getHeight(data) / 2f,  spawner.getWidth(data), spawner.getHeight(data));
		}
	}
	
	private void requestSpawn(Array<Spawner> spawners) {
		
		for (Spawner spawner : spawners) {
			
			DataPriority[] reqSpawnList = spawner.requestSpawnList();
			
			for (int i = 0; i < ROWS; i++) {
				
				if (spawnList[i].data == null) {
					
					spawnList[i] = reqSpawnList[i];
				}
				else if (reqSpawnList[i].data != null) {
					
					if (reqSpawnList[i].priority > spawnList[i].priority) {
						
						spawnList[i] = reqSpawnList[i];
					}
				}
			}
		}
	}
	
	private void spawnThem() {
		
		for (int i = 0; i < ROWS; i++) {
			
			DataPriority dataPriority = spawnList[i];
			
			if (dataPriority.data != null) {
				
				Spawner spawner = dataPriority.data.spawner;
				
				BodyDef bodyDef = spawner.getBodyDef();
				
				bodyDef.position.set(gameWidth + (spawner.getWidth(dataPriority.data) - gameWidth * (21f / 500f)) / 2f, ((i+1) * 2f + 1f) * gameHeight / 12f);
				bodyDef.linearVelocity.set(-VELOCITY, 0f);
				
				Body body = world.createBody(bodyDef);
				
				body.createFixture(spawner.getShape(), 0.0f);
				body.setUserData(dataPriority.data);
				
				bodiesArray.add(body);
			}
			
			spawnList[i].data = null;
		}
	}
	
	public void dispose() {
		
		for (Spawner spawner : carWaveSpawner) {
			
			spawner.dispose();
		}
		
		for (Spawner spawner : betweenWaveSpawner) {
			
			spawner.dispose();
		}
	}
}
