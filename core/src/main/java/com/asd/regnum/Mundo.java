package com.asd.regnum;

import com.asd.regnum.enemies.Enemigo;
import com.asd.regnum.gestores.GestorDeSonidos;
import com.asd.regnum.items.Item;
import com.asd.regnum.jugador.Jugador;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class Mundo {

    private Jugador jugador;
    private MapManager mapManager;
    private GestorDeSonidos gestorDeSonidos;
    private float cooldownRecibirDmg = 0.0f;
    private List<ParticleEffect> efectosParticulas = new ArrayList<>();

    public Mundo(GestorDeSonidos gestorDeSonidos) {
        this.gestorDeSonidos = gestorDeSonidos;
        this.jugador = new Jugador();
        this.mapManager = new MapManager();

        inicializarPosicionJugador();
    }

    private void inicializarPosicionJugador() {
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

    public void actualizar(float dt, FitViewport viewport) {

        jugador.actualizarMovimiento(mapManager.getParedes(), dt);
        jugador.controlarDisparios(viewport, mapManager.getParedes(), dt, gestorDeSonidos);
        gestionarColisionJugadorEnemigo(dt);
        gestionarColisionJugadorItem();
        for(Item item : mapManager.getItems()){
            item.moverItem(dt);
        }
        for (Enemigo enemigo : mapManager.getEnemigos()) {
            enemigo.colisionarBala(jugador.getProyectiles());
            enemigo.chequearVida();
            enemigo.atacarJugador(jugador.getX(), jugador.getY());
            enemigo.update(dt, jugador.getX(), jugador.getY(), mapManager.getParedes());
        }
        for (int i = efectosParticulas.size() - 1; i >= 0; i--) {
            ParticleEffect efecto = efectosParticulas.get(i);
            efecto.update(dt);
            if (efecto.isComplete()) {
                efecto.dispose();
                efectosParticulas.remove(i);
            }
        }

        mapManager.despawnearEnemigos();
    }

    public Jugador getJugador() {
        return jugador;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public void gestionarColisionJugadorItem(){
        for (int i = mapManager.getItems().size() - 1; i >= 0; i--) {
            Item item = mapManager.getItem(i);
            if (jugador.getHitbox().overlaps(item.getHitbox())) {
                ParticleEffect efecto = new ParticleEffect();
                efecto.load(Gdx.files.internal("efectos/corazonExplosion.p"), Gdx.files.internal("efectos"));
                efecto.setPosition(item.getX(), item.getY());
                efecto.start();
                efectosParticulas.add(efecto);
                mapManager.borrarItem(i);
                item.dispose();
                jugador.curarJugador();
            }
        }
    }
    public void dibujarParticulas(SpriteBatch batch) {
        for (ParticleEffect efecto : efectosParticulas) {
            efecto.draw(batch);
        }
    }

    public void gestionarColisionJugadorEnemigo(float dt){
        for(Enemigo enemigo : mapManager.getEnemigos()){
            if(jugador.getHitbox().overlaps(enemigo.getHitbox()) && cooldownRecibirDmg <= 0.0f){
                jugador.recibirDmg();
                cooldownRecibirDmg = 35.0f;
            } else {
                cooldownRecibirDmg -= dt;
            }
        }
    }

    public void dispose() {
        jugador.dispose();
        mapManager.dispose();
    }
}
