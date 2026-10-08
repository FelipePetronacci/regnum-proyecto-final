package com.asd.regnum;

import com.asd.regnum.enemies.*;
import com.asd.regnum.enums.EstadosEnemigo;
import com.asd.regnum.items.*;
import com.asd.regnum.rooms.*;
import com.asd.regnum.utilidades.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapManager {

    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;
    private List<TiledMap> listaDeMapas;
    private List<Rectangle> paredes;
    private List<Enemigo> enemigos;
    private List<Item> items;
    private int[][] matrizMapa;
    private Room[][] grillaRooms;

    private final int FILAS = 3;
    private final int COLUMNAS = 3;
    private final int ROOM_WIDTH = 23 * 16;
    private final int ROOM_HEIGHT = 15 * 16;




    public MapManager() {
        mapLoader = new TmxMapLoader();
        listaDeMapas = new ArrayList<>();
        matrizMapa = new int[FILAS][COLUMNAS];
        grillaRooms = new Room[FILAS][COLUMNAS];

        armarMapa();

        mapRenderer = new OrthogonalTiledMapRenderer(listaDeMapas.get(0));

        cargarParedesGlobales();
        spawnearEnemigos();
    }


    public void armarMapa() {
        listaDeMapas.clear();
        FactoryRoom[] opciones = FactoryRoom.values();

        boolean exito = GenerarMapa(0, 0, opciones);

        if (!exito) {
            return;
        }


        Map<String, Integer> mapasCargados = new HashMap<>();

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Room room = grillaRooms[f][c];
                String ruta = room.getRuta();

                if (!mapasCargados.containsKey(ruta)) {
                    TiledMap map = mapLoader.load(ruta);
                    listaDeMapas.add(map);
                    mapasCargados.put(ruta, listaDeMapas.size());
                }

                matrizMapa[f][c] = mapasCargados.get(ruta);
            }
        }
    }

    private boolean GenerarMapa(int fila, int col, FactoryRoom[] opciones) {
        if (fila >= FILAS) {
            return true;
        }
        int sigFila = (col == COLUMNAS - 1) ? fila + 1 : fila;
        int sigCol = (col == COLUMNAS - 1) ? 0 : col + 1;
        List<FactoryRoom> candidatos = new ArrayList<>(List.of(opciones));

        Collections.shuffle(candidatos);// mezclo el array de mierda

        for (FactoryRoom candidato : candidatos) {
            Room room = candidato.getRoom();

            if (esHabitacionValida(fila, col, room)) {
                grillaRooms[fila][col] = room;

                if (GenerarMapa(sigFila, sigCol, opciones)) {
                    return true;
                }

                grillaRooms[fila][col] = null;
            }
        }

        return false;
    }

    private boolean esHabitacionValida(int fila, int col, Room room) {
        if (fila == 0 && room.tienePuertaSur()) return false;
        if (fila == FILAS - 1 && room.tienePuertaNorte()) return false;
        if (col == 0 && room.tienePuertaOeste()) return false;
        if (col == COLUMNAS - 1 && room.tienePuertaEste()) return false;

        if (fila > 0 && grillaRooms[fila - 1][col] != null) {
            Room vecinoSur = grillaRooms[fila - 1][col];
            if (room.tienePuertaSur() != vecinoSur.tienePuertaNorte()) {
                return false;
            }
        }

        if (col > 0 && grillaRooms[fila][col - 1] != null) {
            Room vecinoOeste = grillaRooms[fila][col - 1];
            if (room.tienePuertaOeste() != vecinoOeste.tienePuertaEste()) {
                return false;
            }
        }

        return true;
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

    public void borrarItem(int i){
        items.remove(i);
    }

    public void despawnearEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.getEstado() == EstadosEnemigo.MUERTO) {
                enemigo.dispose();
            }
        }
        enemigos.removeIf(enemigo -> enemigo.getEstado() == EstadosEnemigo.MUERTO);
    }

    private void spawnearEnemigos() {
        enemigos = new ArrayList<>();
        items = new ArrayList<>();

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
                                    if(Aleatorio.generarAleatorio(1, 2) == 2) {
                                        int num = Aleatorio.generarAleatorio(1, 100);
                                        if(num < 15) {
                                            Enemigo enemigo = new Bat(xGlobal, yGlobal);
                                            enemigos.add(enemigo);
                                        }else if(num < 100) {
                                            Enemigo enemigo = new Spider(xGlobal, yGlobal);
                                            enemigos.add(enemigo);
                                        }
                                    }else if(Aleatorio.generarAleatorio(1, 2) == 2){
                                        Item item;
                                        if(Aleatorio.generarAleatorio(1, 2) == 1){
                                            item = new Corazon(xGlobal, yGlobal);
                                        } else {
                                            item = new PowerUpDmg(xGlobal, yGlobal);
                                        }
                                        items.add(item);
                                    }
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

    public List<Rectangle> getParedes() { return paredes; }
    public List<Enemigo> getEnemigos() { return enemigos; }
    public Item getItem(int index) { return items.get(index); }
    public List<Item> getItems() { return items; }

    public void dispose() {
        for (TiledMap map : listaDeMapas) {
            map.dispose();
        }
        mapRenderer.dispose();
        for (Enemigo enemigo : enemigos){
            enemigo.dispose();
        }
        for (Item item : items){
            item.dispose();
        }
    }
}
