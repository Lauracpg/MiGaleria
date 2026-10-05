package com.example.migaleria;

import android.graphics.Color;
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
    private GaleriaActivity galeriaActivity;
    private int posicionSeleccionada = -1;

    public FotoAdapter(ArrayList<Foto> listaFotos, GaleriaActivity galeriaActivity) {
        this.listaFotos = listaFotos;
        this.galeriaActivity = galeriaActivity;
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

        if (foto.tieneUbicacion()) {
            String ubicacion = foto.getLatitud() + ", " + foto.getLongitud();
            holder.textoUbicacion.setText(ubicacion);
        } else {
            holder.textoUbicacion.setText("");
        }

        if (position == posicionSeleccionada) {
            holder.itemFoto.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemFoto.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            int posicion = holder.getAdapterPosition();
            if (posicion != RecyclerView.NO_POSITION) {
                posicionSeleccionada = posicion;
                Foto fotoSeleccionada = listaFotos.get(posicion);
                galeriaActivity.seleccionarFoto(fotoSeleccionada);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFotos.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenFoto;
        TextView textoFecha;
        TextView textoUbicacion;
        View itemFoto;
        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenFoto = itemView.findViewById(R.id.imagenFoto);
            textoFecha = itemView.findViewById(R.id.textoFecha);
            textoUbicacion = itemView.findViewById(R.id.textoUbicacion);
            itemFoto = itemView.findViewById(R.id.itemFoto);
        }
    }
}
