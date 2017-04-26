package org.qohs.dogrunner.gameobjects.clicker;

import org.qohs.dogrunner.gameobjects.*;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * 
 * @author Derek Zhang
 *
 */
public class Donut extends GameObject {
	
	private int index;
	private TextureRegion donutTexture;
	
	//regular dimensions (allows for padding)
	private float m_x, m_y, m_width, m_height;
	//down dimensions
	private float md_x, md_y, md_width, md_height;
	
	private boolean down;
	
	private PlusNumberManager manager;

	public Donut(float x, float y, float width, float height, PlusNumberManager manager) {
		
		super(x, y, width, height);
		
		index = 0;
		donutTexture = dogRunner.gudrunTRegionFM.donutThings.get(index);
		calcDraw();
		
		down = false;
		
		this.removeListener(super.listener);
		this.addListener(new MyClickListener());
		
		this.manager = manager;
	}
	
	private void calcDraw() {
		
		float origWidth, origHeight;
		
		origWidth = height * donutTexture.getRegionWidth() / donutTexture.getRegionHeight();
		origHeight = height;
		
		m_width = origWidth * 9f / 10f;
		m_height = origHeight * 9f / 10f;
		
		m_x = (origWidth - m_width) / 2f;
		m_y = (origHeight - m_height) / 2f;
		
		md_width = m_width * 9f / 10f;
		md_height = m_height * 9f / 10f;
		
		md_x = (origWidth - md_width) / 2f;
		md_y = (origHeight - md_height) / 2f;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {

		if (!down) {
			
			batch.draw(donutTexture, m_x, m_y, m_width, m_height);
		}
		else {
			
			batch.draw(donutTexture, md_x, md_y, md_width, md_height);
		}
	}
	
	private class MyClickListener extends ClickListener {
		
		int clicks = 0;
		
		@Override
		public void clicked(InputEvent event, float x, float y) {

			int addition = (index + 1) * dogRunner.userProfile.gasStops;
			
			manager.requestPlusNumber(x + (int) (Math.random() * (width / 10)) - width / 10, y, addition);
			
			dogRunner.userProfile.gas += addition;
			
			clicks++;
			
			if (clicks < 10) {
				
				return;
			}
			
			if (index < 2) {
				
				index = (int) (Math.random() * dogRunner.gudrunTRegionFM.donutThings.size());
			}
			else {
				
				if (Math.random() * 3 < 2) {
					
					index = 0;
				}
				else {
					
					index = 1;
				}
			}
			
			donutTexture = dogRunner.gudrunTRegionFM.donutThings.get(index);
			calcDraw();
			
			clicks = 0;
		}
		
		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

			super.exit(event, x, y, pointer, toActor);
			down = false;
		}
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

			super.touchDown(event, x, y, pointer, button);
			down = true;
			return true;
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			super.touchUp(event, x, y, pointer, button);
			down = false;
		}
	}
}
