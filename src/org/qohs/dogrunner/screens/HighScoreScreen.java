package org.qohs.dogrunner.screens;

import java.util.Iterator;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.*;
import org.qohs.dogrunner.util.HighScore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

/**
 * 
 * @author Derek Zhang
 *
 */
public class HighScoreScreen extends StageScreen {
	
	private Label title;
	
	private LabelStyle labelStyle;
	private LabelStyle labelStyle2;
	private QueryButton continueButton;

	public HighScoreScreen() {
		
		super();
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("square.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		BitmapFont font = dogRunner.assetManager.get(DogFont.ARIAL_YELLOW_L.FILE_NAME, BitmapFont.class);
		GlyphLayout glyphLayout = new GlyphLayout(font, "A");
		parameter.size = (int) (dogRunner.GAME_HEIGHT / (HighScore.MAX_HIGH_SCORES + 1.0) * glyphLayout.width / glyphLayout.height);
		parameter.color = Color.WHITE;
		parameter.borderColor = Color.BLACK;
		parameter.flip = true;
		//parameter.borderWidth = 1f;
		//parameter.borderColor = Color.WHITE;
		BitmapFont theFont = generator.generateFont(parameter);
		generator.dispose();
		////////////////////////////////
		
		labelStyle = new LabelStyle();
		labelStyle.font = theFont;
		
		////////////////////////////////

		title = new Label("High Scores", labelStyle);
		title.setX(dogRunner.GAME_WIDTH / 2f - title.getPrefWidth() / 2f);
		title.setY(0f);
		Color color = new Color(Color.GOLD);
		color.a = .9f;
		title.setColor(color);
		
		stage.addActor(title);
		
		////////////////////////////////

		continueButton = new QueryButton(dogRunner.GAME_WIDTH - title.getPrefHeight(), 0f, title.getPrefHeight(), title.getPrefHeight(), new TextureRegion(dogRunner.getAtlasRegion(DogAtlasRegion.RESUME_IMG)));
		
		stage.addActor(continueButton);
		
		////////////////////////////////

		labelStyle2 = new LabelStyle(labelStyle);
		labelStyle2.fontColor = Color.WHITE;
	}
	
	@Override
	public void show() {
		
		super.show();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		stage.addActor(title);
		stage.addActor(continueButton);
		
		final int height = dogRunner.GAME_HEIGHT / (HighScore.MAX_HIGH_SCORES + 1);
		int drawHeight = height;
		
		Color color = new Color(Color.CYAN);
		color.a = .50f;
		
		Iterator<String> it2 = dogRunner.highScoreFM.highScore.names.iterator();
		while (it2.hasNext() && drawHeight < dogRunner.GAME_HEIGHT) {
			
			Label label = new Label(it2.next(), labelStyle);
			
			if (label.getWidth() > dogRunner.GAME_WIDTH / 2f) {

				label.setFontScale(dogRunner.GAME_WIDTH / 2f / label.getWidth());
			}
			
			label.setX(0f);
			label.setY(drawHeight);
			
			label.setColor(color);
			
			stage.addActor(label);
			
			drawHeight += height;
		}
		
		drawHeight = height;
		
		color = new Color(Color.GOLD);
		color.a = .9f;
		
		Iterator<Integer> it = dogRunner.highScoreFM.highScore.scores.iterator();
		while (it.hasNext() && drawHeight < dogRunner.GAME_HEIGHT) {
			
			Label label = new Label(it.next().toString(), labelStyle2);
			
			if (label.getWidth() > dogRunner.GAME_WIDTH / 2f) {

				label.setFontScale(dogRunner.GAME_WIDTH / 2f / label.getWidth());
			}
			
			label.setX(dogRunner.GAME_WIDTH - label.getPrefWidth());
			label.setY(drawHeight);
			
			label.setColor(color);
			
			stage.addActor(label);
			
			drawHeight += height;
		}
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (continueButton.queryClicked()) {
			
			dogRunner.setScreen(DogScreens.Type.START_SCREEN);
			//return;
		}
	}
	
	@Override
	public void hide() {
		
		super.hide();
		
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		
		stage.clear();
	}
	
	@Override
	public void dispose() {
		
		super.dispose();
		
		labelStyle.font.dispose();
	}
}
