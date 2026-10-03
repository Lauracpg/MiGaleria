package com.example.migaleria;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GaleriaActivity extends AppCompatActivity {
    private RecyclerView recyclerFotos;
    private FotoAdapter fotoAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        recyclerFotos = findViewById(R.id.recyclerFotos);
        dbHelper = new DBHelper(this);
        ArrayList<Foto> listaFotos = dbHelper.obtenerListaFotos();
        fotoAdapter = new FotoAdapter(listaFotos);
        recyclerFotos.setLayoutManager(new LinearLayoutManager(this));
        recyclerFotos.setAdapter(fotoAdapter);
    }
}
