package com.asd.regnum.rooms;

public enum FactoryRoom {

    //             Norte, Sur, Este, Oeste
    ROOM1("rooms/hab1.tmx", true, true, true, true),   // 4 accesos (Centro)
    ROOM2("rooms/hab2.tmx", true, true, true, true),   // 4 accesos
    ROOM3("rooms/hab3.tmx", true, true, true, true),   // 4 accesos

    // Pasillos / Codos / Esquinas
    ROOM4("rooms/hab4.tmx", false, true, false, true),  // S, O (Esquina sup-der)
    ROOM5("rooms/hab5.tmx", false, true, false, false), // S (Callejón sin salida)
    ROOM6("rooms/hab6.tmx", true, false, true, false),  // N, E (Esquina inf-izq)
    ROOM7("rooms/hab7.tmx", true, false, false, true),  // N, O (Esquina inf-der)
    ROOM8("rooms/hab8.tmx", false, true, true, false),  // S, E (Esquina sup-izq)
    ROOM9("rooms/hab9.tmx", false, false, true, true),  // E, O (Pasillo horizontal)

    // --- VARIANTES FALTANTES PARA CERRAR EL MAPA PERFECTAMENTE ---
    ROOM10("rooms/hab10.tmx", true, true, false, false), // N, S (Pasillo vertical)
    ROOM11("rooms/hab11.tmx", true, true, true, false),  // N, S, E (T-Junction)
    ROOM12("rooms/hab12.tmx", true, true, false, true),  // N, S, O (T-Junction)
    ROOM13("rooms/hab13.tmx", true, false, true, true),  // N, E, O (T-Junction)
    ROOM14("rooms/hab14.tmx", false, true, true, true);  // S, E, O (T-Junction)

    private String direccion;
    private boolean puertaNorte;
    private boolean puertaSur;
    private boolean puertaEste;
    private boolean puertaOeste;

    private FactoryRoom(String direccion, boolean puertaNorte, boolean puertaSur, boolean puertaEste, boolean puertaOeste){
        this.direccion = direccion;
        this.puertaNorte = puertaNorte;
        this.puertaSur = puertaSur;
        this.puertaEste = puertaEste;
        this.puertaOeste = puertaOeste;
    }

    public Room getRoom(){
        return new Room(this.direccion, this.puertaNorte, this.puertaSur, this.puertaEste, this.puertaOeste);
    }
}
