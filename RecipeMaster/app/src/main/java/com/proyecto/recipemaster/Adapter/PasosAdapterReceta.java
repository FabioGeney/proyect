package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.R;

import java.util.List;

public class PasosAdapterReceta extends RecyclerView.Adapter<PasosAdapterReceta.ViewHolder> {

    private List<Pasos> pasosList;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public PasosAdapterReceta(List<Pasos> pasosList, int layout, OnItemClickListener itemClickListener ){
        this.pasosList = pasosList;
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
        viewHolder.bind(pasosList.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return pasosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView paso ;
        public TextView numero ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paso = itemView.findViewById(R.id.paso);
            numero = itemView.findViewById(R.id.numero);

        }


        public void bind( final Pasos pasos, final OnItemClickListener listener){

            int index = getAdapterPosition() +1;

            numero.setText("  "+index);
            paso.setText(pasos.getDescripcion());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(pasos, getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Pasos pasos, int posicion);

    }
}
