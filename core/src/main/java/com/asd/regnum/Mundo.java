package com.asd.regnum;

import com.asd.regnum.enemies.Enemigo;
import com.asd.regnum.gestores.GestorDeSonidos;
import com.asd.regnum.items.Corazon;
import com.asd.regnum.items.Item;
import com.asd.regnum.items.PowerUpDmg;
import com.asd.regnum.jugador.Jugador;
import com.asd.regnum.teleport.Teleport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class Mundo {

    private Jugador jugador;
    private Teleport teleport;
    private MapManager mapManager;
    private GestorDeSonidos gestorDeSonidos;
    private float cooldownRecibirDmg = 0.0f;
    private List<ParticleEffect> efectosParticulas = new ArrayList<>();
    private float xTeleport;
    private float yTeleport;
    private boolean pausado;

    public Mundo(GestorDeSonidos gestorDeSonidos) {
        this.gestorDeSonidos = gestorDeSonidos;
        this.jugador = new Jugador();
        this.mapManager = new MapManager();
        teleport = null;
        pausado = false;
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
        xTeleport = spawnX;
        yTeleport = spawnY;

    }
    public void crearTeleport(){
        if(mapManager.getEnemigos().size() == 0 && teleport == null){
            teleport = new Teleport(xTeleport - 16, yTeleport - 16);
        }
    }

    public void actualizar(float dt, FitViewport viewport) {
        crearTeleport();
        cargarZonaSegura();
        jugador.actualizarMovimiento(mapManager.getParedes(), dt, pausado);
        jugador.controlarDisparios(viewport, mapManager.getParedes(), dt, gestorDeSonidos, pausado);
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

    private void cargarZonaSegura(){
        if(Math.sqrt(Math.pow(xTeleport - jugador.getX(), 2) + Math.pow(yTeleport - jugador.getY(), 2)) < 100){
            for (Enemigo enemigo : mapManager.getEnemigos()){
                enemigo.pausarMovimiento(true);
            }
        }else{
            for (Enemigo enemigo : mapManager.getEnemigos()){
                enemigo.pausarMovimiento(false);
            }
        }
    }

    public Teleport getTeleport() {
        return teleport;
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
                if(item instanceof Corazon){
                    jugador.curarJugador();
                } else if (item instanceof PowerUpDmg) {
                    jugador.aumentarDmg();
                }

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

    public void borrarTeleport(){
        this.teleport = null;
    }

    public void dispose() {
        jugador.dispose();
        mapManager.dispose();
        teleport.dispose();
    }

    public void reiniciarMapa(){
        mapManager = new MapManager();
    }

    public void dibujarTeleport(SpriteBatch batch) {
        if(teleport != null){
            teleport.dibujar(batch);
        }
    }

    public void obtenerPausado(boolean pausado) {
        this.pausado = pausado;
    }
}
