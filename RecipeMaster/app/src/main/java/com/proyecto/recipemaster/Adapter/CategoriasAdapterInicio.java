package com.proyecto.recipemaster.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.proyecto.recipemaster.Models.Categoria;
import com.proyecto.recipemaster.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class CategoriasAdapterInicio extends RecyclerView.Adapter<CategoriasAdapterInicio.ViewHolder> {

    private List<Categoria> categoriaList;
    private HashMap<String,String> temp = new HashMap<>();
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public CategoriasAdapterInicio(List<Categoria> categoriaList, int layout, OnItemClickListener itemClickListener ){
        this.categoriaList = categoriaList;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    public List<String> getItem(){
        Collection<String> tempCollection = temp.values();
        return  new ArrayList<> (tempCollection);
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
        public ImageView imageView;

;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            imageView = itemView.findViewById(R.id.imagen);


        }
        public void bind( final Categoria categoria, final OnItemClickListener listener){

            nombre.setText(categoria.getNombre());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(temp.get(""+getAdapterPosition()) != null){
                        imageView.setBackgroundResource(R.drawable.background_black);
                        temp.remove(""+getAdapterPosition());

                    }else {
                        imageView.setBackgroundResource(R.drawable.background_cat);
                        temp.put(""+getAdapterPosition(), categoria.getNombre());

                    }
                }
            });


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
