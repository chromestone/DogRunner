package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.Iterator;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.io.DogAssets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Moves the road to give the illusion that the player's
 * car is moving forward
 * 
 * @author Derek Zhang
 *
 */
public class RoadManager {
	
	private DogRunner dogRunner;
	
	private float screenWidth;
	
	private TextureRegion road;
	
	private float roadHeight;
	private float roadWidth;
	
	private float distance;
	
	private Array<Point> tRegionPoints;
	
	//private float accumulator;
	
	/**
	 * It is assumed that roadWidth and roadHeight are in same units
	 * these units should be consistent with speed
	 * 
	 * @param screenWidth
	 * @param roadHeight
	 * @param speed horizontal velocity of road (units/second)
	 */
	public RoadManager(float screenWidth, float roadHeight, float speed) {
		
		dogRunner = DogRunner.getInstance();
		
		this.screenWidth = screenWidth;
		
		road = new TextureRegion(dogRunner.assetManager.get(DogAssets.ROAD_IMG.FILE_NAME, Texture.class));
		road.flip(false, true);
		
		this.roadHeight = roadHeight;
		this.roadWidth = roadHeight * road.getTexture().getWidth() / road.getTexture().getHeight();
		
		distance = speed / 60f;
		
		road = new TextureRegion(dogRunner.assetManager.get(DogAssets.ROAD_IMG.FILE_NAME, Texture.class));
		road.flip(false, true);
		
		tRegionPoints = new Array<Point>(4);
		
		tRegionPoints.add(new Point(0f, 0f));
		
		//accumulator = 0f;
	}
	
	public void act(float delta) {
		
		/*
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		//float temp = accumulator % 1/60f;
		
		float totalDist = 0f;
		while (accumulator >= 1/60f) {
			
			totalDist += distance;
			accumulator -= 1/60f;
		}
		*/
		
		//accumulator = temp;

		Iterator<Point> iterator = tRegionPoints.iterator();
		Point p = null;
		while (iterator.hasNext()) {

			p = iterator.next();
			p.x -= distance;
			if (p.x + roadWidth <= 0) {

				iterator.remove();
				p = null;
			}
		}

		if (p == null) {

			tRegionPoints.add(p = new Point(0f, 0f));
		}

		if (p.x + roadWidth < screenWidth) {

			tRegionPoints.add(new Point(p.x + roadWidth, 0f));
		}
	}
	
	public void render() {
		
		dogRunner.batch.begin();
		
		Iterator<Point> iterator = tRegionPoints.iterator();
		Point p = null;
		while (iterator.hasNext()) {
			
			p = iterator.next();
			dogRunner.batch.draw(road, p.x, p.y, roadWidth, roadHeight);
		}
		
		dogRunner.batch.end();
	}

	private class Point {
		
		public float x, y;
		
		public Point(float x, float y) {
			
			this.x = x;
			this.y = y;
		}
	}
}
