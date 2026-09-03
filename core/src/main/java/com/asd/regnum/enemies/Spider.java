package com.asd.regnum.enemies;

import com.asd.regnum.enums.EstadosEnemigo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.List;

public class Spider extends Enemigo {
    private final int ALTURA = 35;
    private final int ANCHURA = 38;
    private float angulo;
    private float velocidad = 60f;

    public Spider(float x, float y) {
        super(150, new Texture("enemies/spider.png"), x, y, getHitbox(x, y));
        this.angulo = 0;
    }

    private static Rectangle getHitbox(float x, float y) {
        final float TILE_SIZE = 16f;
        float drawWidth = (38 - (38 / 18f)) / 2.5f;
        float drawHeight = (35 - (35 / 18f)) / 2.5f;
        float celdaCentroX = x + (TILE_SIZE / 2.5f);
        float celdaCentroY = y + (TILE_SIZE / 2.5f);
        float drawX = celdaCentroX - (drawWidth / 2.5f);
        float drawY = celdaCentroY - (drawHeight / 2.5f);
        return new Rectangle(drawX, drawY, drawWidth, drawHeight);
    }

    @Override
    public void update(float delta, float xJugador, float yJugador, List<Rectangle> collisionRects) {
        if (this.getEstado() != EstadosEnemigo.MUERTO) {
            float deltaX = xJugador - this.getX();
            float deltaY = yJugador - this.getY();
            float distanciaJugador = (float) Math.hypot(deltaY, deltaX);
            float anguloGrados = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            if (distanciaJugador < 120) {
                this.setEstado(EstadosEnemigo.PERSIGUIENDO);
                angulo = anguloGrados - 270;

                float dirX = deltaX / distanciaJugador;
                float dirY = deltaY / distanciaJugador;

                float currentX = this.getX();
                float currentY = this.getY();

                float nextX = currentX + dirX * velocidad * delta;
                if (!isColliding(nextX, currentY, collisionRects)) {
                    currentX = nextX;
                }

                float nextY = currentY + dirY * velocidad * delta;
                if (!isColliding(currentX, nextY, collisionRects)) {
                    currentY = nextY;
                }

                this.setPosition(currentX, currentY);
                this.setHitbox(getHitbox(currentX, currentY));
            } else {
                this.setEstado(EstadosEnemigo.QUIETO);
            }
        }
    }

    private boolean isColliding(float x, float y, List<Rectangle> collisionRects) {
        Rectangle spiderBounds = getHitbox(x, y);
        for (Rectangle rect : collisionRects) {
            if (rect.overlaps(spiderBounds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void atacarJugador(float xJugador, float yJugador) {
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        final float TILE_SIZE = 16f;
        float drawWidth = this.ANCHURA - (this.ANCHURA / 18f);
        float drawHeight = this.ALTURA - (this.ALTURA / 18f);
        float celdaCentroX = super.getX() + (TILE_SIZE / 2f);
        float celdaCentroY = super.getY() + (TILE_SIZE / 2f);
        float drawX = celdaCentroX - (drawWidth / 2f);
        float drawY = celdaCentroY - (drawHeight / 2f);

        batch.draw(
            super.getTextura(),
            drawX, drawY,
            drawWidth / 2f, drawHeight / 2f,
            drawWidth, drawHeight,
            1f, 1f,
            this.angulo,
            0, 0,
            super.getTextura().getWidth(),
            super.getTextura().getHeight(),
            false, false
        );
    }
}
