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

	public ScoreText(BitmapFont font) {
		
		super(font);
	}

	@Override
	public void render(Batch batch) {
		
		String text = "SCORE: " + dogRunner.userProfile.score;

		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		float fontX = dogRunner.GAME_WIDTH - glyphLayout.width ;//width - glyphLayout.width / fairies.GAME_WIDTH * width;
		//float fontY = glyphLayout.height / 2;//height + glyphLayout.height / fairies.GAME_HEIGHT * height;
		
		font.draw(batch, glyphLayout, fontX, 0f);
	}
}
