package com.asd.regnum.enemies;

import com.asd.regnum.enums.EstadosEnemigo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JefeEsqueleto extends Enemigo {

    private static final int ALTURA = 76;
    private static final int ANCHURA = 64;

    private float velocidad = 70f;
    private List<Vector2> waypoints;
    private int waypointActual = 0;
    private boolean pausado = false;

    public JefeEsqueleto(float x, float y, List<Vector2> waypoints) {
        super(5000, new Texture("enemies/JefeEsqueleto.png"),
            x, y, crearHitbox(x, y));

        this.waypoints = new ArrayList<>(waypoints);

        if (!this.waypoints.isEmpty()) {
            float centroX = 0;
            float centroY = 0;

            for (Vector2 punto : this.waypoints) {
                centroX += punto.x;
                centroY += punto.y;
            }

            centroX /= this.waypoints.size();
            centroY /= this.waypoints.size();

            final float cx = centroX;
            final float cy = centroY;

            this.waypoints.sort(Comparator.comparingDouble(p ->
                Math.atan2(p.y - cy, p.x - cx)
            ));

            float menorDistancia = Float.MAX_VALUE;

            for (int i = 0; i < this.waypoints.size(); i++) {
                Vector2 punto = this.waypoints.get(i);

                float distancia = Vector2.dst(
                    x, y, punto.x, punto.y
                );

                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    waypointActual = i;
                }
            }
        }
    }

    private static Rectangle crearHitbox(float x, float y) {
        final float TILE_SIZE = 16f;

        float drawWidth = ANCHURA;
        float drawHeight = ALTURA;

        float celdaCentroX = x + (TILE_SIZE / 2f);
        float celdaCentroY = y + (TILE_SIZE / 2f);

        return new Rectangle(
            celdaCentroX - (drawWidth / 2f),
            celdaCentroY - (drawHeight / 2f),
            drawWidth,
            drawHeight
        );
    }

    @Override
    public void update(float delta, float xJugador, float yJugador,
                       List<Rectangle> collisionRects) {

        if (this.getEstado() == EstadosEnemigo.MUERTO || pausado) {
            return;
        }

        if (waypoints == null || waypoints.isEmpty()) {
            return;
        }

        float movimientoRestante = velocidad * delta;

        float currentX = getX();
        float currentY = getY();

        Vector2 destino = waypoints.get(waypointActual);

        while (movimientoRestante > 0) {
            destino = waypoints.get(waypointActual);

            float deltaX = destino.x - currentX;
            float deltaY = destino.y - currentY;

            float distancia = (float) Math.hypot(deltaX, deltaY);

            // Tolerancia para considerar que llegó al waypoint (evita bloqueos por decimales)
            if (distancia < 2f || movimientoRestante >= distancia) {
                currentX = destino.x;
                currentY = destino.y;
                movimientoRestante -= distancia;
                waypointActual = (waypointActual + 1) % waypoints.size();
            } else {
                float dirX = deltaX / distancia;
                float dirY = deltaY / distancia;

                currentX += dirX * movimientoRestante;
                currentY += dirY * movimientoRestante;
                movimientoRestante = 0;
            }
        }

        float finalX = getX();
        float finalY = getY();

        if (!isColliding(currentX, currentY, collisionRects)) {
            finalX = currentX;
            finalY = currentY;
        } else {
            if (!isColliding(currentX, finalY, collisionRects)) {
                finalX = currentX;
            } else if (!isColliding(finalX, currentY, collisionRects)) {
                finalY = currentY;
            } else {
                float distanciaAlWaypointActual = Vector2.dst(getX(), getY(), destino.x, destino.y);
                if (distanciaAlWaypointActual < 15f) {
                    waypointActual = (waypointActual + 1) % waypoints.size();
                }
            }
        }

        setPosition(finalX, finalY);
        setHitbox(crearHitbox(finalX, finalY));
    }

    private boolean isColliding(float x, float y, List<Rectangle> collisionRects) {
        Rectangle bounds = crearHitbox(x, y);

        for (Rectangle rect : collisionRects) {
            if (rect.overlaps(bounds)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void pausarMovimiento(boolean estado) {
        this.pausado = estado;
    }

    @Override
    public void atacarJugador(float xJugador, float yJugador) {
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        final float TILE_SIZE = 16f;

        float drawWidth = ANCHURA;
        float drawHeight = ALTURA;

        float celdaCentroX = getX() + (TILE_SIZE / 2f);
        float celdaCentroY = getY() + (TILE_SIZE / 2f);

        float drawX = celdaCentroX - (drawWidth / 2f);
        float drawY = celdaCentroY - (drawHeight / 2f);

        batch.draw(
            getTextura(),
            drawX, drawY,
            drawWidth / 2f, drawHeight / 2f,
            drawWidth, drawHeight,
            1f, 1f,
            0,
            0, 0,
            getTextura().getWidth(),
            getTextura().getHeight(),
            false, false
        );
    }
}
