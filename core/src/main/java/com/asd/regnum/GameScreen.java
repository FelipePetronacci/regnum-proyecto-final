package com.asd.regnum;

import com.asd.regnum.enemies.*;
import com.asd.regnum.jugador.Jugador;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Jugador jugador;
    private MapManager mapManager;
    private BitmapFont fuente;
    private FreeTypeFontGenerator generator;

    private final float VIRTUAL_WIDTH = 320f;
    private final float VIRTUAL_HEIGHT = 240f;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/determination.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        fuente = generator.generateFont(parameter);
        generator.dispose();

        batch = new SpriteBatch();
        jugador = new Jugador();
        mapManager = new MapManager();

        int colCentro = 1;
        int filaCentro = 1;

        float roomWidth = 23 * 16f;
        float roomHeight = 15 * 16f;
        float habX = colCentro * roomWidth;
        float habY = filaCentro * roomHeight;

        float spawnX = habX + (roomWidth / 2f);
        float spawnY = habY + (roomHeight / 2f);

        jugador.setX(spawnX);
        jugador.setY(spawnY);
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();
        camera.position.set(
            jugador.getX() + 17f / 2f,
            jugador.getY() + 11f / 2f,
            0
        );
        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        mapManager.dibujarMapa(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        jugador.moverJugador(batch, mapManager.getParedes(), dt);
        jugador.controlarDisparios(viewport, mapManager.getParedes(), dt);
        jugador.dibujarProyectil(batch);
        jugador.dibujarEfectos(batch);
        for (Enemigo enemigo : mapManager.getEnemigos()) {
            enemigo.dibujarVida(batch, fuente);
            enemigo.colisionarBala(jugador.getProyectiles());
            enemigo.chequearVida();
            enemigo.dibujar(batch);
        }
        batch.end();
    }
//openCode
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        mapManager.dispose();
        batch.dispose();
        jugador.dispose();
        fuente.dispose();
    }
}
