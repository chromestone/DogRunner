package org.qohs.dogrunner.gameobjects.start;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogAsset;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * 
 * @author Derek Zhang
 *
 */
public class HighScoreButton extends GameObject {
	
	private Color color;
	private float fontX, fontY;
	private BitmapFont font;
	
	public HighScoreButton(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		color = Color.BLACK;
		
		font = dogRunner.assetManager.get(DogAsset.ARIAL_WHITE_M.FILE_NAME, BitmapFont.class);
		
		//color = font.getColor();
		
		//font.setColor(Color.BLACK);
		
		////////////////////////////////
		//
		String text = "High Score";
		
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX = x + width / 2 - glyphLayout.width / 2;
		fontY = y + height / 2 - glyphLayout.height / 2;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		/*
		batch.end();
		
		dogRunner.renderer.begin(ShapeType.Filled);
		dogRunner.renderer.setColor(color);
		dogRunner.renderer.rect(x, y, width, height);
		dogRunner.renderer.end();

		batch.begin();
		*/
		
		Color origColor = font.getColor();
		
		//batch.setColor(color);
		
		font.setColor(color);

		font.draw(batch, "High Score", fontX, fontY);
		
		font.setColor(origColor);
		
		//batch.setColor(origColor);
	}
	
	public void animateDown() {
		
		color = Color.YELLOW;
	}
	
	public void animateUp() {
		
		color = Color.BLACK;
	}
	
	public void clicked() {
		
		dogRunner.setScreen(DogScreens.Type.HIGH_SCORE_SCREEN);
	}
}
