package org.qohs.dogrunner.screens;

import java.util.Iterator;

import org.qohs.dogrunner.DogScreens;
import org.qohs.dogrunner.gameobjects.QueryButton;
import org.qohs.dogrunner.io.DogAsset;
import org.qohs.dogrunner.io.DogTexture;
import org.qohs.dogrunner.util.HighScore;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

	public HighScoreScreen(Batch batch) {
		
		super(batch);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Arial.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		BitmapFont font = dogRunner.assetManager.get(DogAsset.ARIAL_YELLOW_L.FILE_NAME, BitmapFont.class);
		GlyphLayout glyphLayout = new GlyphLayout(font, "A");
		parameter.size = (int) (dogRunner.GAME_HEIGHT / (HighScore.MAX_HIGH_SCORES + 1.0) * glyphLayout.width / glyphLayout.height);
		parameter.color = Color.YELLOW;
		parameter.borderColor = Color.BLACK;
		parameter.flip = true;
		BitmapFont theFont = generator.generateFont(parameter);
		generator.dispose();
		
		labelStyle = new LabelStyle();
		labelStyle.font = theFont;

		title = new Label("High Scores", labelStyle);
		title.setX(dogRunner.GAME_WIDTH / 2f - title.getPrefWidth() / 2f);
		title.setY(0f);
		
		stage.addActor(title);
		
		continueButton = new QueryButton(dogRunner.GAME_WIDTH - title.getPrefHeight(), 0f, title.getPrefHeight(), title.getPrefHeight(), new TextureRegion(dogRunner.assetManager.get(DogTexture.RESUME_IMG.FILE_NAME, Texture.class)));
		
		stage.addActor(continueButton);
		
		labelStyle2 = new LabelStyle(labelStyle);
		labelStyle2.fontColor = Color.GREEN;
	}
	
	@Override
	public void show() {
		
		super.show();
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		stage.addActor(title);
		stage.addActor(continueButton);
		
		final int height = dogRunner.GAME_HEIGHT / (HighScore.MAX_HIGH_SCORES + 1);
		int drawHeight = height;
		
		Iterator<String> it2 = dogRunner.highScoreFM.highScore.names.iterator();
		while (it2.hasNext() && drawHeight < dogRunner.GAME_HEIGHT) {
			
			Label label = new Label(it2.next(), labelStyle);
			label.setX(0f);
			label.setY(drawHeight);
			
			stage.addActor(label);
			
			drawHeight += height;
		}
		
		drawHeight = height;
		
		Iterator<Integer> it = dogRunner.highScoreFM.highScore.scores.iterator();
		while (it.hasNext() && drawHeight < dogRunner.GAME_HEIGHT) {
			
			Label label = new Label(it.next().toString(), labelStyle2);
			label.setX(dogRunner.GAME_WIDTH - label.getPrefWidth());
			label.setY(drawHeight);
			
			stage.addActor(label);
			
			drawHeight += height;
		}
	}
	
	@Override
	public void render(float delta) {
		
		super.render(delta);
		
		if (continueButton.queryClicked()) {
			
			dogRunner.setScreen(DogScreens.Type.START_SCREEN);
			return;
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
