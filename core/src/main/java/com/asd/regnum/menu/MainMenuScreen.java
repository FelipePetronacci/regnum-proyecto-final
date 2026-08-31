package com.asd.regnum.menu;

import com.asd.regnum.GameScreen;
import com.asd.regnum.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenuScreen implements Screen {

    final Main game;
    Stage stage;

    private Texture backgroundTexture;
    private Image backgroundImage;
    private Texture buttonTexture;
    private Texture buttonPressedTexture;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;

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
        parameter.size = 16;
        font = generator.generateFont(parameter);
        generator.dispose();
        buttonTexture = createSolidTexture(new Color(0.1f, 0.1f, 0.15f, 0.9f)); // Fondo oscuro semi-opaco
        buttonPressedTexture = createSolidTexture(Color.DARK_GRAY);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = new TextureRegionDrawable(buttonTexture);
        textButtonStyle.down = new TextureRegionDrawable(buttonPressedTexture);
        textButtonStyle.fontColor = Color.WHITE;

        Table table = new Table();
        table.setFillParent(true);
        table.left().bottom();
        table.padLeft(50).padBottom(50);

        stage.addActor(table);

        TextButton playButton = new TextButton("Jugar", textButtonStyle);
        TextButton exitButton = new TextButton("Salir", textButtonStyle);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen());
                dispose();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(playButton).size(180, 45).padBottom(15).row();
        table.add(exitButton).size(180, 45);
    }

    private Texture createSolidTexture(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
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
        buttonTexture.dispose();
        buttonPressedTexture.dispose();
        backgroundTexture.dispose();
    }
}
