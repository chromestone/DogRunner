package org.qohs.dogrunner.util;

import com.badlogic.gdx.Input.TextInputListener;

/**
 * Currently not used :(
 * 
 * Note: it is possible to still use the listener after {@link #input(String)} is called.
 * Unless reset is called, there will be no further indication
 * of if {@link #input(String)} is called and {@link #getState()} continues to return
 * {@link State#INPUTTED}
 * 
 * @see com.badlogic.gdx.Input#getTextInput(TextInputListener, String, String, String)
 * 
 * @author Derek Zhang
 *
 */
public class SimpleInputListener implements TextInputListener {

	/**
	 * 
	 * @see SimpleInputListener#getState()
	 * 
	 * @author Derek Zhang
	 *
	 */
	public enum State {

		WAITING,
		/**
		 * Used when the enclosing listener's input method is called
		 * @see SimpleInputListener#input(String)
		 */
		INPUTTED,
		CANCELED;
	}
	
	private State state;
	private String input;
	
	public SimpleInputListener() {
		
		state = State.WAITING;
		input = null;
	}
	
	public State getState() {
		
		return state;
	}
	
	public String getInput() {
		
		return input;
	}
	
	/**
	 * Clears the input and resets the state
	 * back to waiting
	 */
	public void reset() {
		
		state = State.WAITING;
		input = null;
	}
	
	@Override
	public void canceled() {
		
		if (state == State.WAITING) {
			
			state = State.CANCELED;
		}
	}

	@Override
	public void input(String text) {
		
		state = State.INPUTTED;
		input = text;
	}
}
