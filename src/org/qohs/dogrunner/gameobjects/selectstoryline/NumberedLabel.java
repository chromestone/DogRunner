package org.qohs.dogrunner.gameobjects.selectstoryline;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * 
 * @author Derek Zhang
 *
 */
public class NumberedLabel extends Label {
	
	public final int num;

	public NumberedLabel(CharSequence text, LabelStyle style, int num) {
		
		super(text, style);
		
		this.num = num;
	}

}
