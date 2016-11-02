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
	private float fontX2, fontY2;
	private BitmapFont font;
	
	private float scoreFontX, scoreFontY;
	private String scoreText;
	
	//noticed that user sometimes clicks after exiting previous screen
	//two clicks exits the Game Over screen
	//***consider implementing a button instead***
	private int clicks;
	
	public GameOverMessage(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		font = dogRunner.assetManager.get(DogAssets.ARIAL_BLACK_L.FILE_NAME, BitmapFont.class);
		
		////////////////////////////////
		//
		String text = "GAME OVER";

		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX = width / 2f - glyphLayout.width / 2f;
		fontY = height / 4f - glyphLayout.height / 2f;

		//float prevWidth = glyphLayout.height;

		text = "SCORE:";
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX2 = width / 2f - glyphLayout.width / 2f;
		fontY2 = height / 2f - glyphLayout.height / 2f;
		
		scoreFontX = -1f;
		scoreFontY = height * 3f / 4f - glyphLayout.height / 2f;
		scoreText = null;
		
		clicks = 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		font.draw(batch, "GAME OVER", fontX, fontY);
		font.draw(batch, "SCORE:", fontX2, fontY2);
		if (scoreText == null) {
			
			scoreText = "" + dogRunner.userProfile.score;

			GlyphLayout glyphLayout = new GlyphLayout();
			glyphLayout.setText(font, scoreText);

			scoreFontX = (width - glyphLayout.width) / 2f;
		}
		font.draw(batch, scoreText, scoreFontX, scoreFontY);
	}
	
	public void clicked() {
		
		clicks++;
		if (clicks >= 2) {
			
			scoreText = null;
			clicks = 0;
			dogRunner.userProfile.score = 0;
			dogRunner.setScreen(DogScreens.Type.START_SCREEN);
		}
	}
}

