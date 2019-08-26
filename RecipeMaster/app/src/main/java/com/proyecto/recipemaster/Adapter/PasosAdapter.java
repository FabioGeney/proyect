package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Models.Pasos;
import com.proyecto.recipemaster.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasosAdapter  extends RecyclerView.Adapter<PasosAdapter.ViewHolder>  {

    private List<Pasos> pasos = new ArrayList<>();
    private Map<String, Pasos> temp = new HashMap<>();

    private OnItemClickListener itemClickListener;
    private Context context;

    public PasosAdapter(Context context, PasosAdapter.OnItemClickListener itemClickListener ){
        this.context = context;
        this.itemClickListener =  itemClickListener;
    }

    public void agregarMensaje(String key, Pasos paso){
        temp.put(key, paso);
        Collection<Pasos> getValues = temp.values();
        pasos = new ArrayList<> (getValues);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_pasos, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(pasos.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return pasos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView numero;
        private EditText despcricion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numero = itemView.findViewById(R.id.numero);
            despcricion = itemView.findViewById(R.id.paso);
        }
        public void bind( final Pasos paso, final OnItemClickListener listener){
            numero.setText(getAdapterPosition()+1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(paso, getAdapterPosition());
                }
            });


        }
    }

    public interface OnItemClickListener{
        void OnItemClick(Pasos paso, int posicion);

    }
}
