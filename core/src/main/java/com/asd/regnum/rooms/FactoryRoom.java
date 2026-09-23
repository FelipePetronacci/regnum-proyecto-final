package com.asd.regnum.rooms;

public enum FactoryRoom {

    //      Norte
    //Oeste         Este
    //      Sur
    ROOM1("rooms/hab1.tmx", true, true, true, true),
    ROOM2("rooms/hab1.tmx", true, true, true, true),
    ROOM3("rooms/hab1.tmx", true, true, true, true),
    ROOM4("rooms/hab4.tmx", false, true, false, true),
    ROOM5("rooms/hab5.tmx", false, true, false, false),
    ROOM6("rooms/hab6.tmx", true, false, true, false),
    ROOM7("rooms/hab7.tmx", true, false, false, true),
    ROOM8("rooms/hab8.tmx", false, true, true, false),
    ROOM9("rooms/hab9.tmx", false, false, true, true);

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

    /*
    public static Room elegirRoom(String direccion){
        FactoryRoom habitaciones[] = FactoryRoom.values();
        switch (direccion){
            case "norte":

                break;
            default:
                break;
        }
    }
    */


}
