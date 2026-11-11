package com.asd.regnum;

import com.asd.regnum.enemies.Enemigo;
import com.asd.regnum.enums.EnumMusica;
import com.asd.regnum.gestores.GestorDeMusica;
import com.asd.regnum.gestores.GestorDeSonidos;
import com.asd.regnum.hud.Hud;
import com.asd.regnum.items.Item;
import com.asd.regnum.jugador.Jugador;
import com.asd.regnum.menu.PantallaMuerte;
import com.asd.regnum.teleport.Teleport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {


    private Main game;
    public GameScreen(Main game) {
        this.game = game;
    }
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont fuente;
    private FreeTypeFontGenerator generator;
    private BitmapFont fuente2;
    private FreeTypeFontGenerator generator2;
    private Texture textureFlecha;
    private GestorDeMusica gestorDeMusica;
    private GestorDeSonidos gestorDeSonidos;
    private Hud hud;
    private boolean pausado;

    private Mundo mundo;

    private final float VIRTUAL_WIDTH = 320f;
    private final float VIRTUAL_HEIGHT = 240f;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        this.textureFlecha = new Texture("hud/flecha.png");
        //FUENTE 1
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/determination.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 9;
        fuente = generator.generateFont(parameter);
        generator.dispose();
        //FUENTE 2
        generator2 = new FreeTypeFontGenerator(Gdx.files.internal("fuentes/determination.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 18;
        fuente2 = generator2.generateFont(parameter2);
        generator2.dispose();

        pausado = false;
        batch = new SpriteBatch();
        gestorDeMusica = new GestorDeMusica();
        gestorDeSonidos = new GestorDeSonidos();

        mundo = new Mundo(gestorDeSonidos);

        hud = new Hud(batch);
        gestorDeMusica.reproducirMusica(EnumMusica.DISTANT);
    }

    @Override
    public void render(float delta) {
        if (mundo.getJugador().getVida() <= 0) {
            game.setScreen(new PantallaMuerte(game));
            return;
        }


        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pausado = !pausado;
        }
        batch.begin();
        if(pausado){
            dt = 0;
        } else if (!pausado) {
            dt = Gdx.graphics.getDeltaTime();
        }

        mundo.obtenerPausado(pausado);

        if(mundo.getTeleport() != null){
            if(mundo.getMapManager().getEnemigos().size() == 0 && chequearColisionJugadorTeleport(mundo.getJugador(), mundo.getTeleport())){
                mundo.reiniciarMapa();
            }
        }

        if(mundo.getMapManager().getEnemigos().size() != 0){
            mundo.borrarTeleport();
        }



        mundo.actualizar(dt, viewport);


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

        mundo.dibujarTeleport(batch);
        mundo.dibujarParticulas(batch);
        mundo.getJugador().dibujar(batch);
        mundo.getJugador().dibujarProyectil(batch);
        mundo.getJugador().dibujarEfectos(batch);

        for(Item item : mundo.getMapManager().getItems()){
            item.dibujar(batch);
        }

        for (Enemigo enemigo : mundo.getMapManager().getEnemigos()) {
            enemigo.dibujarVida(batch, fuente);
            enemigo.dibujar(batch);
        }


        if(pausado){
            fuente2.draw(batch, "--PAUSADO--", mundo.getJugador().getX() - 42 , camera.position.y + 50);
        }

        batch.end();
        dibujarFlecha(mundo.getTeleport(), batch, mundo.getJugador());
        hud.render();
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
        viewport.update(width, height, true);
    }

    private void dibujarFlecha(Teleport teleport, SpriteBatch batch, Jugador jugador){
        if(teleport != null){
            float deltaX = mundo.getTeleport().getX() - jugador.getX();
            float deltaY = mundo.getTeleport().getY() - jugador.getY();
            float anguloGrados = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            batch.begin();
            batch.draw(
                textureFlecha,
                jugador.getX() + 5, jugador.getY() + 16,
                textureFlecha.getWidth() / 2f,
                textureFlecha.getHeight() / 2f,
                textureFlecha.getWidth(), textureFlecha.getHeight(),
                1.5f, 1.5f,
                anguloGrados - 90,
                0, 0,
                textureFlecha.getWidth(), textureFlecha.getHeight(),
                false, false
            );
            batch.end();
        }
    }

    private boolean chequearColisionJugadorTeleport(Jugador jugador, Teleport teleport){
        if(jugador.getHitbox().overlaps(teleport.getHitbox())){
            return true;
        }
        return false;
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
