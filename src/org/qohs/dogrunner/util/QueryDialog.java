package org.qohs.dogrunner.util;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * 
 * @author Derek Zhang
 *
 */
public class QueryDialog extends Dialog {
	
	/**
	 * Waiting is waiting for response (after show called)
	 * 
	 * 
	 * @author Derek Zhang
	 *
	 */
	public enum State {

		WAITING,
		INPUTTED,
		IDLE;
	}
	
	private State state;
	private String result;

	public QueryDialog(String title, WindowStyle windowStyle) {
		
		super(title, windowStyle);
		
		result = null;
		state = State.IDLE;
	}
	
	@Override
	public Dialog show(Stage stage, Action action) {
		
		state = State.WAITING;
		result = null;
		
		return super.show(stage, action);
	}
	
	public State getState() {
		
		return state;
	}
	
	public String queryResult() {
		
		if (state == State.IDLE) {
			
			return "null";
		}

		state = State.IDLE;
		
		String result = this.result;
		
		this.result = null;
		
		return result;
	}

	@Override
	protected void result(Object result) {

		state = State.INPUTTED;
		this.result = String.valueOf(result);
	}
}
