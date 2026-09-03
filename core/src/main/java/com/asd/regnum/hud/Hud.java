package com.asd.regnum.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    private Stage stage;
    private Viewport viewport;
    private BitmapFont font;

    private Table heartTable;
    private Table debugTable;
    private Label debugLabel;

    private boolean showDebug = false;
    private boolean f3WasPressed = debounceF3();

    private final Color COLOR_BORDO = new Color(0.55f, 0.0f, 0.0f, 1f);

    public Hud(SpriteBatch batch) {
        viewport = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(com.badlogic.gdx.Gdx.files.internal("fuentes/consola.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.WHITE;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "♥♡";
        font = generator.generateFont(parameter);
        generator.dispose();


        heartTable = new Table();
        heartTable.top().left();
        heartTable.setFillParent(true);
        heartTable.pad(20);
        stage.addActor(heartTable);


        debugTable = new Table();
        debugTable.top().right();
        debugTable.setFillParent(true);
        debugTable.pad(20);

        Label.LabelStyle debugStyle = new Label.LabelStyle(font, Color.WHITE);
        debugLabel = new Label("", debugStyle);
        debugTable.add(debugLabel);

        stage.addActor(debugTable);
        debugTable.setVisible(false);
    }

    public void update(float playerX, float playerY, int enemigosSize) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            showDebug = !showDebug;
            debugTable.setVisible(showDebug);
        }

        if (showDebug) {
            int fps = Gdx.graphics.getFramesPerSecond();
            String debugText = String.format("DEBUG MENU\nFPS: %d\nPOS X: %.2f\nPOS Y: %.2f\nEnemigosTotales: %d", fps, playerX, playerY, enemigosSize);
            debugLabel.setText(debugText);
        }
    }

    public void updateHealth(int currentHp) {
        heartTable.clearChildren();

        Label.LabelStyle whiteStyle = new Label.LabelStyle(font, Color.WHITE);
        Label.LabelStyle bordoStyle = new Label.LabelStyle(font, COLOR_BORDO);

        Table row1 = new Table();
        for (int i = 0; i < 2; i++) {
            boolean active = i < currentHp;
            row1.add(new Label("[", whiteStyle));
            row1.add(new Label(active ? "♥" : "♡", bordoStyle));
            row1.add(new Label("] ", whiteStyle));
        }
        heartTable.add(row1).row();

        Table row2 = new Table();
        boolean activeBottom = currentHp > 2;
        row2.add(new Label("[", whiteStyle));
        row2.add(new Label(activeBottom ? "♥" : "♡", bordoStyle));
        row2.add(new Label("]", whiteStyle));

        heartTable.add(row2).left().padLeft(22);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    private boolean debounceF3() {
        return false;
    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }
}
