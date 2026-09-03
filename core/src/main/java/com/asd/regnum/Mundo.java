package com.asd.regnum;

import com.asd.regnum.enemies.Enemigo;
import com.asd.regnum.gestores.GestorDeSonidos;
import com.asd.regnum.jugador.Jugador;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Mundo {

    private Jugador jugador;
    private MapManager mapManager;
    private GestorDeSonidos gestorDeSonidos;

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

        for (Enemigo enemigo : mapManager.getEnemigos()) {
            enemigo.colisionarBala(jugador.getProyectiles());
            enemigo.chequearVida();
            enemigo.atacarJugador(jugador.getX(), jugador.getY());
            enemigo.update(dt, jugador.getX(), jugador.getY(), mapManager.getParedes());
        }

        mapManager.despawnearEnemigos();
    }

    public Jugador getJugador() {
        return jugador;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public void dispose() {
        if (jugador != null) jugador.dispose();
        if (mapManager != null) mapManager.dispose();
    }
}
