package com.asd.regnum;

import com.asd.regnum.enums.EnumMusica;
import com.asd.regnum.gestores.GestorDeMusica;
import com.asd.regnum.gestores.GestorDeSonidos;
import com.asd.regnum.hud.Hud;
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
    private BitmapFont fuente;
    private FreeTypeFontGenerator generator;

    private GestorDeMusica gestorDeMusica;
    private GestorDeSonidos gestorDeSonidos;
    private Hud hud;

    private Mundo mundo; // Nuestra clase de lógica

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
        gestorDeMusica = new GestorDeMusica();
        gestorDeSonidos = new GestorDeSonidos();

        mundo = new Mundo(gestorDeSonidos);

        hud = new Hud(batch);
        gestorDeMusica.reproducirMusica(EnumMusica.DISTANT);
    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();

        mundo.actualizar(dt, viewport);

        // 2. ACTUALIZAR CÁMARA Y HUD
        camera.position.set(
            mundo.getJugador().getX() + 17f / 2f,
            mundo.getJugador().getY() + 11f / 2f,
            0
        );
        camera.update();
        hud.update(mundo.getJugador().getX(), mundo.getJugador().getY(), mundo.getMapManager().getEnemigos().size());
        hud.updateHealth(mundo.getJugador().getVida());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        mundo.getMapManager().dibujarMapa(camera);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        mundo.getJugador().dibujar(batch);
        mundo.getJugador().dibujarProyectil(batch);
        mundo.getJugador().dibujarEfectos(batch);

        for (var enemigo : mundo.getMapManager().getEnemigos()) {
            enemigo.dibujarVida(batch, fuente);
            enemigo.dibujar(batch);
        }
        batch.end();

        hud.render();
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        fuente.dispose();
        gestorDeMusica.dispose();
        gestorDeSonidos.dispose();
        hud.dispose();
        mundo.dispose();
    }
}
