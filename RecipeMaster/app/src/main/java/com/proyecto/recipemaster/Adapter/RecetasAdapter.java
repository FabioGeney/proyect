package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class RecetasAdapter extends RecyclerView.Adapter<RecetasAdapter.ViewHolder> {

    private List<Receta> recetas;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public RecetasAdapter(List<Receta> recetas, Context context,int layout, OnItemClickListener itemClickListener ){
        this.recetas = recetas;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        context = viewGroup.getContext();
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(recetas.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView nombre ;
        public TextView likes ;
        public ImageView imageView;

        ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            likes = itemView.findViewById(R.id.likes);
            imageView = itemView.findViewById(R.id.imageRecipe);


        }
        public void bind( final Receta receta, final OnItemClickListener listener){

            nombre.setText(receta.getNombre());
            likes.setText(""+receta.getLikes() + " Me gusta");
            Picasso.with(context).load(receta.getImagen()).fit().into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(receta, getAdapterPosition());
                }
            });


        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Receta receta, int posicion);

    }
}
