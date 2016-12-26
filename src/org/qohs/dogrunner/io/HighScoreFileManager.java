package org.qohs.dogrunner.io;

import java.util.Iterator;
import java.util.LinkedList;

import org.qohs.dogrunner.util.HighScore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * loads and saves high score while holding onto a high score object
 * 
 * @author Derek Zhang
 *
 */
public class HighScoreFileManager implements FileManager {

	public HighScore highScore = null;
	
	@Override
	public void load() {
		
		FileHandle file = Gdx.files.local(FileManager.PARENT_DIR + "highscores.txt");
		LinkedList<String> names = new LinkedList<String>();
		LinkedList<Integer> scores = new LinkedList<Integer>();
		String text = file.readString();//.replace("\n", "");
		String[] strArr = text.split(";");
		for (String nameScore : strArr) {

			String[] arr = nameScore.split(":");
			if (arr.length == 2) {
				
				names.add(arr[0].trim());
				scores.add(Integer.valueOf(arr[1]));
			}
		}
		highScore = new HighScore(names, scores);
	}
	
	@Override
	public void save() {
		
		if (!highScore.isModified() && (highScore.names.isEmpty() || highScore.scores.isEmpty())) {
			
			return;
		}
		
		try {
			
			StringBuilder stringBuilder = new StringBuilder();
			char colon = ':', semiColon = ';';

			Iterator<String> it1 = highScore.names.iterator();
			Iterator<Integer> it2 = highScore.scores.iterator();
			while (it1.hasNext() && it2.hasNext()) {

				stringBuilder.append(it1.next());
				stringBuilder.append(colon);
				stringBuilder.append(it2.next());
				stringBuilder.append(semiColon);
			}
			
			FileHandle file = Gdx.files.local(FileManager.PARENT_DIR + "highscores.txt");
			file.writeString(stringBuilder.toString(), false);
		}
		catch (Exception e) {
			
			Gdx.app.log("DogRunner-chromestone", e.getMessage());
		}
	}
}
