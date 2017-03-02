package org.qohs.dogrunner.text.mainsurvival;

import org.qohs.dogrunner.text.TextObject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Displays score for main survival screen.
 * 
 * @author Derek Zhang
 *
 */
public class ScoreText extends TextObject {
	
	private GlyphLayout glyphLayout;
	
	public ScoreText(BitmapFont font) {
		
		super(font);
		glyphLayout = new GlyphLayout();
	}

	@Override
	public void render(Batch batch) {
		
		String text = "SCORE: " + dogRunner.userProfile.score;

		//glyphLayout.reset();
		glyphLayout.setText(font, text);

		//float fontX = width - glyphLayout.width / dogRunner.GAME_WIDTH * width;
		//float fontY = glyphLayout.height / 2;//height + glyphLayout.height / dogRunner.GAME_HEIGHT * height;
		
		font.draw(batch, glyphLayout, dogRunner.GAME_WIDTH - glyphLayout.width, 0f);
	}
}
