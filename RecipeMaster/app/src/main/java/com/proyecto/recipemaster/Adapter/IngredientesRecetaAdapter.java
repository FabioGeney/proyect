package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Clases.Ingredientes;
import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.R;

import java.util.List;

public class IngredientesRecetaAdapter extends RecyclerView.Adapter<IngredientesRecetaAdapter.ViewHolder> {

    private List<Ingredientes> inredientesList;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public IngredientesRecetaAdapter(List<Ingredientes> inredientesList, int layout, OnItemClickListener itemClickListener ){
        this.inredientesList = inredientesList;
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
        viewHolder.bind(inredientesList.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return inredientesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public TextView nombre ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);

        }


        public void bind( final Ingredientes ingredientes, final OnItemClickListener listener){

            nombre.setText(ingredientes.getNombreCantidad());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(ingredientes, getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Ingredientes ingredientes, int posicion);

    }
}

