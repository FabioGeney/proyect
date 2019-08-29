package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Clases.Comentario;
import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;

import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.ViewHolder> {

    private List<Comentario> comentariosList;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public MensajesAdapter(List<Comentario> comentariosList, int layout, OnItemClickListener itemClickListener ){
        this.comentariosList = comentariosList;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    public void addComentario(Comentario comentario){
        comentariosList.add(comentario);
        notifyItemInserted(comentariosList.size()-1);
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
        viewHolder.bind(comentariosList.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return comentariosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView nombre ;
        public TextView descr ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            descr = itemView.findViewById(R.id.comentario);



        }
        public void bind( final Comentario comentario, final OnItemClickListener listener){

            nombre.setText(comentario.getNombreRemintente());
            descr.setText(comentario.getMensaje());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(comentario, getAdapterPosition());
                }
            });


        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Comentario comentario, int posicion);

    }
}


