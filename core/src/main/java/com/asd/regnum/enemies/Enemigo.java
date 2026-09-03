package com.asd.regnum.enemies;

import com.asd.regnum.*;
import com.asd.regnum.enums.*;
import com.asd.regnum.proyectiles.*;
import com.asd.regnum.utilidades.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.beans.Introspector;
import java.util.List;

public abstract class Enemigo{
    private int vida;
    private int vidaMaxima;
    private EstadosEnemigo estado;
    private Texture textura;
    private float x;
    private float y;
    private Rectangle hitbox;


    public Enemigo(int vida, Texture textura, float x, float y, Rectangle hitbox) {
        this.estado = EstadosEnemigo.PERSIGUIENDO;
        this.hitbox = hitbox;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.textura = textura;
        this.x = x;
        this.y = y;
    }

    public void update(float delta, float xJugador, float yJugador, List<Rectangle> collisionRects){

    }

    public void dibujarHitbox(){

    }

    public abstract void atacarJugador(float xJugador, float yJugador);

    public void colisionarBala(List<Proyectil> proyectiles){
        for(Proyectil proyectil : proyectiles) {
            if (proyectil.getHitbox().overlaps(this.hitbox)){
                this.vida -= proyectil.getDMG();
                proyectil.setActivo(false);
            }
        }
    }

    public void chequearVida(){
        this.vida = Utilidades.limitarEstado(this.vida, this.vidaMaxima);
        if(this.vida <= 0 && this.estado != EstadosEnemigo.MUERTO){
            estado = EstadosEnemigo.MUERTO;
        }
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }


    public void dibujarVida(SpriteBatch batch, BitmapFont fuente) {
        String textoVida = vida + "/" + vidaMaxima;
        float textoX = x;
        float textoY = y + textura.getHeight() - 12;

        fuente.draw(batch, textoVida, textoX - 10, textoY - 20);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public Texture getTextura() { return textura; }
    public EstadosEnemigo getEstado() { return estado; }
    protected void setEstado(EstadosEnemigo estado) { this.estado = estado; };

    protected void setHitbox(Rectangle hitbox) { this.hitbox = hitbox; }


    public void dispose() {
        textura.dispose();
    }

    public abstract void dibujar(SpriteBatch batch);
}
