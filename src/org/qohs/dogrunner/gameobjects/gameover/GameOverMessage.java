package org.qohs.dogrunner.gameobjects.gameover;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.g2d.*;

/**
 * 
 * @author Derek Zhang
 *
 */
public class GameOverMessage extends GameObject {
	
	private float fontX, fontY;
	private BitmapFont font;
	
	private float scoreFontX, scoreFontY;
	//noticed that user sometimes clicks after exiting previous screen
	//two clicks exits the Game Over screen
	//***consider implementing a button instead***
	private int clicks;
	
	public GameOverMessage(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		font = dogRunner.assetManager.get(DogAssets.COMIC_SANS_BLACK_L.fileName, BitmapFont.class);
		
		////////////////////////////////
		//
		String text = "GAME OVER";

		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX = width / 2 - glyphLayout.width / 2f;
		fontY = height / 3f - glyphLayout.height / 2f;
		
		scoreFontX = -1;
		scoreFontY = height * 2f / 3f - glyphLayout.height / 2f;
		clicks = 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		font.draw(batch, "GAME OVER", fontX, fontY);
		if (scoreFontX < 0) {
			
			String text = "SCORE: " + dogRunner.userProfile.score;

			GlyphLayout glyphLayout = new GlyphLayout();
			glyphLayout.setText(font, text);

			scoreFontX = (width - glyphLayout.width) / 2f;
		}
		font.draw(batch, "SCORE: " + dogRunner.userProfile.score, scoreFontX, scoreFontY);
	}
	
	public void clicked() {
		
		clicks++;
		if (clicks >= 2) {
			
			clicks = 0;
			dogRunner.userProfile.score = 0;
			dogRunner.setScreen(DogScreens.Type.START_SCREEN);
		}
	}
}

