package org.qohs.dogrunner.text.mainsurvival;

import org.qohs.dogrunner.text.TextObject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Displays score; used in game over state of main survival screen
 * 
 * @author Derek Zhang
 *
 */
public class GameOverScore extends TextObject {
	
	private float fontX, fontY;
	private float fontX2, fontY2;
	
	private float scoreFontX, scoreFontY;
	private String scoreText;
	
	public GameOverScore(BitmapFont font) {
		
		super(font);

		////////////////////////////////
		//
		String text = "GAME OVER";

		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX = dogRunner.GAME_WIDTH / 2f - glyphLayout.width / 2f;
		fontY = dogRunner.GAME_HEIGHT / 4f - glyphLayout.height / 2f;

		//float prevWidth = glyphLayout.height;

		text = "SCORE:";
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX2 = dogRunner.GAME_WIDTH / 2f - glyphLayout.width / 2f;
		fontY2 = dogRunner.GAME_HEIGHT / 2f - glyphLayout.height / 2f;

		scoreFontX = -1f;
		scoreFontY = dogRunner.GAME_HEIGHT * 3f / 4f - glyphLayout.height / 2f;
		//scoreText = null;
		
		////////////////////////////////
		
		scoreText = "" + dogRunner.userProfile.score;

		GlyphLayout glyphLayout2 = new GlyphLayout();
		glyphLayout2.setText(font, scoreText);

		scoreFontX = (dogRunner.GAME_WIDTH - glyphLayout2.width) / 2f;
	}

	@Override
	public void render(Batch batch) {

		font.draw(batch, "GAME OVER", fontX, fontY);
		font.draw(batch, "SCORE:", fontX2, fontY2);
		/*
		if (scoreText == null) {

			scoreText = "" + dogRunner.userProfile.score;

			GlyphLayout glyphLayout = new GlyphLayout();
			glyphLayout.setText(font, scoreText);

			scoreFontX = (dogRunner.GAME_WIDTH - glyphLayout.width) / 2f;
		}
		*/
		font.draw(batch, scoreText, scoreFontX, scoreFontY);
	}

}
