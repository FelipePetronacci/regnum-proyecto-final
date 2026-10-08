package com.asd.regnum.teleport;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Teleport {

    private float x;
    private float y;
    private Rectangle hitbox;
    private Texture texture;

    public Teleport(float x, float y){
        this.x = x;
        this.y = y;
        this.texture = new Texture("teleport/teleport.png");
    }

    public void dibujar(SpriteBatch batch){
        batch.draw(texture, x, y, this.texture.getWidth(), this.texture.getHeight());
    }

}
