package com.asd.regnum.gestores;

import com.asd.regnum.enums.EnumMusica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class GestorDeMusica {

    Music musicaActual;


    public GestorDeMusica(){
        this.musicaActual = Gdx.audio.newMusic(EnumMusica.DISTANT.getArchivo());
    }


    public void reproducirMusica(EnumMusica musica){
        if(!this.musicaActual.isPlaying()) {
            this.musicaActual = Gdx.audio.newMusic(musica.getArchivo());
            this.musicaActual.setVolume(0.01f);
            this.musicaActual.setLooping(true);
            musicaActual.play();
        }
    }

    public void dispose(){
        this.musicaActual.dispose();
    }

}
