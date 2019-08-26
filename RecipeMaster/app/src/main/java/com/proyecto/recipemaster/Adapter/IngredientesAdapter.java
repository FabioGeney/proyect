package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Clases.Ingredientes;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singlentons.SingletonIngredientes;
import com.proyecto.recipemaster.Singlentons.SingletonPasos;

import java.util.ArrayList;
import java.util.List;

public class IngredientesAdapter extends RecyclerView.Adapter<IngredientesAdapter.ViewHolder> {
    private List<Ingredientes> ingredientes = new ArrayList<>();
    private SingletonIngredientes singletonIngredientes = SingletonIngredientes.getInstance();

    private OnItemClickListener itemClickListener;
    private Context context;

    public IngredientesAdapter(Context context, OnItemClickListener itemClickListener ){
        this.context = context;
        this.itemClickListener =  itemClickListener;
    }

    public void agregarIngrediente(Ingredientes key){
        ingredientes.add(key);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_ingredientes, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(ingredientes.get(i),itemClickListener);
    }

    @Override
    public int getItemCount() {
        return ingredientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private EditText ingredienteEdt;
        private ImageView close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            close = itemView.findViewById(R.id.close);
            ingredienteEdt = itemView.findViewById(R.id.ingredientes);
        }
        public void bind( final Ingredientes ingrediente, final OnItemClickListener listener){

            ingredienteEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    ingrediente.setNombreCantidad(ingredienteEdt.getText().toString());
                    singletonIngredientes.setIngredientes(""+getAdapterPosition(), ingrediente);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ingredientes.remove(getAdapterPosition());
                    singletonIngredientes.remove(""+getAdapterPosition());
                    updateAll();

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(ingrediente, getAdapterPosition());
                }
            });




        }
    }
    private void updateAll(){
        ArrayList<Ingredientes> temp = singletonIngredientes.getPasos();
        singletonIngredientes.removeAll();
        int index = 0;
        for (Ingredientes paso: temp){
            singletonIngredientes.setIngredientes(""+index, paso);
        }
        notifyDataSetChanged();


    }

    public interface OnItemClickListener{
        void OnItemClick(Ingredientes ingredientes, int posicion);

    }
}
