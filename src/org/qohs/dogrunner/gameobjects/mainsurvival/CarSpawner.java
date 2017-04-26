package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.*;

import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
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
class CarSpawner extends Spawner {
	
	private float carWidth, carHeight;
	
	//previous wave of cars
	private boolean[] prevFormation;
	//current wave of cars
	private boolean[] currentFormation;
	//the open rows of the previous wave (important in ensuring a possible path for player)
	private int[] openRows;
	
	private final Drawable carDrawable, crashedDrawable;
	
	private final PolygonShape shape;
	
	private final BodyDef bodyDef;
	
	private Sound sound;
	private Sound soundWilhelm;
	
	/**
	 * This editor has one method that gets called by the editSpawnList method
	 * each object has the option to pass the job onto another editor
	 */
	private Editor editorHackz;
	
	CarSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion carTexture;
		carTexture = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.WHITE_CAR));
		carTexture.flip(false, true);
		
		carHeight = 7f * gameHeight / 48f;
		carWidth = carHeight / carTexture.getRegionHeight() * carTexture.getRegionWidth();
		
		TextureRegion crashedTexture;
		crashedTexture = new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.EXPLODE));

		prevFormation = new boolean[SpawnManager.ROWS];
		currentFormation = new boolean[SpawnManager.ROWS];
		openRows = new int[SpawnManager.ROWS];
		Arrays.fill(openRows, (byte) -1);
		
		carDrawable = new TextureRegionDrawable(carTexture);
		crashedDrawable = new TextureRegionDrawable(crashedTexture);

			
		shape = new PolygonShape();  
		//it's actually a radius
		shape.setAsBox((carWidth - gameWidth * (21f / 500f)) / 2, carHeight / 2f - 2f);
		
		bodyDef = new BodyDef(); 
		//bodyDef.type = BodyDef.BodyType.KinematicBody;
		
		sound = dogRunner.assetManager.get(DogSound.CAR_CRASH_BONG.FILE_NAME, Sound.class);
		soundWilhelm = dogRunner.assetManager.get(DogSound.WILHELM_SCREAM.FILE_NAME, Sound.class);
		
		editorHackz = new FirstEditor();
	}
	
	@Override
	protected void editSpawnList() {
		
		editorHackz.editSpawnList();
	}
	
	/*
	@Override
	void act(SpawnerBodyData data) {
		
		NPCBodyData npcData = (NPCBodyData) data;
		
		if (	npcData.crashed) {

			if (!npcData.soundPlayed) {

				dogRunner.assetManager.get(DogSound.CAR_CRASH_BONG.FILE_NAME, Sound.class).play();

				npcData.soundPlayed = true;
			}
		}
	}
	*/
	
	@Override
	void onCrash(SpawnerBodyData data) {
		
		if (data.crashed) {
			
			return;
		}
		
		data.crashed = true;
		
		dogRunner.userProfile.lives--;

		if (Math.random() * 2 >= 1) { 
			
			soundWilhelm.play();
		}
		else {

			sound.play();
		}
	}
	
	@Override
	void onDestroy(SpawnerBodyData data) {
		
		//NPCBodyData npcData = (NPCBodyData) data;
		if (!data.crashed) {

			dogRunner.userProfile.score += dogRunner.userProfile.multiplier;
		}
	}

	@Override
	Drawable getDrawable(SpawnerBodyData data) {
		
		//NPCBodyData npcData = (NPCBodyData) data;
		
		if (data.crashed) {
			
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
	 * @return the width of the car
	 */
	float getWidth() {
		
		return carWidth;
	}

	@Override
	float getWidth(SpawnerBodyData data) {
		
		return carWidth;
	}
	
	@Override
	float getHeight(SpawnerBodyData data) {
		
		return carHeight;
	}

	@Override
	Shape getShape() {

		return shape;
	}

	@Override
	BodyDef getBodyDef() {

		return bodyDef;
	}
	
	@Override
	protected void mDispose() {
		
		shape.dispose();
	}
	
	private interface Editor {
		
		void editSpawnList();
	}
	
	private class LaterEditor implements Editor {
		
		private final CarSpawner cS;
		private int counter;
		
		LaterEditor() {
			
			counter = 0;
			cS = CarSpawner.this;
		}
		
		@Override
		public void editSpawnList() {
			
			//the amount of open rows in the previous wave
			//also corresponds to the index in the openRows array
			int openRowCount = 0;
			
			//note it is important to overwrite every index of the currentFormation array
			
			if (Math.random() * 2 >= 1) {
				
				for (counter = 0; counter < 6; counter++) {//counter is "row"

					//if the previous wave of cars did spawn a car at this row
					if (prevFormation[counter]) {

						//1/2 chance of spawning a car
						if (Math.random() * 2 >= 1) {

							currentFormation[counter] = true;
							DataPriority dataPriority = cS.get(counter);
							dataPriority.priority = 10000;
							dataPriority.data = new SpawnerBodyData(cS);//NPCBodyData(cS);
							//carArray.add(createCarBody(row));
						}
						else {

							currentFormation[counter] = false;
							cS.get(counter).data = null;
						}
					}
					//there is an opening in the previous wave
					//this row is considered "open"
					else {

						currentFormation[counter] = false;
						cS.get(counter).data = null;
						openRows[openRowCount++] = counter;
					}
				}
			}
			else {
				
				for (counter = 5; counter >= 0; counter--) {//counter is "row"

					//if the previous wave of cars did spawn a car at this row
					if (prevFormation[counter]) {

						//1/2 chance of spawning a car
						if (Math.random() * 2 >= 1) {

							currentFormation[counter] = true;
							DataPriority dataPriority = cS.get(counter);
							dataPriority.priority = 10000;
							dataPriority.data = new SpawnerBodyData(cS);//new NPCBodyData(cS);
							//carArray.add(createCarBody(row));
						}
						else {

							currentFormation[counter] = false;
							cS.get(counter).data = null;
						}
					}
					//there is an opening in the previous wave
					//this row is considered "open"
					else {

						currentFormation[counter] = false;
						cS.get(counter).data = null;
						openRows[openRowCount++] = counter;
					}
				}
			}
			
			for (counter= 0; counter < openRowCount && openRows[counter] >= 0; counter++) {
				
				//checks if the previous and next row is not accessible to the player (a car or "wall" present)
				boolean previousFilled = (openRows[counter] - 1 < 0) || currentFormation[openRows[counter] - 1];
				boolean nextFilled = (openRows[counter] + 1 >= SpawnManager.ROWS) || currentFormation[openRows[counter] + 1];
				
				//if the player is not blocked both above and below the current row
				if (!previousFilled || !nextFilled) {
					
					//1/2 chance of spawning a car
					if (Math.random() * 2 >= 1) {
						
						currentFormation[openRows[counter]] = true;
						DataPriority dataPriority = cS.get(openRows[counter]);
						dataPriority.priority = 10000;
						dataPriority.data = new SpawnerBodyData(cS);//new NPCBodyData(cS);
						//carArray.add(createCarBody(openRows[i]));
					}
				}
			}

			//copy current wave into the previous wave
			System.arraycopy(currentFormation, 0, prevFormation, 0, SpawnManager.ROWS);
			
			//System.out.println(Arrays.toString(prevFormation));
		}
	}
	
	private class FirstEditor implements Editor {
		
		private final CarSpawner cS;
		private final Editor laterEditor;
		
		FirstEditor() {
			
			laterEditor = new LaterEditor();
			cS = CarSpawner.this;
		}
	
		@Override
		public void editSpawnList() {
			
			ArrayList<Integer> firstFormation = new ArrayList<Integer>();
			for (int i = 0; i < SpawnManager.ROWS; i++) {

				firstFormation.add(i);
			}

			//ensures a uniform distribution (probability) of cars spawning in each row
			Collections.shuffle(firstFormation);

			//leave at least one row empty
			//assuming 6 rows fill randomly 1 to 5 rows (inclusive)
			int rowsFilled = (int) (Math.random() * (SpawnManager.ROWS - 1)) + 1;
			for (int i = 0; i < rowsFilled; i++) {

				int row = firstFormation.get(i);
				prevFormation[row] = true;
				DataPriority dataPriority = cS.get(row);
				dataPriority.priority = 10000;
				dataPriority.data = new SpawnerBodyData(cS);//new NPCBodyData(cS);
				//1/2 chance of spawning a car
				/*if (Math.random() * 2 >= 1) {

					int row = firstFormation.get(i);
					prevFormation[row] = true;
					DataPriority dataPriority = cS.get(i);
					dataPriority.priority = 10000;
					dataPriority.data = new NPCBodyData(cS);
					//carArray.add(createCarBody(row));
				}*/
			}
			
			cS.editorHackz = laterEditor;
		}
	}
}
