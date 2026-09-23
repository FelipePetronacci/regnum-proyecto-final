package com.asd.regnum.enemies;

import com.asd.regnum.enums.EstadosEnemigo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class Bat extends Enemigo {
    private static final int ALTURA = 27;
    private static final int ANCHURA = 40;
    private float angulo;

    public Bat(float x, float y) {
        super(100, new Texture("enemies/bat.png"), x, y, getHitbox(x, y));
    }

    private static Rectangle getHitbox(float x, float y) {
        final float TILE_SIZE = 16f;
        float drawWidth = (ANCHURA - (ANCHURA / 18f)) / 2.5f;
        float drawHeight = (ALTURA - (ALTURA / 18f)) / 2.5f;
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

            if (distanciaJugador < 250) {
                this.setEstado(EstadosEnemigo.PERSIGUIENDO);
                angulo = anguloGrados - 90;
                float dirX = deltaX / distanciaJugador;
                float dirY = deltaY / distanciaJugador;
                float currentX = this.getX();
                float currentY = this.getY();

                float velocidad = 120f;
                currentX += dirX * velocidad * delta;
                currentY += dirY * velocidad * delta;

                this.setPosition(currentX, currentY);
                this.setHitbox(getHitbox(currentX, currentY));
            } else {
                this.setEstado(EstadosEnemigo.QUIETO);
            }
        }
    }

    @Override
    public void atacarJugador(float xJugador, float yJugador) {
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        final float TILE_SIZE = 16f;
        float drawWidth = ANCHURA - (ANCHURA / 18f);
        float drawHeight = ALTURA - (ALTURA / 18f);
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
