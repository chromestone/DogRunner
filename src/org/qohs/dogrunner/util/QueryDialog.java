package org.qohs.dogrunner.util;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class QueryDialog extends Dialog {
	
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
	public Dialog show(Stage stage) {
		
		state = State.WAITING;
		result = null;
		
		return super.show(stage);
	}
	
	public State getState() {
		
		return state;
	}
	
	public String queryResult() {
		
		if (state == State.WAITING) {
			
			return "null";
		}
		
		state = State.IDLE;
		
		String result = this.result;
		
		this.result = null;
		
		return result;
	}

	@Override
	protected void result(Object result) {
		System.out.println(result);
		state = State.INPUTTED;
		result = String.valueOf(result);
	}
}
