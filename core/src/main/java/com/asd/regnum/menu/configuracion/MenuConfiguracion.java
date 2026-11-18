
package com.asd.regnum.menu.configuracion;

import com.asd.regnum.Main;
import com.asd.regnum.menu.MainMenuScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuConfiguracion implements Screen {

    final Main game;

    Stage stage;

    BitmapFont font;
    BitmapFont titleFont;

    Texture backgroundTexture;
    Skin skin;

    Preferences preferencias;

    public MenuConfiguracion(Main game) {

        this.game = game;
        this.stage = new Stage(new FitViewport(800, 480));
        preferencias = Gdx.app.getPreferences("RegnumConfig");
        backgroundTexture = new Texture(
            Gdx.files.internal("menu/menu_config.png")
        );

        backgroundTexture.setFilter(
            Texture.TextureFilter.Nearest,
            Texture.TextureFilter.Nearest
        );

        Image background = new Image(backgroundTexture);

        background.setSize(800, 480);
        background.setPosition(0, 0);
        background.setScaling(Scaling.stretch);

        stage.addActor(background);

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(
                Gdx.files.internal("fuentes/determination.ttf")
            );

        FreeTypeFontGenerator.FreeTypeFontParameter titleParam =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        titleParam.size = 28;
        titleParam.color = Color.YELLOW;

        titleFont = generator.generateFont(titleParam);

        FreeTypeFontGenerator.FreeTypeFontParameter param =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 18;
        param.color = Color.WHITE;

        font = generator.generateFont(param);

        TextButton.TextButtonStyle textButtonStyle =
            new TextButton.TextButtonStyle();

        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.YELLOW;

        generator.dispose();

        Label.LabelStyle titleStyle =
            new Label.LabelStyle(titleFont, Color.YELLOW);

        skin = new Skin(
            Gdx.files.internal("skin/uiskin.json")
        );

        Table mainTable = new Table();

        mainTable.setFillParent(true);
        mainTable.top().padTop(40);

        stage.addActor(mainTable);

        mainTable.add(
            new Label("Configuracion", titleStyle)
        ).padBottom(20).row();


        Table table = new Table();

        table.setFillParent(true);
        table.left().bottom();

        table.padLeft(50);
        table.padBottom(150);

        stage.addActor(table);

        CheckBox vsync = new CheckBox("", skin);

        vsync.getImageCell().size(40f, 40f);
        vsync.getImage().setScaling(Scaling.stretch);

        boolean vsyncActivado =
            preferencias.getBoolean("vsync", true);

        vsync.setChecked(vsyncActivado);

        Gdx.graphics.setVSync(vsyncActivado);

        TextButton vsyncButton =
            new TextButton("Activar Vsync", textButtonStyle);

        vsync.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                boolean activado = vsync.isChecked();

                Gdx.graphics.setVSync(activado);

                preferencias.putBoolean("vsync", activado);
                preferencias.flush();
            }
        });

        vsyncButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                vsync.setChecked(!vsync.isChecked());
            }
        });

        TextButton bdConfig =
            new TextButton("Ajustes de Ranking", textButtonStyle);

        TextButton exitButton =
            new TextButton("Salir", textButtonStyle);

        exitButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new MainMenuScreen(game));

                dispose();
            }
        });

        bdConfig.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setScreen(new ConfiguracionBD(game));

                dispose();
            }
        });

        Table vsyncTable = new Table();

        vsyncTable.add(vsync);

        vsyncTable.add(vsyncButton)
            .padLeft(10);

        table.add(vsyncTable)
            .center()
            .colspan(2)
            .padBottom(15)
            .row();

        table.add(bdConfig)
            .center()
            .colspan(2)
            .padBottom(15)
            .row();

        table.add(exitButton)
            .center()
            .colspan(2)
            .padBottom(15)
            .row();
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(
            Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)
        );

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        stage.dispose();

        backgroundTexture.dispose();

        skin.dispose();

        font.dispose();

        titleFont.dispose();
    }
}
