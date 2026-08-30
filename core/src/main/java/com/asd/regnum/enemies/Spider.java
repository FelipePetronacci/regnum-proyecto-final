package com.asd.regnum.enemies;

import com.asd.regnum.enums.EstadosEnemigo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.css.Rect;

public class Spider extends Enemigo{
    private final int ALTURA = 35;
    private final int ANCHURA = 38;
    private float angulo;

    public Spider(float x, float y) {
        super(150, 15, new Texture("enemies/spider.png"), x, y, getHitbox(x,y));
        this.angulo = 0;
    }

    private static Rectangle getHitbox(float x, float y) {
        final float TILE_SIZE = 16f;
        float drawWidth = 38 - (38 / 18f);
        float drawHeight = 35 - (35 / 18f);
        float celdaCentroX = x + (TILE_SIZE / 2f);
        float celdaCentroY = y + (TILE_SIZE / 2f);
        float drawX = celdaCentroX - (drawWidth / 2f);
        float drawY = celdaCentroY - (drawHeight / 2f);
        Rectangle hitbox = new Rectangle(drawX, drawY, drawWidth, drawHeight);
        return hitbox;
    }

    @Override
    public void atacarJugador(float xJugador, float yJugador) {
        if(this.getEstado() != EstadosEnemigo.MUERTO){
            float deltaX = xJugador - this.getX();
            float deltaY = yJugador - this.getY();
            float distanciaJugador = (float) Math.hypot(deltaY, deltaX);
            float anguloGrados = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;
            if(distanciaJugador < 100){
                this.setEstado(EstadosEnemigo.PERSIGUIENDO);
                angulo = anguloGrados - 270;
            }
        }
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
