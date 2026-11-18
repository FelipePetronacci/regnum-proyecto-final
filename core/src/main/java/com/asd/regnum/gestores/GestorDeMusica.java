package com.asd.regnum.gestores;

import com.asd.regnum.enums.EnumMusica;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class GestorDeMusica {

    private Music musicaActual;

    public GestorDeMusica() {
        this.musicaActual = null;
    }

    public void reproducirMusica(EnumMusica musica) {

        if (musicaActual != null && musicaActual.isPlaying()) {
            return;
        }

        if (musicaActual != null) {
            musicaActual.dispose();
            musicaActual = null;
        }

        musicaActual = Gdx.audio.newMusic(musica.getArchivo());

        musicaActual.setVolume(0.01f);
        musicaActual.setLooping(true);
        musicaActual.play();
    }

    public void dispose() {
        if (musicaActual != null) {
            musicaActual.stop();
            musicaActual.dispose();
            musicaActual = null;
        }
    }
}
