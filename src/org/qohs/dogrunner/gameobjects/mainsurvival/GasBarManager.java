package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.io.DogCustomGraphic;
import org.qohs.dogrunner.util.ProgressBarCustomizer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * 
 * @author Derek Zhang
 *
 */
public class GasBarManager {

	private final DogRunner dogRunner;
	public final Table container;
	public final ProgressBar gasBar;
	private double beginningFuel;
	
	private Drawable green;
	private Drawable yellow;
	private Drawable red;
	
	public GasBarManager() {
		
		dogRunner = DogRunner.getInstance();
		
		float gasY = dogRunner.GAME_HEIGHT / 2f + dogRunner.GAME_HEIGHT * 3f / 40f;
		
		container= new Table();
		container.setTransform(true);

		gasBar = new ProgressBar(0f, 1f, .01f, true,
				dogRunner.assetManager.get(DogCustomGraphic.UI_SKIN.FILE_NAME, Skin.class));
		
		ProgressBarCustomizer.customize2(dogRunner, gasBar);
		gasBar.setStyle(new ProgressBarStyle(gasBar.getStyle()));
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
		
		TextureRegionDrawable d = (new TextureRegionDrawable(
				dogRunner.assetManager.get(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas", TextureAtlas.class)
				.findRegion("white")));
		d.setMinWidth(dogRunner.GAME_HEIGHT / 20f);
		green = d.tint(Color.GREEN);
		yellow = d.tint(Color.YELLOW);
		red = d.tint(Color.RED);
	}
	
	private void setColor(Drawable d) {
		
		gasBar.getStyle().knob = d;
		gasBar.getStyle().knobBefore = d;
	}
	
	public void reset() {
		
		//System.out.println("GBM" + dogRunner.userProfile.gas);
		beginningFuel = dogRunner.userProfile.gas;
		setColor(green);
		gasBar.setValue(1f);
	}
	
	public void update() {
		
		if (dogRunner.userProfile.gas <= 0) {
			
			gasBar.setValue(0f);
			return;
		}
		
		double raw = dogRunner.userProfile.gas / beginningFuel;
		float p = ((int) (raw * 100)) / 100f;
		gasBar.setValue(p);
		if (p <= .75f) {
			
			if (p > .25f) {
				
				setColor(yellow);
			}
			else {
				
				setColor(red);
			}
		}
	}
}
