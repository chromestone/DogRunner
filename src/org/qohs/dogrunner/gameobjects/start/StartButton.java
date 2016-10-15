package org.qohs.dogrunner.gameobjects.start;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.GameObject;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

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
		
		color = Color.GRAY;
		
		font = dogRunner.assetManager.get(DogAssets.COMIC_SANS_RED_M.fileName, BitmapFont.class);
		
		////////////////////////////////
		//
		String text = "START";
		
		GlyphLayout glyphLayout = new GlyphLayout();
		glyphLayout.setText(font, text);

		fontX = x + width / 2 - glyphLayout.width / 2;
		fontY = y + height / 2 - glyphLayout.height / 2;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.end();
		
		dogRunner.renderer.begin(ShapeType.Filled);
		dogRunner.renderer.setColor(color);
		dogRunner.renderer.rect(x, y, width, height);
		dogRunner.renderer.end();

		batch.begin();

		font.draw(batch, "START", fontX, fontY);
	}
	
	public void animateDown() {
		
		color = Color.DARK_GRAY;
	}
	
	public void animateUp() {
		
		color = Color.GRAY;
	}
	
	public void clicked() {
		
		dogRunner.assetManager.get(DogAssets.ENGINE_REV.fileName, Sound.class).play(1f);
		dogRunner.setScreen(DogScreens.Type.MAIN_SURVIVE_SCREEN);
	}
}
