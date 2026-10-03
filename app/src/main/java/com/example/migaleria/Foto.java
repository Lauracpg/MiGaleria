package com.example.migaleria;

public class Foto {
    private int id;
    private String fecha;
    private String ruta;
    private double latitud;
    private double longitud;

    public Foto(int id, String fecha, String ruta, double latitud, double longitud) {
        this.id = id;
        this.fecha = fecha;
        this. ruta = ruta;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getRuta() {
        return ruta;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
