package org.qohs.dogrunner.screens;

import org.qohs.dogrunner.gameobjects.clicker.Donut;
import org.qohs.dogrunner.io.DogFont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

/**
 * 
 * @author Derek Zhang
 *
 */
public class ClickerScreen extends StageScreen {

	public ClickerScreen() {
		
		super();
		
		TextureRegion tr = dogRunner.gudrunTRegionFM.donutThings.get(3);
		
		float origWidth, origHeight;
		
		origWidth = dogRunner.GAME_HEIGHT * tr.getRegionWidth() /tr.getRegionHeight();
		origHeight = dogRunner.GAME_HEIGHT;
		
		float newWidth = origWidth * 9f / 10f;
		float newHeight = origHeight * 9f / 10f;
		
		Donut donut = new Donut((origWidth - newWidth) / 2f, (origHeight - newHeight) /2f, newWidth, newHeight, tr);
		stage.addActor(donut);
		
		LabelStyle lStyle = new LabelStyle();
		lStyle.font =  dogRunner.assetManager.get(DogFont.WHITE_S.FILE_NAME, BitmapFont.class);
		lStyle.fontColor = Color.BLACK;
		Label label = new Label("Click to replenish health.", lStyle);
		label.setX(dogRunner.GAME_WIDTH - label.getWidth());
		label.setY(0);
		stage.addActor(label);
	}

	@Override
	public void show() {
		
		super.show();
		
		dogRunner.userProfile.lives = 3;
	}
}
