package com.example.migaleria;

public class FotoSinUbicacion extends Foto {
    public FotoSinUbicacion(int id, String fecha, String ruta, double latitud, double longitud) {
        super(id, fecha, ruta, latitud, longitud);
    }

    @Override
    public boolean tieneUbicacion() {
        return false;
    }
}
