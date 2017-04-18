package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Doesn't have to be used for screens, but recommended if
 * you are going to use GameObject classes.
 * Inherit this class if that is the case.
 * 
 * @author Derek Zhang
 *
 */
public abstract class StageScreen implements Screen {
	
	protected final DogRunner dogRunner;
	protected final Stage stage;
	
	StageScreen() {

		dogRunner = DogRunner.getInstance();
		stage = new Stage(
				new FitViewport(dogRunner.GAME_WIDTH, dogRunner.GAME_HEIGHT, dogRunner.cam),
				dogRunner.batch);
	}

	@Override
	public void show() {
		
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		
		//act(delta);
		stage.act(delta);
		stage.draw();
	}
	
	/*/**
	 * Method can be used to simplify the render method
	 * in some subclasses.
	 * @param delta delta time elapsed since last call
	 */
	/*public void act(float delta) {
		
		stage.act(delta);
	}*/

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void hide() {
		
		Gdx.input.setInputProcessor(null);
	}
	
	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		
		stage.dispose();
	}
}
