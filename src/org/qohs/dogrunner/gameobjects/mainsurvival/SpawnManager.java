package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.Arrays;
import java.util.Iterator;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

/**
 * Handles all spawning for the Main Survival World.
 * There are two types of spawning: car waves and between car waves.
 * <br><br>
 * Car wave spawns are spawned in the wave that cars are spawned.
 * Please keep the width less than the NPC car width.
 * <br><br>
 * Between car wave spawns are spawned between the waves that cars are spawned.
 * (IKR, totally not obvious?) Please also keep the width less than
 * approx. 1.5 * player car width.
 * <br><br>
 * Each time a wave is about to be spawned, the spawn manager calls
 * for requesting spawns to all the designated spawners (car wave or in between).
 * <br><br>
 * Once the request is processed and a spawn list is "compiled" based on priorities,
 * the spawns with the highest priorities will be spawned.
 * (yes an actual entity (body) in the world)
 * <br><br>
 * Afterward, ALL ENTITIES SPAWNED (in the same loop, even those just spawned) will have
 * their corresponding spawner's act method called.
 * <br><br>
 * The following processes happen EVERY FRAME CYCLE:
 * (regardless if there was a spawn request, but if there was, always AFTER)
 * <br><br>
 * Then "house cleaning" is done. Those entities off the screen are cleared, and the corresponding
 * spawner's "onDestroy" method is called
 * <br><br>
 * Finally, the entity is rendered using the spawner's "getDrawable" method and the cycle is done :)
 * 
 * @author Derek Zhang
 *
 */
//note to programmers looking to register spawner find "PROGRAMMERS LOOK HERE - #DANKMEMES"
public class SpawnManager {
	
	/**
	 * The velocity of all bodies (entities) spawned by this spawn manager
	 */
	public static final float VELOCITY = 75f;//75f

	public static int ROWS = 6;
	
	private final DogRunner dogRunner;
	
	//THESE SHOULD BE USED/SET BY THE MAIN SURVIVAL WORLD
	//AND THUS ARE IN --->METERS<---
	private final float gameHeight;//, gameWidth;
	
	//how much time for a car wave (determined by length of NPC car)
	private float carWaveTime;
	//how much time between waves (determined by player car length)
	private float betweenWaveTime;
	
	/**
	 * used to accumulate time
	 * is "subtracted" from when a car is spawned
	 * this ensures accuracy of spawning
	*/
	private float accumulator;
	
	private final MainSurvivalWorld world;
	
	/**
	 * whether or not a car wave is next
	 */
	private boolean pendingCarWave;
	
	private final Spawner[] carWaveSpawner;
	private final Spawner[] betweenWaveSpawner;
	
	private final DataPriority[] emptySpawnList;
	private final DataPriority[] spawnList;
	
	private Array<Body> bodiesArray;
	
	/**
	 * Pre-calculated positions
	 */
	private float carWavePos, betweenWavePos;
	
	/**
	 * 
	 * @param gameWidth the width of the game
	 * @param gameHeight the height of the game
	 * @param occurringLength a synonym for between wave length (should be a function of player car length)
	 */
	SpawnManager(MainSurvivalWorld world, float gameWidth, float gameHeight, float occurringLength) {
		
		this.dogRunner = DogRunner.getInstance();
		
		//this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		
		this.world = world;
		
		pendingCarWave = true;
		
		//carWaveSpawner = new Array<Spawner>(10);
		
		//betweenWaveSpawner = new Array<Spawner>(10);
		
		//spawnList = new DataPriority[6];
		
		bodiesArray = new Array<Body>(20);
		
		////////////////////////////////
		
		CarSpawner cSp = new CarSpawner(gameWidth, gameHeight);
		//carWaveSpawner.add(cSp);
		
		////////////////////////////////
		
		carWavePos = gameWidth + cSp.getWidth() / 2f;// + gameWidth / 5f;
		betweenWavePos = gameWidth + occurringLength / 2f;// + gameWidth / 5f;
		
		carWaveTime = cSp.getWidth() / VELOCITY;
		
		betweenWaveTime = occurringLength / VELOCITY;
		
		////////////////////////////////
		
		//I mean the first row should spawn in a reasonable time when the game starts
		accumulator = betweenWaveTime;
		
		////////////////////////////////
		//PROGRAMMERS LOOK HERE - #DANKMEMES
		
		carWaveSpawner = new Spawner[]{cSp};
		
		betweenWaveSpawner = new Spawner[]{new GhostSpawner(gameWidth, gameHeight),
				new GasStationSpawner(gameWidth, gameHeight)};
		
		////////////////////////////////
		
		emptySpawnList = new DataPriority[ROWS];
		Arrays.fill(emptySpawnList, new DataPriority(null, 0));
		
		spawnList = new DataPriority[ROWS];
		System.arraycopy(emptySpawnList, 0, spawnList, 0, ROWS);
	}
	
