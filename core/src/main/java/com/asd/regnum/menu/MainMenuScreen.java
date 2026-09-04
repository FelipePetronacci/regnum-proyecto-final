package com.asd.regnum.menu;

import com.asd.regnum.GameScreen;
import com.asd.regnum.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {

    final Main game;
    Stage stage;

    Texture backgroundTexture;
    Image backgroundImage;
    BitmapFont font;
    FreeTypeFontGenerator generator;

    boolean isTransitioning = false;
    float zoomTimer = 0f;
    float transitionDuration = 1.0f;

    public MainMenuScreen(final Main game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("menu/menu_fondo.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/determination.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.YELLOW;

        Table table = new Table();
        table.setFillParent(true);
        table.left().bottom();
        table.padLeft(50).padBottom(150);
        stage.addActor(table);

        TextButton playButton = new TextButton("Jugar", textButtonStyle);
        TextButton exitButton = new TextButton("Salir", textButtonStyle);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isTransitioning) {
                    isTransitioning = true;
                    Gdx.input.setInputProcessor(null);
                }
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isTransitioning) {
                    Gdx.app.exit();
                }
            }
        });

        table.add(playButton).padBottom(15).row();
        table.add(exitButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (isTransitioning) {
            zoomTimer += delta;

            float progress = zoomTimer / transitionDuration;
            if (progress > 1f) progress = 1f;

            float targetZoom = 1.0f - (0.5f * progress);
            ((OrthographicCamera) stage.getCamera()).zoom = targetZoom;

            float targetX = 350 + (150 * progress);
            float targetY = 150 + (50 * progress);
            stage.getCamera().position.set(targetX, targetY, 0);

            stage.getCamera().update();

            if (zoomTimer >= transitionDuration) {
                game.setScreen(new GameScreen(game));
                dispose();
                return;
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        backgroundTexture.dispose();
    }
}
