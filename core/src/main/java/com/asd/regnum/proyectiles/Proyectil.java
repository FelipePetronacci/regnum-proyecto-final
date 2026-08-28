package com.asd.regnum.proyectiles;

import com.asd.regnum.Colisionable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.List;
import com.asd.regnum.jugador.Jugador;

public abstract class Proyectil implements Colisionable {
    private float x;
    private float y;
    private float velocidad = 300f;
    private Rectangle hitbox = new Rectangle(x, y, 4, 4);
    private Texture textura;
    private float dirX;
    private float dirY;
    private boolean activo = true;
    private int dmg;

    private float xInicial;
    private float yInicial;
    private float alcanceMaximo = 100f;

    protected Proyectil(Jugador jugador, FitViewport viewport, int dmg){
        this.dmg = dmg;
        this.x = jugador.getX() + (jugador.getANCHOJUGADOR() / 2f);
        this.y = jugador.getY() + (jugador.getALTURAJUGADOR() / 2f);

        this.xInicial = this.x;
        this.yInicial = this.y;

        hitbox.setPosition(x, y);
        this.textura = new Texture("proyectiles/proyectil.png");

        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);

        float deltaX = mousePos.x - x;
        float deltaY = mousePos.y - y;

        float distancia = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distancia > 1f) {
            this.dirX = deltaX / distancia;
            this.dirY = deltaY / distancia;
        } else {
            this.dirX = 0;
            this.dirY = 0;
        }
    }

    public void moverse(float dt, List<Rectangle> paredes) {

        x += dirX * velocidad * dt;
        hitbox.setPosition(x, y);
        if (hayColision(paredes)) {
            x -= dirX * velocidad * dt;
            hitbox.setPosition(x, y);
            activo = false;
            return;
        }

        y += dirY * velocidad * dt;
        hitbox.setPosition(x, y);
        if (hayColision(paredes)) {
            y -= dirY * velocidad * dt;
            hitbox.setPosition(x, y);
            activo = false;
            return;
        }
        float distanciaRecorrida = (float) Math.sqrt(Math.pow(x - xInicial, 2) + Math.pow(y - yInicial, 2));

        if (distanciaRecorrida >= alcanceMaximo) {
            activo = false;
        }

    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, x, y, 4, 4);
    }

    private boolean hayColision(List<Rectangle> paredes) {
        for (Rectangle pared : paredes) {
            if (hitbox.overlaps(pared)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActivo() {
        return activo;
    }

    public float getY() {
        return y;
    }

    public Rectangle getHitbox() { return hitbox; }

    public float getX() {
        return x;
    }

    public int getDMG() { return dmg; }

    public void dispose() {
        textura.dispose();
    }
}