	public void act(float delta) {

		accumulator += delta;
		if (pendingCarWave) {
			
			if (accumulator >= betweenWaveTime) {
				
				requestSpawn(carWaveSpawner);
				
				spawnThem();
				
				System.arraycopy(emptySpawnList, 0, spawnList, 0, ROWS);
				
				accumulator -= betweenWaveTime;
				
				pendingCarWave = !pendingCarWave;
			}
		}
		else {
			
			if (accumulator >= carWaveTime) {
				
				requestSpawn(betweenWaveSpawner);

				spawnThem();
				
				System.arraycopy(emptySpawnList, 0, spawnList, 0, ROWS);
				
				accumulator -= carWaveTime;
				
				pendingCarWave = !pendingCarWave;
			}
		}
		
		Iterator<Body> iterator = bodiesArray.iterator();
		while (iterator.hasNext()) {
			
			Body body = iterator.next();
			SpawnerBodyData data = (SpawnerBodyData) body.getUserData();
			data.spawner.act(data);
			
			if (data.destroy) {
				
				iterator.remove();
				
				world.destroyBody(body);
			}
			else if (body.getPosition().x + data.spawner.getWidth(data) / 2 < 0) {

				iterator.remove();

				data.spawner.onDestroy(data);

				world.destroyBody(body);
			}
		}
	}
	
	/*
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
	*/
	
	public void render() {

		for (Body body : bodiesArray) {

			SpawnerBodyData data = (SpawnerBodyData) body.getUserData();
			Spawner spawner = data.spawner;

			spawner.getDrawable(data).draw(dogRunner.batch, body.getPosition().x - spawner.getWidth(data) / 2f, body.getPosition().y - spawner.getHeight(data) / 2f, spawner.getWidth(data), spawner.getHeight(data));
		}
	}
	
	private void requestSpawn(Spawner[] spawners) {
		
		for (Spawner spawner : spawners) {
			
			DataPriority[] reqSpawnList = spawner.requestSpawnList();
			//System.out.println(Arrays.toString(reqSpawnList));
			for (int i = 0; i < ROWS; i++) {
				
				if (spawnList[i].data == null) {
					
					spawnList[i] = reqSpawnList[i];
				}
				else if (reqSpawnList[i].data != null) {
					
					if (reqSpawnList[i].priority > spawnList[i].priority) {
						
						spawnList[i] = reqSpawnList[i];
					}
					
					//spawnList[i].data = null;
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
				
				bodyDef.type = BodyType.KinematicBody;
				//bodyDef.position.set(gameWidth + (spawner.getWidth(dataPriority.data) - gameWidth * (21f / 500f)) / 2f, ((i) * 2f + 1f) * gameHeight / 12f);
				if (pendingCarWave) {
					
					bodyDef.position.set(carWavePos, ((i) * 2f + 1f) * gameHeight / 12f);
				}
				else {
					
					bodyDef.position.set(betweenWavePos, ((i) * 2f + 1f) * gameHeight / 12f);
				}
				bodyDef.linearVelocity.set(-VELOCITY, 0f);
				
				Body body = world.createBody(bodyDef);
				
				body.createFixture(spawner.getShape(), 0.0f);
				body.setUserData(dataPriority.data);
				
				bodiesArray.add(body);
			}
			
			//spawnList[i].data = null;
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
