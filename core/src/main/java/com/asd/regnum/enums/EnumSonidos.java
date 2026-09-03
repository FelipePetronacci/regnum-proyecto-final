package com.asd.regnum.enums;

public enum EnumSonidos {
    DISPARO("sonidos/disparo.wav");

    String ruta;

    private EnumSonidos(String ruta){
        this.ruta = ruta;
    }

    public com.badlogic.gdx.files.FileHandle getArchivo() {
        return com.badlogic.gdx.Gdx.files.internal(ruta);
    }
}
