package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.ViewHolder> {

    private List<Categoria> categoriaList;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public CategoriasAdapter(List<Categoria> categoriaList, int layout, OnItemClickListener itemClickListener ){
        this.categoriaList = categoriaList;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
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
        viewHolder.bind(categoriaList.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView nombre ;
        ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);



        }
        public void bind( final Categoria categoria, final OnItemClickListener listener){

            nombre.setText(categoria.getNombre());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(categoria, getAdapterPosition());
                }
            });


        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Categoria categoria, int posicion);

    }
}
