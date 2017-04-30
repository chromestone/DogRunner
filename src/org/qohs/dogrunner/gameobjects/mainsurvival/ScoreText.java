package org.qohs.dogrunner.gameobjects.mainsurvival;

import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogFont;
import org.qohs.dogrunner.io.DogTexture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Actually more than just score (multiplier related to score)
 * 
 * @author Derek Zhang
 *
 */
public class ScoreText extends GameObject {
	
	private static final String TEXT = " SCORE: ";

	private BitmapFont font;
	private GlyphLayout glyphLayout;
	private long previous;
	
	private TextureRegion paw;
	private GlyphLayout multiplierGlyph;
	private byte previousMult;
	
	public ScoreText(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		font = dogRunner.assetManager.get(DogFont.RED_M.FILE_NAME, BitmapFont.class);
		
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, TEXT + dogRunner.userProfile.score);
		previous = dogRunner.userProfile.score;
		
		paw = new TextureRegion(dogRunner.assetManager.get(DogTexture.PAW.FILE_NAME, Texture.class));
		paw.flip(false, true);
		
		multiplierGlyph = new GlyphLayout();
		multiplierGlyph.setText(font, Byte.toString(dogRunner.userProfile.multiplier));
		previousMult = dogRunner.userProfile.multiplier;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		//glyphLayout.reset();
		if (previous != dogRunner.userProfile.score) {
			
			//String text = "SCORE: " + dogRunner.userProfile.score;
			glyphLayout.setText(font, TEXT + dogRunner.userProfile.score);
		}
		
		previous = dogRunner.userProfile.score;

		//float fontX = width - glyphLayout.width / dogRunner.GAME_WIDTH * width;
		//float fontY = glyphLayout.height / 2;//height + glyphLayout.height / dogRunner.GAME_HEIGHT * height;
		
		font.draw(batch, glyphLayout, dogRunner.GAME_WIDTH - glyphLayout.width, 0f);
		
		Color color = batch.getColor();
		batch.setColor(Color.RED);
		batch.draw(paw, dogRunner.GAME_WIDTH - glyphLayout.width - glyphLayout.height, 0f, glyphLayout.height, glyphLayout.height);
		batch.setColor(color);
		
		//glyphLayout.reset();
		if (previousMult != dogRunner.userProfile.multiplier) {
			
			//String text = "SCORE: " + dogRunner.userProfile.score;
			multiplierGlyph.setText(font, Byte.toString(dogRunner.userProfile.multiplier));
		}
		
		previousMult = dogRunner.userProfile.multiplier;
		
		font.draw(batch, multiplierGlyph, dogRunner.GAME_WIDTH - glyphLayout.width - glyphLayout.height - multiplierGlyph.width, 0f);

	}
}
