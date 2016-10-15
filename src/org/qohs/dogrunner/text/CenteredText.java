package org.qohs.dogrunner.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Displays a centered text that can be set externally
 * 
 * @author Derek Zhang
 *
 */
public class CenteredText extends TextObject {
	
	public String text;

	public CenteredText(BitmapFont font) {
		
		super(font);
		
		text = "";
	}

	@Override
	public void render(Batch batch) {

		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		font.draw(batch, glyphLayout, (dogRunner.GAME_WIDTH - glyphLayout.width) / 2f, (dogRunner.GAME_HEIGHT - glyphLayout.height) / 2f);
	}
}
