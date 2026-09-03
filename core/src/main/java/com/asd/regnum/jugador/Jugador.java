package com.asd.regnum.jugador;

import com.asd.regnum.enums.EnumSonidos;
import com.asd.regnum.gestores.GestorDeSonidos;
import com.asd.regnum.proyectiles.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;

public class Jugador{

    private final int ALTURAJUGADOR = 11;
    private final int ANCHOJUGADOR = 15;

    private float x = 170;
    private float y = 75;
    private int vida = 3;
    private int vidaMaxima = 3;
    private float velocidad = 75f;
    private Texture textura = new Texture("player/player.png");
    private Rectangle hitbox = new Rectangle(x, y, ANCHOJUGADOR, ALTURAJUGADOR);

    private List<Proyectil> proyectiles = new ArrayList<>();
    private List<ParticleEffect> efectosParticulas = new ArrayList<>();

    // 1. Método exclusivo para la lógica (Se llama desde el Mundo)
    public void actualizarMovimiento(List<Rectangle> paredes, float dt){
        controlarMovimiento(paredes, dt);
    }

    // 2. Método exclusivo para pintar gráficos (Se llama desde la GameScreen)
    public void dibujar(SpriteBatch batch){
        batch.draw(textura, x, y, ANCHOJUGADOR, ALTURAJUGADOR);
    }

    public void controlarMovimiento(List<Rectangle> paredes, float dt){

        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            y += velocidad * dt;
            hitbox.setPosition(x, y);
            if (hayColision(paredes)) {
                y -= velocidad * dt;
                hitbox.setPosition(x, y);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            y -= velocidad * dt;
            hitbox.setPosition(x, y);
            if (hayColision(paredes)) {
                y += velocidad * dt;
                hitbox.setPosition(x, y);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            x -= velocidad * dt;
            hitbox.setPosition(x, y);
            if (hayColision(paredes)) {
                x += velocidad * dt;
                hitbox.setPosition(x, y);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            x += velocidad * dt;
            hitbox.setPosition(x, y);
            if (hayColision(paredes)) {
                x -= velocidad * dt;
                hitbox.setPosition(x, y);
            }
        }
    }

    private boolean hayColision(List<Rectangle> paredes) {
        for (Rectangle pared : paredes) {
            if (hitbox.overlaps(pared)) {
                return true;
            }
        }
        return false;
    }

    public void dibujarProyectil(SpriteBatch batch){
        for (Proyectil p : proyectiles) {
            p.dibujar(batch);
        }
    }

    public void dibujarEfectos(SpriteBatch batch){
        for (int i = efectosParticulas.size() - 1; i >= 0; i--) {
            ParticleEffect efecto = efectosParticulas.get(i);
            efecto.draw(batch, Gdx.graphics.getDeltaTime());

            if (efecto.isComplete()) {
                efecto.dispose();
                efectosParticulas.remove(i);
            }
        }
    }

    public void controlarDisparios(FitViewport viewport, List<Rectangle> paredes, float dt, GestorDeSonidos gestorDeSonidos) {
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            proyectiles.add(new ProyectilComun(this, viewport));
            gestorDeSonidos.reproducir(EnumSonidos.DISPARO);
        }

        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            Proyectil p = proyectiles.get(i);
            p.moverse(dt, paredes);

            if (!p.isActivo()) {
                ParticleEffect efecto = new ParticleEffect();
                efecto.load(Gdx.files.internal("efectos/explosion.p"), Gdx.files.internal("efectos/"));
                efecto.setPosition(p.getX(), p.getY());
                efecto.start();

                efectosParticulas.add(efecto);

                p.dispose();
                proyectiles.remove(i);
            }
        }
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getALTURAJUGADOR() { return ALTURAJUGADOR; }
    public int getANCHOJUGADOR() { return ANCHOJUGADOR; }
    public List<Proyectil> getProyectiles() { return proyectiles; }
    public int getVida() { return vida; }

    public void dispose() {
        textura.dispose();
        for (Proyectil p : proyectiles) {
            p.dispose();
        }
        for (ParticleEffect efecto : efectosParticulas) {
            efecto.dispose();
        }
    }
}
