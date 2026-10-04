package com.example.migaleria;

public class FotoConUbicacion extends Foto{

    public FotoConUbicacion(int id, String fecha, String ruta, double latitud, double longitud) {
        super(id, fecha, ruta, latitud, longitud);
    }

    @Override
    public boolean tieneUbicacion() {
        return true;
    }
}
