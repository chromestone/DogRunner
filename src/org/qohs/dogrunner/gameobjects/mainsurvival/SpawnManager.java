package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.Arrays;
import java.util.Iterator;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
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
	 * @param gameWidth
	 * @param gameHeight
	 * @param occuringLength a synonym for between wave length (should be a function of player car length)
	 */
	public SpawnManager(MainSurvivalWorld world, float gameWidth, float gameHeight, float occuringLength) {
		
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
		betweenWavePos = gameWidth + occuringLength / 2f;// + gameWidth / 5f;
		
		carWaveTime = cSp.getWidth() / VELOCITY;
		
		betweenWaveTime = occuringLength / VELOCITY;
		////////////////////////////////

		//betweenWaveSpawner.add(null);
		////////////////////////////////
		
		//I mean the first row should spawn in a reasonable time when the game starts
		accumulator = betweenWaveTime;
		
		//JAMES, LOOK HERE
		carWaveSpawner = new Spawner[]{cSp};
		
		betweenWaveSpawner = new Spawner[]{new GasStationSpawner(gameWidth, gameHeight)};
		
		//spawnList = Arrays.copyOf(cSp.requestSpawnList(), ROWS);
		
		//spawnThem();
		
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
			Spawner spawner = data.spawner;
			
			spawner.getDrawable(data).draw(dogRunner.batch, body.getPosition().x - spawner.getWidth(data) / 2f, body.getPosition().y - spawner.getHeight(data) / 2f,  spawner.getWidth(data), spawner.getHeight(data));
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
