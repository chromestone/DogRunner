package org.qohs.dogrunner.gameobjects;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The Game Object class
 * Meant to be used as an actor for the stage screens
 * 
 * @author Derek Zhang
 *
 */
public abstract class GameObject extends Actor {
	
	protected DogRunner dogRunner;
	protected float x, y;
	protected float width, height;
	
	public GameObject(float x, float y, float width, float height) {
		
		super();
		
		dogRunner = DogRunner.getInstance();
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		this.addListener(new MyClickListener());
	}
	
	/**
	 * When the player clicks down on this object.
	 */
	public void animateDown() {
		
	}
	
	/**
	 * When the player clicks up on this object.
	 * (releases finger/mouse)
	 */
	public void animateUp() {
		
	}
	
	/**
	 * Called before animateUp().
	 */
	public void clicked() {
		
	}
	
	/*
	/**
	 * Called to check if a click is on this object (before anything else).
	 * If the game object is not a rectangle, then override this method.
	 * @param x
	 * @param y
	 * @return
	 *//*
	public boolean collision(float x, float y) {
		
		return true;
	}*/
	
	private class MyClickListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y) {

			GameObject.this.clicked();
		}
		
		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

			super.exit(event, x, y, pointer, toActor);
			GameObject.this.animateUp();
		}
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

			super.touchDown(event, x, y, pointer, button);
			GameObject.this.animateDown();
			return true;
		}
		
		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

			super.touchUp(event, x, y, pointer, button);
			GameObject.this.animateUp();
		}
	}
}
