package org.qohs.dogrunner.gameobjects.mainsurvival;

import java.util.*;

import org.qohs.dogrunner.DogRunner;
import org.qohs.dogrunner.gameobjects.PhysicsWorld;
import org.qohs.dogrunner.io.*;

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
class RoadManager {
	
	private static final float SPEED = 130f;//165f
	
	private DogRunner dogRunner;
	
	private float screenWidth;
	
	private TextureRegion road;
	
	private float roadHeight;
	private float roadWidth;
	
	private float distance;
	
	private Array<Point> tRegionPoints;
	//private Queue<Point> pointPool;
	
	//private float accumulator;
	
	/**
	 * It is assumed that roadWidth and roadHeight are in same units
	 * these units should be consistent with speed
	 * 
	 * @param screenWidth the width of the screen
	 * @param roadHeight the height of the road
	 *///@param speed horizontal velocity of road (units/second)
	RoadManager(float screenWidth, float roadHeight) {//, float speed) {
		
		dogRunner = DogRunner.getInstance();
		
		this.screenWidth = screenWidth;
		
		road = new TextureRegion(dogRunner.assetManager.get(DogTexture.ROAD_IMG.FILE_NAME, Texture.class));
		road.flip(false, true);
		
		this.roadHeight = roadHeight;
		this.roadWidth = roadHeight * road.getRegionWidth() / road.getRegionHeight();
		
		distance = SPEED * PhysicsWorld.STEP_RATE;
		
		road = new TextureRegion(dogRunner.assetManager.get(DogTexture.ROAD_IMG.FILE_NAME, Texture.class));
		road.flip(false, true);
		
		tRegionPoints = new Array<Point>(4);
		
		Point p = new Point(0f, 0f);
		tRegionPoints.add(p);
		addMoreRoad(p);
		
		//accumulator = 0f;
		
		//pointPool = new LinkedList<>();
	}
	
	//public void act(float delta) {
	public void act() {
		
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

				//p.x = 0;
				//p.y = 0;
				//pointPool.add(p);
				iterator.remove();
				p = null;
			}
		}

		if (p == null) {

			p = new Point(0f, 0f);//getPoint();
			tRegionPoints.add(p);
		}

		addMoreRoad(p);
		/*
		if (p.x + roadWidth < screenWidth) {

			//Point newPoint = getPoint();
			//newPoint.x = p.x + roadWidth;
			tRegionPoints.add(new Point(p.x + roadWidth, 0f));
		}
		*/
	}
	
	public void render() {
		
		//dogRunner.batch.begin();
		
		Iterator<Point> iterator = tRegionPoints.iterator();
		Point p;
		while (iterator.hasNext()) {
			
			p = iterator.next();
			dogRunner.batch.draw(road, p.x, p.y, roadWidth, roadHeight);
		}
		
		//dogRunner.batch.end();
	}
	
	private void addMoreRoad(Point p) {
		
		while (p.x + roadWidth < screenWidth) {

			//Point newPoint = getPoint();
			//newPoint.x = p.x + roadWidth;
			tRegionPoints.add(p = new Point(p.x + roadWidth, 0f));
		}
	}
	
	/*
	private Point getPoint() {
		
		System.out.println(pointPool.size());
		Point p = pointPool.poll();
		
		if (p == null) {
			
			return new Point(0f, 0f);
		}
		
		return p;
	}
	*/

	private static class Point {
		
		float x, y;
		
		Point(float x, float y) {
			
			this.x = x;
			this.y = y;
		}
	}
}
