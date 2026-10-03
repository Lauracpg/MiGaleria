package com.example.migaleria;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoViewHolder> {
    private ArrayList<Foto> listaFotos;

    public FotoAdapter(ArrayList<Foto> listaFotos) {
        this.listaFotos = listaFotos;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_foto, parent, false);

        return new FotoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        Foto foto = listaFotos.get(position);
        holder.imagenFoto.setImageURI(Uri.parse(foto.getRuta()));
        holder.textoFecha.setText(foto.getFecha());
        String ubicacion = foto.getLatitud() + ", " + foto.getLongitud();
        holder.textoUbicacion.setText(ubicacion);
    }

    @Override
    public int getItemCount() {
        return listaFotos.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenFoto;
        TextView textoFecha;
        TextView textoUbicacion;
        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenFoto = itemView.findViewById(R.id.imagenFoto);
            textoFecha = itemView.findViewById(R.id.textoFecha);
            textoUbicacion = itemView.findViewById(R.id.textoUbicacion);
        }
    }

}
