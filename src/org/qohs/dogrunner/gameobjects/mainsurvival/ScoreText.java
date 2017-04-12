package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogFont;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class ScoreText extends GameObject {

	private BitmapFont font;
	private GlyphLayout glyphLayout;
	private int previous;
	
	public ScoreText(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		font = dogRunner.assetManager.get(DogFont.RED_M.FILE_NAME, BitmapFont.class);
		
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, "SCORE: " + dogRunner.userProfile.score);
		previous = dogRunner.userProfile.score;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		String text = "SCORE: " + dogRunner.userProfile.score;

		//glyphLayout.reset();
		if (previous != dogRunner.userProfile.score) {
			
			glyphLayout.setText(font, text);
		}
		
		previous = dogRunner.userProfile.score;

		//float fontX = width - glyphLayout.width / dogRunner.GAME_WIDTH * width;
		//float fontY = glyphLayout.height / 2;//height + glyphLayout.height / dogRunner.GAME_HEIGHT * height;
		
		font.draw(batch, glyphLayout, dogRunner.GAME_WIDTH - glyphLayout.width, 0f);
	}

}
