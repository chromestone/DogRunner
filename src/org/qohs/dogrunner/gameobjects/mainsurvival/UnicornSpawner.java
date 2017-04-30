package org.qohs.dogrunner.gameobjects.mainsurvival;

//import org.qohs.dogrunner.io.DogSound;
import org.qohs.dogrunner.io.DogSound;
import org.qohs.dogrunner.io.DogTexture;





import com.badlogic.gdx.audio.Sound;
//import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 * @author Gudrun T.
 * 
 */
class UnicornSpawner extends Spawner {
	
	private final static int PRIORITY = 90;
	
	private final TextureRegionDrawable textureDrawable;
	private final TextureRegionDrawable butterflyTexture;
	
	private final PolygonShape shape;
	
	private final BodyDef bodydef;
	private final float width, height;
	
	//private Sound sound;
	
	private int waveCount;
	
	private Sound neighing;
	
	UnicornSpawner(float gameWidth, float gameHeight) {
		
		super(gameWidth, gameHeight);
		
		TextureRegion textureRegion;
		textureRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.UNICORN.FILE_NAME, Texture.class));
		textureRegion.flip(false, true);
		textureDrawable = new TextureRegionDrawable(textureRegion);
		
		textureRegion = new TextureRegion(dogRunner.assetManager.get(DogTexture.BUTTERFLY.FILE_NAME, Texture.class));
		textureRegion.flip(false, true);
		butterflyTexture = new TextureRegionDrawable(textureRegion);
		
		bodydef = new BodyDef(); 
		//bodydef.type = BodyDef.BodyType.DynamicBody;
		
	    height = 7f * gameHeight / 48f;
	    width = height * textureRegion.getRegionWidth() / textureRegion.getRegionHeight();

		//shape
		shape = new PolygonShape();
		shape.setAsBox(width / 2f, height / 2f);
		
		neighing = dogRunner.assetManager.get(DogSound.NEIGH.FILE_NAME, Sound.class);
		
		waveCount = 0;
		
		//dogRunner.assetManager.get(DogSound.MULTIPLIER_SOUND.FILE_NAME, Sound.class).setVolume(1, 1);
		
	}

	@Override
	protected void editSpawnList() {
		
		super.get(2).data = null;
		super.get(3).data = null;

		waveCount += 1;

		if (waveCount > 19) {//19

			DataPriority data = super.get((int)(Math.random()*2) + 2);
			data.priority = PRIORITY;
			data.data = new SpawnerBodyData(this);//PhysicsBodyType.UNICORN, this);
			waveCount = 0;
		}
		else if (Math.random() * 30 < 1) {

			DataPriority data = super.get((int)(Math.random()*2) + 2);
			data.priority = PRIORITY;
			data.data = new SpawnerBodyData(this);//PhysicsBodyType.UNICORN, this);
		}
		
		/*
		super.get(3).data = null;
		super.get(4).data = null;
		
		if (dogRunner.userProfile.invincible == 0) {

			waveCount += 1;
			
			if (waveCount > 37) {
				
				System.out.println("Score: "+ dogRunner.userProfile.score);
				DataPriority data = super.get((int)(Math.random()*2) + 3);
				data.priority = 9999;
				data.data = new SpawnerBodyData(PhysicsBodyType.MULTIPLIER, this);
				waveCount = 0;
			}
			else if (((int) (Math.random() * 30)) == 0) {
				
				DataPriority data = super.get((int)(Math.random()*2) + 3);
				data.priority = 9999;
				data.data = new SpawnerBodyData(PhysicsBodyType.MULTIPLIER, this);
			}
		}
		else {
			
			waveCount = 0;
		}
		*/
	}
	
	@Override
	void onCrash(SpawnerBodyData data) {
		
		if (data.crashed) {
			
			return;
		}
		
		data.crashed = true;
		
		dogRunner.userProfile.score += 44;
		
		neighing.play();
	}

	@Override
	Drawable getDrawable(SpawnerBodyData data) {

		if(data.crashed){
			
			return butterflyTexture;
		}
		return textureDrawable;
	}

	@Override
	float getWidth(SpawnerBodyData data) {
		
		return width;
	}

	@Override
	float getHeight(SpawnerBodyData data) {
		
		return height;
	}

	@Override
	BodyDef getBodyDef() {

		return bodydef;
	}

	@Override
	Shape getShape() {

		return shape;
	}

	@Override
	protected void mDispose() {

		shape.dispose();
	}
}
