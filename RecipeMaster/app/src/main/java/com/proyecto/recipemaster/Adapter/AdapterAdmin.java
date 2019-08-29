package com.proyecto.recipemaster.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.ViewHolder> {

    private List<Receta> recetas;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;

    public AdapterAdmin(List<Receta> recetas, Context context,int layout, OnItemClickListener itemClickListener ){
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    String id = recetas.get(getAdapterPosition()).getIdDocument();
                    deleteRecipe(id, getAdapterPosition());
                    return true;
                }
            });


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

    private void deleteRecipe(final String id,  final int index){
        final CharSequence[] item = {"Eliminar receta"};
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (item[which].toString()){

                    case "Eliminar receta":
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Recetas").document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(context, "receta eliminada", Toast.LENGTH_SHORT).show();
                                        recetas.remove(index);
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        break;
                    default:
                        dialog.dismiss();
                        break;

                }
            }
        });
        alert.show();
    }

}
