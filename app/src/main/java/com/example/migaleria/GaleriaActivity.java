package com.example.migaleria;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GaleriaActivity extends AppCompatActivity {
    private RecyclerView recyclerFotos;
    private FotoAdapter fotoAdapter;
    private DBHelper dbHelper;
    private Foto fotoSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        getWindow().setStatusBarColor(Color.BLACK);
        getWindow().setNavigationBarColor(Color.BLACK);
        getWindow().getDecorView().setSystemUiVisibility(0);

        recyclerFotos = findViewById(R.id.recyclerFotos);
        dbHelper = new DBHelper(this);
        recyclerFotos.setLayoutManager(new LinearLayoutManager(this));

        Button botonEliminar = findViewById(R.id.botonEliminar);
        botonEliminar.setOnClickListener(v -> {
            eliminarFoto();
        });

        cargarFotos();

    }
    private void cargarFotos() {
        ArrayList<Foto> listaFotos = dbHelper.obtenerListaFotos();
        fotoAdapter = new FotoAdapter(listaFotos, this);
        recyclerFotos.setAdapter(fotoAdapter);
    }

    public void seleccionarFoto(Foto foto) {
        fotoSeleccionada = foto;
    }

    private void eliminarFoto() {
        if (fotoSeleccionada == null) {
            return;
        }

        Uri uri = Uri.parse(fotoSeleccionada.getRuta());
        getContentResolver().delete(uri, null, null);
        dbHelper.eliminarFoto(fotoSeleccionada.getId());
        fotoSeleccionada = null;
        cargarFotos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarFotos();
    }
}
