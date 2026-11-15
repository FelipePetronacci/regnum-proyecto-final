package com.asd.regnum.menu;

import com.asd.regnum.GameScreen;
import com.asd.regnum.Main;
import com.asd.regnum.gestores.GestorBD;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PantallaMuerte extends ScreenAdapter {

    private Main game;
    private Stage stage;
    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont fuente;
    private Texture backgroundTexture;
    private GestorBD bd;

    public PantallaMuerte(Main game, int nivel, String tiempoJugado) {
        this.bd = new GestorBD();
        this.game = game;
        this.viewport = new FitViewport(320, 240, new OrthographicCamera());
        this.batch = new SpriteBatch();
        this.stage = new Stage(viewport, batch);
        bd.conectar();

        backgroundTexture = new Texture("menu/menu_muerte.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/determination.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 10;
        parameter.color = Color.WHITE;
        fuente = generator.generateFont(parameter);
        generator.dispose();

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = fuente;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.YELLOW;

        TextButton btnReiniciar = new TextButton("Volver a empezar", buttonStyle);
        TextButton btnMenu = new TextButton("Menu principal", buttonStyle);
        TextButton btnSalir = new TextButton("Salir", buttonStyle);

        TextField textField = new TextField("", skin);
        textField.setMaxLength(7);
        textField.setMessageText("Nombre");

        textField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField tf, char character) {
                if (character == '\n' || character == '\r') {
                    String nombreIngresado = tf.getText();
                    bd.cargarPartida(nivel, nombreIngresado,tiempoJugado );
                    if (!nombreIngresado.trim().isEmpty()) {

                        tf.setText("");
                    }
                }
            }
        });

        btnReiniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        btnSalir.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        table.padBottom(25);

        table.add(btnReiniciar).padBottom(4).row();
        table.add(btnMenu).padBottom(4).row();
        table.add(btnSalir).padBottom(4).row();
        table.add(textField).width(75).height(15);

        stage.addActor(table);

        stage.setKeyboardFocus(textField);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        fuente.dispose();
        backgroundTexture.dispose();
    }
}
