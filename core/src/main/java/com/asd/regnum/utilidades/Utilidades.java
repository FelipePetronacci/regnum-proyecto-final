package com.asd.regnum.utilidades;

public class Utilidades {
    public static int limitarEstado(int cantidad) {
        if(cantidad > 100) { return 100;}
        if(cantidad < 0) { return 0;}
        return cantidad;
    }
}
