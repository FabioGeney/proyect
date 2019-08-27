package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasosAdapter  extends RecyclerView.Adapter<PasosAdapter.ViewHolder>  {

    private List<Pasos> pasos = new ArrayList<>();


    private OnItemClickListener itemClickListener;
    private Context context;

    public PasosAdapter(Context context, PasosAdapter.OnItemClickListener itemClickListener ){
        this.context = context;
        this.itemClickListener =  itemClickListener;
    }

    public void agregarPaso(Pasos key){
        pasos.add(key);
        notifyItemInserted(pasos.size());
    }
    public List<Pasos> getPasos(){
        return pasos;
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
        private ImageView close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numero = itemView.findViewById(R.id.numero);
            despcricion = itemView.findViewById(R.id.paso);
            close = itemView.findViewById(R.id.close);
        }
        public void bind( final Pasos paso, final OnItemClickListener listener){
            int index = getAdapterPosition()+1;
            numero.setText("  "+index);
            if(paso.getDescripcion()!=null){
                despcricion.setText(paso.getDescripcion());
            }
            despcricion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    paso.setDescripcion(despcricion.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {


                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    paso.setDescripcion(null);
                    pasos.remove(paso);
                    despcricion.setText(null);
                    notifyDataSetChanged();
                    //updateAll();

                }
            });

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
