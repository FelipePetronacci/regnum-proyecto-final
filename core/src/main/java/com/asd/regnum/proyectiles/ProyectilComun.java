package com.asd.regnum.proyectiles;

import com.asd.regnum.jugador.Jugador;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ProyectilComun extends Proyectil{

    public ProyectilComun(Jugador jugador, FitViewport viewport) {
        super(jugador, viewport, 40);
    }
}
