package org.qohs.dogrunner.gameobjects.start;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogFont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * 
 * @author Derek Zhang
 *
 */
public class OptionsButton extends GameObject {
	
	private Color color;
	private float fontX, fontY;
	private BitmapFont font;
	
	public OptionsButton(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		color = Color.FOREST;
		
		font = dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		
		//color = font.getColor();
		
		//font.setColor(Color.BLACK);
		
		////////////////////////////////
		//
		String text = "Options";
		
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX = x;// + width / 2 - glyphLayout.width / 2;
		fontY = y + height / 2 - glyphLayout.height / 2;
		
		this.setWidth(glyphLayout.width);
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

		font.draw(batch, "Options", fontX, fontY);
		
		font.setColor(origColor);
		
		//batch.setColor(origColor);
	}
	
	public void animateDown() {
		
		color = Color.YELLOW;
	}
	
	public void animateUp() {
		
		color = Color.FOREST;
	}
	
	public void clicked() {
		
		dogRunner.setScreen(DogScreens.Type.OPTIONS_SCREEN);
	}
}
