package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.*;

import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * This class spawns the cars in the Main Survival World
 * Note that this class should be used in accordance
 * and inside a physics world (see act and clean)
 * 
 * @author Derek Zhang
 *
 */
public class CarSpawner extends Spawner {
	
	private TextureRegion carTexture;
	private float carWidth, carHeight;
	
	private TextureRegion crashedTexture;
	
	//previous wave of cars
	private boolean[] prevFormation;
	//current wave of cars
	private boolean[] currentFormation;
	//the open rows of the previous wave (important in ensuring a possible path for player)
	private byte[] openRows;
	
	private final Drawable carDrawable, crashedDrawable;
	
	private boolean firstEditCall;
	
	private final PolygonShape shape;
	
	private final BodyDef bodyDef;
	
	public CarSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		carTexture = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.WHITE_CAR));
		carTexture.flip(false, true);
		
		carHeight = 7f * gameHeight / 48f;
		carWidth = carHeight / carTexture.getRegionHeight() * carTexture.getRegionWidth();
		
		crashedTexture = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.EXPLODE));

		prevFormation = new boolean[SpawnManager.ROWS];
		currentFormation = new boolean[SpawnManager.ROWS];
		openRows = new byte[6];
		Arrays.fill(openRows, (byte) -1);
		
		firstEditCall = true;
		
		carDrawable = new TextureRegionDrawable(carTexture);
		crashedDrawable = new TextureRegionDrawable(crashedTexture);

			
		shape = new PolygonShape();  
		//it's actually a radius
		shape.setAsBox((carWidth - gameWidth * (21f / 500f)) / 2, carHeight / 2f - 2f);
		
		bodyDef = new BodyDef(); 
		bodyDef.type = BodyType.DynamicBody;
	}
	
	@Override
	protected void editSpawnList() {
		
		if (firstEditCall) {
			
			ArrayList<Byte> firstFormation = new ArrayList<Byte>();
			for (byte i = 0; i < SpawnManager.ROWS; i++) {
				
				firstFormation.add(i);
			}
			
			//ensures a uniform distribution (probability) of cars spawning in each row
			Collections.shuffle(firstFormation);
			
			//leave at least one row empty
			//assuming 6 rows fill randomly 1 to 5 rows (inclusive)
			int rowsFilled = (int) (Math.random() * (SpawnManager.ROWS - 2)) + 1;
			for (int i = 0; i < rowsFilled; i++) {
				
				//1/2 chance of spawning a car
				if (Math.random() * 2 >= 1) {
					
					byte row = firstFormation.get(i);
					prevFormation[row] = true;
					DataPriority dataPriority = super.get(i);
					dataPriority.priority = 10_000;
					dataPriority.data = new NPCBodyData(PhysicsBodyType.NPC_CAR, this);
					//carArray.add(createCarBody(row));
				}
			}
			
			firstEditCall = false;
			
			return;
		}
		
		//the amount of open rows in the previous wave
		//also corresponds to the index in the openRows array
		int openRowCount = 0;
		
		//note it is important to overwrite every index of the currentFormation array
		
		if (Math.random() * 2 >= 1) {
			
			for (byte row = 0; row < 6; row++) {

				//if the previous wave of cars did spawn a car at this row
				if (prevFormation[row]) {

					//1/2 chance of spawning a car
					if (Math.random() * 2 >= 1) {

						currentFormation[row] = true;
						DataPriority dataPriority = super.get(row);
						dataPriority.priority = 10_000;
						dataPriority.data = new NPCBodyData(PhysicsBodyType.NPC_CAR, this);
						//carArray.add(createCarBody(row));
					}
					else {

						currentFormation[row] = false;
					}
				}
				//there is an opening in the previous wave
				//this row is considered "open"
				else {

					currentFormation[row] = false;
					openRows[openRowCount++] = row;
				}
			}
		}
		else {
			
			for (byte row = 5; row >= 0; row--) {

				//if the previous wave of cars did spawn a car at this row
				if (prevFormation[row]) {

					//1/2 chance of spawning a car
					if (Math.random() * 2 >= 1) {

						currentFormation[row] = true;
						DataPriority dataPriority = super.get(row);
						dataPriority.priority = 10_000;
						dataPriority.data = new NPCBodyData(PhysicsBodyType.NPC_CAR, this);
						//carArray.add(createCarBody(row));
					}
					else {

						currentFormation[row] = false;
					}
				}
				//there is an opening in the previous wave
				//this row is considered "open"
				else {

					currentFormation[row] = false;
					openRows[openRowCount++] = row;
				}
			}
		}
		
		for (byte i = 0; i < openRowCount && openRows[i] >= 0; i++) {
			
			//checks if the previous and next row is not accessible to the player (a car or "wall" present)
			boolean previousFilled = (openRows[i] - 1 < 0) || currentFormation[openRows[i] - 1];
			boolean nextFilled = (openRows[i] + 1 >= 6) || currentFormation[openRows[i] + 1];
			
			//if the player is not blocked both above and below the current row
			if (!previousFilled || !nextFilled) {
				
				//1/2 chance of spawning a car
				if (Math.random() * 2 >= 1) {
					
					currentFormation[openRows[i]] = true;
					DataPriority dataPriority = super.get(openRows[i]);
					dataPriority.priority = 10_000;
					dataPriority.data = new NPCBodyData(PhysicsBodyType.NPC_CAR, this);
					//carArray.add(createCarBody(openRows[i]));
				}
			}
		}

		//copy current wave into the previous wave
		System.arraycopy(currentFormation, 0, prevFormation, 0, 6);
	}
	
	@Override
	public void act(SpawnerBodyData data) {
		
		NPCBodyData npcData = (NPCBodyData) data;
		
		if (	npcData.crashed) {

			if (!npcData.soundPlayed) {

				dogRunner.assetManager.get(DogSound.CAR_CRASH_BONG.FILE_NAME, Sound.class).play();

				npcData.soundPlayed = true;
			}
		}
	}
	
	@Override
	public void onDestroy(SpawnerBodyData data) {
		
		NPCBodyData npcData = (NPCBodyData) data;
		if (!npcData.crashed) {

			dogRunner.userProfile.score += 1;
		}
	}

	@Override
	public Drawable getDrawable(SpawnerBodyData data) {
		
		NPCBodyData npcData = (NPCBodyData) data;
		
		if (npcData.crashed) {
			
			return crashedDrawable;
		}
		else {
			
			return carDrawable;
		}
	}
	
	/**
	 * This class is "special" so it has this
	 * overloaded method
	 * 
	 * @return
	 */
	public float getWidth() {
		
		return carWidth;
	}

	@Override
	public float getWidth(SpawnerBodyData data) {
		
		return carWidth;
	}
	
	@Override
	public float getHeight(SpawnerBodyData data) {
		
		return carHeight;
	}

	@Override
	public Shape getShape() {

		return shape;
	}

	@Override
	public BodyDef getBodyDef() {

		return bodyDef;
	}
	
	@Override
	protected void mDispose() {
		
		shape.dispose();
	}
}
