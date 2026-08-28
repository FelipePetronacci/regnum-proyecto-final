package com.asd.regnum.enemies;

import com.asd.regnum.Colisionable;
import com.asd.regnum.enums.EstadosEnemigo;
import com.asd.regnum.proyectiles.Proyectil;
import com.asd.regnum.utilidades.Aleatorio;
import com.asd.regnum.utilidades.Utilidades;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.beans.Introspector;
import java.util.List;

public abstract class Enemigo implements Colisionable {
    private int vida;
    private int vidaMaxima;
    private EstadosEnemigo estado;
    private int dmg;
    private Texture textura;
    private float x;
    private float y;
    private Rectangle hitbox;


    public Enemigo(int vida, int dmg, Texture textura, float x, float y, Rectangle hitbox) {
        this.hitbox = hitbox;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.dmg = dmg;
        this.textura = textura;
        this.x = x;
        this.y = y;
    }

    public void dibujarHitbox(){

    }

    public void colisionarBala(List<Proyectil> proyectiles){
        for(Proyectil proyectil : proyectiles) {
            if (proyectil.getHitbox().overlaps(this.hitbox)){
                this.vida -= proyectil.getDMG();
                System.out.println("enemigo golpeado");
                System.out.println("Daño quitado: " + proyectil.getDMG());
                System.out.println(this.vida);
            }
        }
    }

    public void chequearVida(){
        boolean muerto = false;
        this.vida = Utilidades.limitarEstado(this.vida);
        if(this.vida <= 0 && muerto){
            muerto = true;
            System.out.println("Enemigo muerto");
            this.dispose();

        }
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
    protected void setHitbox(Rectangle hitbox) { this.hitbox = hitbox; }

    public void dispose() {
        textura.dispose();
    }

    public abstract void dibujar(SpriteBatch batch);
}
