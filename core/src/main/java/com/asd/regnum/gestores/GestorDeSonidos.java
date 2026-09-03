package com.asd.regnum.gestores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.asd.regnum.enums.EnumSonidos;
import java.util.HashMap;
import java.util.Map;

public class GestorDeSonidos {

    private final Map<EnumSonidos, Sound> sonidosCargados = new HashMap<>();

    private float volumenGeneral = 0.01f;
    private boolean silenciado = false;


    public void reproducir(EnumSonidos enumSonido) {
        if (silenciado) return;

        Sound sound = sonidosCargados.get(enumSonido);
        if (sound == null) {
            sound = Gdx.audio.newSound(enumSonido.getArchivo());
            sonidosCargados.put(enumSonido, sound);
        }
        sound.play(volumenGeneral);
    }


    public void setVolumenGeneral(float volumen) {
        this.volumenGeneral = Math.max(0.0f, Math.min(1.0f, volumen));
    }

    public void setSilenciado(boolean silenciado) {
        this.silenciado = silenciado;
    }

    public boolean isSilenciado() {
        return silenciado;
    }


    public void dispose() {
        for (Sound sound : sonidosCargados.values()) {
            sound.dispose();
        }
        sonidosCargados.clear();
    }
}
