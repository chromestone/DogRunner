package org.qohs.dogrunner.text;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * 
 * @author Derek Zhang
 *
 */
public abstract class TextObject {

	protected DogRunner dogRunner;
	protected BitmapFont font;
	
	public TextObject(BitmapFont font) {
		
		dogRunner = DogRunner.getInstance();
		this.font = font;
	}
	
	abstract public void render(Batch batch);
}
