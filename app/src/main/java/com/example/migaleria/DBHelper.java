package com.example.migaleria;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TABLE_FOTOS = "fotos";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_RUTA = "ruta";
    private static final String COLUMN_LATITUD = "latitud";
    private static final String COLUMN_LONGITUD = "longitud";

    public DBHelper(Context context) {
        super(context, "MiGaleria.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String crearTabla = "CREATE TABLE " + TABLE_FOTOS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_RUTA + " TEXT, " +
                COLUMN_LATITUD + " REAL, " +
                COLUMN_LONGITUD + " REAL)";

        db.execSQL(crearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor obtenerFotos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_FOTOS, null, null,
                null, null, null,
                COLUMN_FECHA + " DESC");
    }

    public ArrayList<Foto> obtenerListaFotos() {
        ArrayList<Foto> listaFotos = new ArrayList<>();
        Cursor cursor = obtenerFotos();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA));
            String ruta = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RUTA));
            double latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD));
            double longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD));

            Foto foto;
            if (latitud == 0 && longitud == 0) {
                foto = new FotoSinUbicacion(id, fecha, ruta, latitud, longitud);
            } else {
                foto = new FotoConUbicacion(id, fecha, ruta, latitud, longitud);
            }

            listaFotos.add(foto);
        }
        cursor.close();
        return listaFotos;
    }

    public void eliminarFoto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FOTOS, COLUMN_ID + " =?",
                new String[]{String.valueOf(id)});
    }
}