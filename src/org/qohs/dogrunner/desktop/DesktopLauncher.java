package org.qohs.dogrunner.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.qohs.dogrunner.DogRunner;

/**
 * 
 * @author unascribed
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Dog Runner";
		config.width = 800;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new DogRunner(), config);
	}
}
