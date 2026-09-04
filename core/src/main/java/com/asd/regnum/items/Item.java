package com.asd.regnum.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import javax.crypto.spec.RC2ParameterSpec;

public class Item {
    private float x;
    private float y;
    private float desplazamientoY;
    private Rectangle hitbox;
    private Texture texture;
    float tiempoAnimacion;

    protected Item(float x, float y, Texture texture){
        tiempoAnimacion = 0;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(x, y, this.texture.getWidth(), this.texture.getHeight());
    }

    public void dibujar(SpriteBatch batch){
        batch.draw(texture, x, y, this.texture.getWidth(), this.texture.getHeight());
    }

    public void moverItem(float dt){
        tiempoAnimacion += dt * 1.5f;
        float desplazamientoY = MathUtils.sin(tiempoAnimacion) * 0.01f;
        this.y = y + desplazamientoY;
        this.hitbox.setPosition(x, y);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose(){
        this.texture.dispose();
    }

}
