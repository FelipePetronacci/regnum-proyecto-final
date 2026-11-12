package com.asd.regnum.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class JefeEsqueleto extends Enemigo{
    private static final int ALTURA = 76;
    private static final int ANCHURA = 64;
    float velocidad = 75f;

    public JefeEsqueleto(float x, float y){
        super(5000, new Texture("enemies/JefeEsqueleto.png"), x, y, crearHitbox(x, y));
    }

    private static Rectangle crearHitbox(float x, float y) {
        final float TILE_SIZE = 16f;
        float drawWidth = ANCHURA - (ANCHURA / 18f);
        float drawHeight = ALTURA - (ALTURA / 18f);
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
    public void pausarMovimiento(boolean estado) {

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
            0,
            0, 0,
            super.getTextura().getWidth(),
            super.getTextura().getHeight(),
            false, false
        );
    }
}
