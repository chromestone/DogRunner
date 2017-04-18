package org.qohs.dogrunner.gameobjects.start;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;

/**
 * 
 * @author Derek Zhang
 *
 */
public class StartButton extends GameObject {
	
	private Color color;
	private float fontX, fontY;
	private BitmapFont font;
	
	public StartButton(float x, float y, float width, float height) {
		
		super(x, y, width, height);
		
		color = Color.BLACK;
		
		font = dogRunner.assetManager.get(DogFont.WHITE_M.FILE_NAME, BitmapFont.class);
		
		//color = font.getColor();
		
		//font.setColor(Color.BLACK);
		
		////////////////////////////////
		//
		String text = "Start";
		
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

		font.draw(batch, "Start", fontX, fontY);
		
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
		
		font.setColor(color);
		//dogRunner.userProfile.score = 0;
		dogRunner.userProfile.reset();
		dogRunner.userProfile.inGame = true;
		dogRunner.assetManager.get(DogMusic.START_THEME.FILE_NAME, Music.class).stop();
		dogRunner.setScreen(DogScreens.Type.STORYLINE_SCREEN);
	}
}
