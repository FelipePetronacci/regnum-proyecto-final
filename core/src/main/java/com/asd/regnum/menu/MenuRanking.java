package com.asd.regnum.menu;

import com.asd.regnum.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuRanking implements Screen {

    final Main game;
    Stage stage;

    BitmapFont font;
    BitmapFont titleFont;
    FreeTypeFontGenerator generator;

    public MenuRanking(final Main game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(800, 480));
        Gdx.input.setInputProcessor(stage);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/determination.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 28;
        titleParam.color = Color.YELLOW;
        titleFont = generator.generateFont(titleParam);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 18;
        param.color = Color.WHITE;
        font = generator.generateFont(param);

        generator.dispose();

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.YELLOW);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.YELLOW;

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top().padTop(40);
        stage.addActor(mainTable);


        mainTable.add(new Label("--- RANKING DE JUGADORES ---", titleStyle)).padBottom(20).row();

        Table listTable = new Table();

        cargarDatosRanking(listTable, labelStyle);

        mainTable.add(listTable).expand().top().row();

        Table buttonTable = new Table();
        buttonTable.setFillParent(true);
        buttonTable.right().bottom();
        buttonTable.padRight(50).padBottom(40);
        stage.addActor(buttonTable);

        TextButton backButton = new TextButton("Volver", textButtonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        buttonTable.add(backButton);
    }

    private void cargarDatosRanking(Table listTable, Label.LabelStyle style) {
        listTable.add(new Label("NOMBRE", style)).padRight(30);
        listTable.add(new Label("PARTIDAS", style)).padRight(30);
        listTable.add(new Label("NIVEL MAX", style)).padRight(30);
        listTable.add(new Label("MEJOR TIEMPO", style)).row();

        listTable.add(new Label("-----------------------------------------------------------------------", style)).colspan(4).row();

        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:regnum_data.db");
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                     "SELECT u.Nombre, u.PartidasJugadas, MAX(p.Nivel) AS NivelMaximo, MIN(p.Tiempo) AS MejorTiempo " +
                         "FROM Usuarios u " +
                         "JOIN Partidas p ON u.idUsuario = p.idUsuario " +
                         "GROUP BY u.idUsuario, u.Nombre, u.PartidasJugadas " +
                         "ORDER BY NivelMaximo DESC, MejorTiempo ASC " +
                         "LIMIT 8;"
                 )) {

                boolean hayRegistros = false;
                while (rs.next()) {
                    hayRegistros = true;
                    String nombre = rs.getString("Nombre");
                    String partidas = String.valueOf(rs.getInt("PartidasJugadas"));
                    String nivelMax = String.valueOf(rs.getInt("NivelMaximo"));
                    String tiempo = rs.getString("MejorTiempo");
                    if (tiempo == null) tiempo = "-";

                    listTable.add(new Label(nombre, style)).padTop(8).padRight(30);
                    listTable.add(new Label(partidas, style)).padTop(8).padRight(30);
                    listTable.add(new Label(nivelMax, style)).padTop(8).padRight(30);
                    listTable.add(new Label(tiempo, style)).padTop(8).row();
                }

                if (!hayRegistros) {
                    listTable.add(new Label("No hay jugadores registrados aun.", style)).colspan(4).padTop(20).row();
                }

            }
        } catch (Exception e) {
            listTable.add(new Label("Error al cargar la base de datos.", style)).colspan(4).padTop(20).row();
            e.printStackTrace();
        }
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
        titleFont.dispose();
    }
}
