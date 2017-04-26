package org.qohs.dogrunner.gameobjects.clicker;

import java.util.Iterator;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.io.DogFont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * @author Derek Zhang
 *
 */
public class PlusNumberManager {
	
	//the time it takes for the font's alpha to become 0 or completely transparent
	private static final float PRIMARY_FADE_TIME = 2f;
	
	private final DogRunner dogRunner;
	
	private Array<PlusNumber> numbers;
	private BitmapFont font;

	public PlusNumberManager() {
		
		dogRunner = DogRunner.getInstance();
		
		numbers = new Array<PlusNumber>(20);
		
		font = dogRunner.assetManager.get(DogFont.WHITE_M_BD.FILE_NAME, BitmapFont.class);
	}
	
	void requestPlusNumber(float x, float y, int addition) {
		
		numbers.add(new PlusNumber(x, y, addition));
	}
	
	public void render(float delta) {
		
		Color color = dogRunner.batch.getColor();
		
		Iterator<PlusNumber> iterator = numbers.iterator();
		
		while (iterator.hasNext()) {
			
			PlusNumber pN = iterator.next();
			

			pN.y -= (dogRunner.GAME_HEIGHT / 2f) * delta / PRIMARY_FADE_TIME;
			pN.fadeAction.act(delta);
			
			//remove this object once it's off the screen, or barely visible
			if (pN.y <= 0 || pN.fadeAction.getColor().a <= 0) {

				iterator.remove();
			}
			else {
				
				font.setColor(pN.fadeAction.getColor());
				font.draw(dogRunner.batch, pN.plusString, pN.x, pN.y);
			}
		}
		
		dogRunner.batch.setColor(color);
	}

	private static class PlusNumber {
		
		//the text displayed which represents this "plus number"
		final String plusString;
		//the x coordinate of the plus number on the screen
		float x;
		//the y coordinate of the plus number on the screen
		float y;
		//fades the "plus number"
		AlphaAction fadeAction;
		
		PlusNumber(float x, float y, int addition) {
			
			plusString = "+" + addition;
			this.x = x;
			this.y = y;
			fadeAction = Actions.fadeOut(PRIMARY_FADE_TIME);
			fadeAction.setColor(Color.WHITE.cpy());
		}
	}
}
