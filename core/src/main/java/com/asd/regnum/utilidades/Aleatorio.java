package com.asd.regnum.utilidades;

public class Aleatorio {

    private Aleatorio(){}

    public static int generarAleatorio(int min, int max){
        return (int)(Math.random() * (max - min + 1)) + min;
    }

    public static int generarAleatorio(int max){
        return generarAleatorio(0, max);
    }

}
