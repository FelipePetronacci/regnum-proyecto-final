package com.asd.regnum.enums;

public enum EnumMusica {
    DISTANT("musica/Distant.ogg");

    String ruta;

    private EnumMusica(String ruta){
        this.ruta = ruta;
    }

    public com.badlogic.gdx.files.FileHandle getArchivo(){
        return com.badlogic.gdx.Gdx.files.internal(ruta);
    }
}
