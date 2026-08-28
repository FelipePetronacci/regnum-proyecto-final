package com.asd.regnum;

import com.asd.regnum.enemies.*;
import com.asd.regnum.rooms.Room;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.asd.regnum.utilidades.Aleatorio;

import java.util.ArrayList;
import java.util.List;

public class MapManager {

    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;
    private List<TiledMap> listaDeMapas;
    private List<Rectangle> paredes;
    private List<Enemigo> enemigos;
    private int[][] matrizMapa;

    private final int ROOM_WIDTH = 23 * 16;
    private final int ROOM_HEIGHT = 15 * 16;

    public MapManager() {
        mapLoader = new TmxMapLoader();
        listaDeMapas = new ArrayList<>();

        Room room1 = new Room("rooms/hab1.tmx", true, true, true, true);


        listaDeMapas.add(mapLoader.load("rooms/hab1.tmx"));
        listaDeMapas.add(mapLoader.load("rooms/hab2.tmx"));
        listaDeMapas.add(mapLoader.load("rooms/hab3.tmx"));

        int cantidadMapas = listaDeMapas.size();

        matrizMapa = new int[][]{
            {Aleatorio.generarAleatorio(1, cantidadMapas), Aleatorio.generarAleatorio(1, cantidadMapas), Aleatorio.generarAleatorio(1, cantidadMapas)},
            {Aleatorio.generarAleatorio(1, cantidadMapas), Aleatorio.generarAleatorio(1, cantidadMapas), Aleatorio.generarAleatorio(1, cantidadMapas)},
            {Aleatorio.generarAleatorio(1, cantidadMapas), Aleatorio.generarAleatorio(1, cantidadMapas), Aleatorio.generarAleatorio(1, cantidadMapas)}
        };

        mapRenderer = new OrthogonalTiledMapRenderer(listaDeMapas.get(0));
        cargarParedesGlobales();
        spawnearEnemigos();
    }

    public void dibujarMapa(OrthographicCamera camera) {
        float viewWidth = camera.viewportWidth * camera.zoom;
        float viewHeight = camera.viewportHeight * camera.zoom;

        for (int fila = 0; fila < matrizMapa.length; fila++) {
            for (int col = 0; col < matrizMapa[fila].length; col++) {
                int tipoHabitacion = matrizMapa[fila][col];

                if (tipoHabitacion != 0) {
                    TiledMap mapaActual = listaDeMapas.get(tipoHabitacion - 1);
                    if (mapaActual != null) {
                        float offsetX = col * ROOM_WIDTH;
                        float offsetY = fila * ROOM_HEIGHT;

                        mapRenderer.setMap(mapaActual);

                        Matrix4 translatedMatrix = new Matrix4(camera.combined).translate(offsetX, offsetY, 0);

                        float viewX = camera.position.x - (viewWidth / 2f) - offsetX;
                        float viewY = camera.position.y - (viewHeight / 2f) - offsetY;

                        mapRenderer.setView(translatedMatrix, viewX, viewY, viewWidth, viewHeight);

                        mapRenderer.render();
                    }
                }
            }
        }
    }

    private void spawnearEnemigos() {
        enemigos = new ArrayList<>();

        for (int fila = 0; fila < matrizMapa.length; fila++) {
            for (int col = 0; col < matrizMapa[fila].length; col++) {
                int tipoHabitacion = matrizMapa[fila][col];
                if (tipoHabitacion != 0) {
                    TiledMap mapa = listaDeMapas.get(tipoHabitacion - 1);

                    if (mapa != null) {
                        MapLayer capaSpawns = mapa.getLayers().get("spawnEnemigo");

                        if (capaSpawns != null) {
                            float offsetX = col * ROOM_WIDTH;
                            float offsetY = fila * ROOM_HEIGHT;

                            for (MapObject object : capaSpawns.getObjects()) {
                                if (object instanceof RectangleMapObject) {
                                    Rectangle rectOriginal = ((RectangleMapObject) object).getRectangle();
                                    float xGlobal = rectOriginal.x + offsetX;
                                    float yGlobal = rectOriginal.y + offsetY;

                                    Enemigo enemigo = new Spider(xGlobal, yGlobal);
                                    enemigos.add(enemigo);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void cargarParedesGlobales() {
        paredes = new ArrayList<>();

        for (int fila = 0; fila < matrizMapa.length; fila++) {
            for (int col = 0; col < matrizMapa[fila].length; col++) {
                int tipoHabitacion = matrizMapa[fila][col];
                if (tipoHabitacion != 0) {
                    TiledMap mapa = listaDeMapas.get(tipoHabitacion - 1);
                    if (mapa != null) {
                        MapLayer capaColisiones = mapa.getLayers().get("colisiones");
                        if (capaColisiones != null) {
                            float offsetX = col * ROOM_WIDTH;
                            float offsetY = fila * ROOM_HEIGHT;
                            for (MapObject object : capaColisiones.getObjects()) {
                                if (object instanceof RectangleMapObject) {
                                    Rectangle rectOriginal = ((RectangleMapObject) object).getRectangle();
                                    Rectangle rectDesplazado = new Rectangle(
                                        rectOriginal.x + offsetX,
                                        rectOriginal.y + offsetY,
                                        rectOriginal.width,
                                        rectOriginal.height
                                    );
                                    paredes.add(rectDesplazado);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Rectangle> getParedes() {
        return paredes;
    }
    public List<Enemigo> getEnemigos() { return enemigos; }



    public void dispose() {
        for (TiledMap map : listaDeMapas) {
            map.dispose();
        }
        mapRenderer.dispose();
        for (Enemigo enemigo : enemigos){
            enemigo.dispose();
        }
    }
}
