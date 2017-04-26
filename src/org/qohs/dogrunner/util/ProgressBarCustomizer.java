package org.qohs.dogrunner.util;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * 
 * @author Derek Zhang
 *
 */
public class ProgressBarCustomizer {

	/**
	 * Note that the progress bar doesn't actually need be passed in
	 * however NULL VALUES NOT ACCEPTED
	 * 
	 * This is because the progress bar acts as a gate way to mutate the progress
	 * bar style for ALL progress bars using the default UI skin
	 * 
	 * @param dogRunner the current dog runner instance
	 * @param progressBar the progress bar to act on
	 * @return
	 */
	public static ProgressBar customize1(DogRunner dogRunner, ProgressBar progressBar) {
		
		TextureRegionDrawable d;
		
		d = (new TextureRegionDrawable(
						dogRunner.assetManager.get(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas", TextureAtlas.class)
						.findRegion("white")));//"default-round-large")));
		d.setMinHeight(dogRunner.GAME_HEIGHT / 20f);
		progressBar.getStyle().background.setMinHeight(d.getMinHeight());
		//progressBar.getStyle().background.setMinWidth(d.getMinWidth());
		
		progressBar.getStyle().knob = d.tint(Color.GREEN);
		progressBar.getStyle().knobBefore = progressBar.getStyle().knob;
		progressBar.getStyle().knobAfter = null;

		return progressBar;
	}
	
	public static ProgressBar customize2(DogRunner dogRunner, ProgressBar progressBar) {
		
		TextureRegionDrawable d;
		
		d = (new TextureRegionDrawable(
						dogRunner.assetManager.get(DogRunner.PARENT_DIR + "uiskin/uiskin.atlas", TextureAtlas.class)
						.findRegion("white")));//"default-round-large")));
		d.setMinWidth(dogRunner.GAME_HEIGHT / 20f);
		progressBar.getStyle().background = d;
		//progressBar.getStyle().background.setMinWidth(d.getMinWidth());
		
		progressBar.getStyle().knob = d.tint(Color.GREEN);
		progressBar.getStyle().knobBefore = progressBar.getStyle().knob;
		progressBar.getStyle().knobAfter = null;

		return progressBar;
	}
}
