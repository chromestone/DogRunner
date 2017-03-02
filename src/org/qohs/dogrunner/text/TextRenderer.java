package org.qohs.dogrunner.text;

import org.qohs.dogrunner.DogRunner;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;

/**
 * This is the renderer that draws text for the package.
 * Note that this is a convenience package and should  be used when
 * physics world is used in a stage
 * since bitmap fonts only work in pixels and physics units are not always pixels.
 * However, this class may be used regardless.
 *  
 * @author Derek Zhang
 *
 */
public class TextRenderer {
	
	private DogRunner dogRunner;
	private Camera originalCam;
	private Array<TextObject> textObjs;
	
	public TextRenderer(Camera originalCam) {
		
		dogRunner = DogRunner.getInstance();
		this.originalCam = originalCam;
		this.textObjs = new Array<TextObject>(4);
	}
	
	public void add(TextObject textObj) {
		
		textObjs.add(textObj);
	}
	
	/**
	 * Note that currently only exact memory location (==) is used for comparison
	 * future implementations will include option of .equals()
	 * 
	 * @param textObj the text object to be removed
	 * @return whether or not if the object was successfully removed
	 */
	public boolean remove(TextObject textObj) {
		
		return textObjs.removeValue(textObj, true);
	}

	public void render() {
		
		dogRunner.batch.setProjectionMatrix(dogRunner.cam.combined);
		dogRunner.batch.begin();
		for (TextObject textObj : textObjs) {
			
			textObj.render(dogRunner.batch);
		}
		dogRunner.batch.end();
		dogRunner.batch.setProjectionMatrix(originalCam.combined);
	}
}
