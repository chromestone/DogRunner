package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.io.DogCustomGraphic;
import org.qohs.dogrunner.util.ProgressBarCustomizer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GasBarManager {

	private final DogRunner dogRunner;
	public final Table container;
	public final ProgressBar gasBar;
	private float beginningFuel;
	
	public GasBarManager() {
		
		dogRunner = DogRunner.getInstance();
		
		float gasY = dogRunner.GAME_HEIGHT / 2f + dogRunner.GAME_HEIGHT / 20f;
		
		container= new Table();
		container.setTransform(true);

		gasBar = new ProgressBar(0f, 1f, .01f, true,
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		ProgressBarCustomizer.customize2(dogRunner, gasBar);
		gasBar.setWidth(gasBar.getPrefWidth());
		gasBar.setHeight(dogRunner.GAME_HEIGHT - gasY);
		gasBar.setFillParent(true);
		
		container.setX(dogRunner.GAME_WIDTH);// - gasBar.getWidth());
		container.setY(gasY);

		container.setWidth(gasBar.getWidth());
		container.setHeight(dogRunner.GAME_HEIGHT - gasY);
		//gasBar.setValue(.5f);
		gasBar.setColor(new Color(1f, 1f, 1f, .25f));
		container.add(gasBar);
		//after intensive debugging, I found this weird trick
		float weird = (gasBar.getHeight() - gasBar.getPrefHeight()) / 2f;
		container.rotateBy(180);
		container.setY(container.getY() + weird + container.getHeight());
		
		beginningFuel = 0f;
	}
	
	public void reset() {
		System.out.println(dogRunner.userProfile.gas);
		beginningFuel = dogRunner.userProfile.gas;
		gasBar.setValue(1f);
	}
	
	public void update() {
		
		gasBar.setValue(dogRunner.userProfile.gas / beginningFuel);
	}
}
