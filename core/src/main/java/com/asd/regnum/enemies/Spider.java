package com.asd.regnum.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import org.w3c.dom.css.Rect;

public class Spider extends Enemigo{
    private final int ALTURA = 35;
    private final int ANCHURA = 38;

    public Spider(float x, float y) {
        super(150, 15, new Texture("enemies/spider.png"), x, y, getHitbox(x,y));

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
    public void dibujar(SpriteBatch batch) {
        final float TILE_SIZE = 16f;
        float drawWidth = this.ANCHURA - (this.ANCHURA / 18f);
        float drawHeight = this.ALTURA - (this.ALTURA / 18f);
        float celdaCentroX = super.getX() + (TILE_SIZE / 2f);
        float celdaCentroY = super.getY() + (TILE_SIZE / 2f);
        float drawX = celdaCentroX - (drawWidth / 2f);
        float drawY = celdaCentroY - (drawHeight / 2f);
        batch.draw(super.getTextura(), drawX, drawY, drawWidth, drawHeight);
    }
}
