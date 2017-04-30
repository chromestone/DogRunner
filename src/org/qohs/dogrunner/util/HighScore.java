package org.qohs.dogrunner.util;

import java.util.*;

/**
 * 
 * @author Derek Zhang
 *
 */
public class HighScore {
	
	public static int MAX_HIGH_SCORES = 5;

	public LinkedList<String> names;
	public LinkedList<Long> scores;
	
	private boolean modified;
	
	public HighScore(LinkedList<String> names, LinkedList<Long> score) {
		
		this.names = names;
		this.scores = score;
		modified = false;
	}
	
	public boolean isHighScore(Long score) {
		
		return scores.size() < MAX_HIGH_SCORES || getScoreIndex(score) != -1;
	}
	
	private int getScoreIndex(Long score) {
		
		int i = 0;
		for (; i < scores.size(); i++) {
			
			if (score > scores.get(i)) {
				
				return i;
			}
		}
		
		if (i <= MAX_HIGH_SCORES - 1) {
			
			return i;
		}
		
		return -1;
	}

	public boolean updateScores(String name, Long score) {

		modified = true;

		int index = getScoreIndex(score);
		if (index != -1) {

			names.add(index, name);
			scores.add(index, score);
			if (scores.size() > MAX_HIGH_SCORES) {

				names.removeLast();
				scores.removeLast();
			}
			return true;
		}
		
		return false;
	}
	
	public boolean isModified() {
		
		return modified;
	}
}
