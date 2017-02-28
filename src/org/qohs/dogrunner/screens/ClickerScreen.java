package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.clicker.Donut;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * 
 * @author Derek Zhang
 *
 */
public class ClickerScreen extends StageScreen {

	public ClickerScreen() {
		
		super();
		
		TextureRegion tr = dogRunner.gudrunThingFM.donutThings.get(2);
		
		float origWidth, origHeight;
		
		origWidth = dogRunner.GAME_HEIGHT * tr.getRegionWidth() /tr.getRegionHeight();
		origHeight = dogRunner.GAME_HEIGHT;
		
		float newWidth = origWidth * 9f / 10f;
		float newHeight = origHeight * 9f / 10f;
		
		Donut donut = new Donut((origWidth - newWidth) / 2f, (origHeight - newHeight) /2f, newWidth, newHeight, tr);
		stage.addActor(donut);
		
		
		
	}

}
