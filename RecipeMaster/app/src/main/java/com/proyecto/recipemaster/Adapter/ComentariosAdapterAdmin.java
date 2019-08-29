package com.proyecto.recipemaster.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyecto.recipemaster.Clases.Comentario;
import com.proyecto.recipemaster.R;

import java.util.List;
public class ComentariosAdapterAdmin extends RecyclerView.Adapter<ComentariosAdapterAdmin.ViewHolder> {

    private List<Comentario> comentariosList;
    private int layout;
    private OnItemClickListener itemClickListener;
    private Context context;
    private String idRecipe;

    public ComentariosAdapterAdmin(List<Comentario> comentariosList, String idRecipe,  int layout, OnItemClickListener itemClickListener ){
        this.comentariosList = comentariosList;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.idRecipe = idRecipe;
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    String idDoc = comentariosList.get(getAdapterPosition()).getIdDocument();
                    deleteComentario(idDoc, getAdapterPosition());
                    return true;
                }
            });


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

    private void deleteComentario(final String idDoc,  final int index){
        final CharSequence[] item = {"Eliminar comentario"};
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (item[which].toString()){

                    case "Eliminar comentario":
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Recetas").document(idRecipe).collection("Comentarios").document(idDoc)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(context, "comentario eliminado", Toast.LENGTH_SHORT).show();
                                        comentariosList.remove(index);
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

