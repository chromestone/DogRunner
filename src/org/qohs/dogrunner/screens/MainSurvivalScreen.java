package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.mainsurvival.*;
import org.qohs.dogrunner.io.DogAssets;
import org.qohs.dogrunner.text.TextRenderer;
import org.qohs.dogrunner.text.mainsurvival.ScoreText;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Where the action at
 * 
 * @author Derek Zhang
 *
 */
public class MainSurvivalScreen extends StageScreen {
	
	private OrthographicCamera cam;

	private MainSurvivalWorld physicsWorld;
	
	//100 meters across no matter screen size
	private static final float meterHeight = 100f;
	private final float meterWidth;
	
	private ClickListener upperHandler;
	private ClickListener lowerHandler;
	
	private TextureRegion car;
	private final float carHeight;
	private final float carWidth;
	
	private CarSpawner carSpawner;
	
	private TextRenderer textRenderer;
	
	public MainSurvivalScreen(Batch batch) {
		
		super(batch);
		
		//calculated width
		meterWidth = meterHeight / dogRunner.GAME_HEIGHT * dogRunner.GAME_WIDTH;
		
		//load car texture
		car = new TextureRegion(dogRunner.assetManager.get(DogAssets.FERRARI_CAR.fileName, Texture.class));
		car.flip(false, true);
		
		//fit car into screen
		carHeight = meterHeight / 8;
		carWidth = carHeight / car.getTexture().getHeight() * car.getTexture().getWidth();
		
		//configure screen units
		cam = new OrthographicCamera();
		cam.setToOrtho(true, meterWidth, meterHeight);
		stage.getViewport().setCamera(cam);
		
		//set up text
		textRenderer = new TextRenderer(cam);
		textRenderer.add(new ScoreText(dogRunner.assetManager.get(DogAssets.COMIC_SANS40.fileName, BitmapFont.class)));
	
		int upperBound = (int) ((meterHeight - (meterHeight * .25)) / 2);
		int lowerBound = (int) (upperBound + (meterHeight * .25));
		
		ClickHandler clickHandler = new ClickHandler(0, 0, (int) meterWidth, upperBound);
		upperHandler = (ClickListener) clickHandler.getListeners().get(0);
		
		stage.addActor(clickHandler);
		
		clickHandler = new ClickHandler(0, lowerBound, (int) meterWidth, (int) (meterHeight - lowerBound));
		lowerHandler = (ClickListener) clickHandler.getListeners().get(0);
		
		stage.addActor(clickHandler);
		
		physicsWorld = null;
	}
	
	@Override
	public void show() {
		
		super.show();
		
		//set screen units
		//note that these directly apply to drawing and must be set back once screen changes [this.hide()]
		dogRunner.batch.setProjectionMatrix(cam.combined);
		dogRunner.renderer.setProjectionMatrix(cam.combined);
		
		MainSurvivalWorld.Definition def = new MainSurvivalWorld.Definition();
		def.meterWidth = meterWidth;
		def.meterHeight = meterHeight;
		def.carWidth = carWidth;
		def.carHeight = carHeight;
		
		physicsWorld = new MainSurvivalWorld(new Vector2(0f, 0f), true, def);
		
		carSpawner = new CarSpawner(physicsWorld.world, meterWidth, meterHeight, carWidth * 1.75f);//old values//1.5f//4f
	}
	
	@Override
	public void render(float delta) {
		
		if (physicsWorld.carCrashed) {
			
			dogRunner.assetManager.get(DogAssets.CAR_CRASH_BONG.fileName, Sound.class).play();
			dogRunner.setScreen(DogScreens.Type.GAME_OVER_SCREEN);
			return;
		}
		
		dogRunner.renderer.begin(ShapeType.Filled);
		dogRunner.renderer.setColor(Color.BLUE);
		dogRunner.renderer.rect(0, 0, meterWidth, meterHeight / 3);
		dogRunner.renderer.setColor(Color.WHITE);
		dogRunner.renderer.rect(0, meterHeight / 3, meterWidth, meterHeight / 3);
		dogRunner.renderer.setColor(Color.BLUE);
		dogRunner.renderer.rect(0, meterHeight * 2 / 3, meterWidth, meterHeight / 3);
		dogRunner.renderer.end();
		
		super.render(delta);
		
		//exclusive or
		if (upperHandler.isPressed() ^ lowerHandler.isPressed()) {
			
			if (upperHandler.isPressed()) {
				
				physicsWorld.carBody.setLinearVelocity(0, -120);
			}
			else {
				
				physicsWorld.carBody.setLinearVelocity(0, 120);
			}
		}
		else {
			
			physicsWorld.carBody.setLinearVelocity(0, 0);
		}
		
		physicsWorld.render(delta);

		dogRunner.batch.begin();
		dogRunner.batch.draw(car, physicsWorld.carBody.getPosition().x - carWidth / 2, physicsWorld.carBody.getPosition().y - carHeight / 2, carWidth, carHeight);
		dogRunner.batch.end();
		
		carSpawner.render(delta);
		
		textRenderer.render();
	}

	@Override
	public void hide() {
		
		super.hide();
		
		//set display units back to normal
		dogRunner.batch.setProjectionMatrix(dogRunner.cam.combined);
		dogRunner.renderer.setProjectionMatrix(dogRunner.cam.combined);

		physicsWorld.dispose();
	}
	
	@Override
	public void resume() {
		
		//***not sure if this is true***needs to be tested***
		//on android assets need to be reloaded (after for example exiting)
		//car = new TextureRegion(fairies.assetManager.get(DogAssets.FERRARI_CAR.fileName, Texture.class));
		//car.flip(false, true);
	}
	
	@Override
	public void pause() {
		
	}

	@Override
	public void dispose() {

		physicsWorld.dispose();
	}

}
