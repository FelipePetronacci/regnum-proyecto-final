package com.asd.regnum.rooms;

public  class Room {
    private String direccion;
    private boolean puertaNorte;
    private boolean puertaSur;
    private boolean puertaEste;
    private boolean puertaOeste;


    public Room(String direccion, boolean puertaNorte, boolean puertaSur, boolean puertaEste, boolean puertaOeste) {
        this.direccion = direccion;
        this.puertaNorte = puertaNorte;
        this.puertaSur = puertaSur;
        this.puertaEste = puertaEste;
        this.puertaOeste = puertaOeste;
    }

    public String getRuta(){
        return this.direccion;
    }

    public boolean tienePuertaNorte() { return puertaNorte; }
    public boolean tienePuertaSur() { return puertaSur; }
    public boolean tienePuertaEste() { return puertaEste; }
    public boolean tienePuertaOeste() { return puertaOeste; }

}
