package com.proyecto.recipemaster.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.proyecto.recipemaster.Adapter.RecetasAdapter;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.Clases.Usuario;
import com.proyecto.recipemaster.Clases.Utility;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonReceta;
import com.proyecto.recipemaster.Vistas.CrearProducto;
import com.proyecto.recipemaster.Vistas.RecetasActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilFragment extends Fragment {

    private TextView nombre;
    private CircleImageView circleImageView;
    private RecyclerView favoritos;
    private RecyclerView misRecetas;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManager2;
    private List<Receta> recetasFavoritas;
    private List<Receta> recetas;
    private Button cerrar;
    private SessionManager sessionManager;
    private RecyclerView.Adapter favAdapter;
    private RecyclerView.Adapter recAdapter;
    private Recetero recetero;
    private int SELECT_FILE = 1;
    private String picture;
    private Uri hImagenUri;
    private String photoPath;
    private TextView textRecetas;
    private TextView textFavoritos;
    private String eleccionusuario;
    private int REQUEST_CAMERA = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private StorageReference hStorageRef;

    public PerfilFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        nombre = root.findViewById(R.id.nombre);
        favoritos = root.findViewById(R.id.favoritos);
        misRecetas = root.findViewById(R.id.recyclerView);
        cerrar = root.findViewById(R.id.cerrarSesion);
        circleImageView = root.findViewById(R.id.image);



        hStorageRef = FirebaseStorage.getInstance().getReference("Subidas");


        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


        sessionManager = new SessionManager(getContext());
        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        recetero = gson.fromJson(userGson,Recetero.class);
        nombre.setText(recetero.getNombre());

        if(recetero.getImage()!=null){
            Picasso.with(getContext()).load(recetero.getImage()).fit().into(circleImageView);
        }

        recetasFavoritas = getFavoritos();

        favAdapter = new RecetasAdapter(recetasFavoritas, getContext(), R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Receta receta, int posicion) {
                Intent intent = new Intent(getContext(), RecetasActivity.class);
                SingletonReceta singletonReceta = SingletonReceta.getInstance();
                singletonReceta.setReceta(receta);
                startActivity(intent);
            }
        });

        favoritos.setLayoutManager(linearLayoutManager2);
        favoritos.setAdapter(favAdapter);

        recetas = getRecetas();

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

        return root;
    }

    private List<Receta> getFavoritos(){


        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        Recetero recetero = gson.fromJson(userGson, Recetero.class);
        Collection<Receta> tempCollection = recetero.getFavoritos().values();
        return  new ArrayList<> (tempCollection);

    }

    private ArrayList<Receta> getRecetas(){
        final ArrayList<Receta> temp = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Recetas").whereEqualTo("idUsuario", recetero.getId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Receta receta = document.toObject(Receta.class);
                                receta.setIdDocument(document.getId());
                                temp.add(receta);

                            }

                            recAdapter = new RecetasAdapter(recetas, getContext(), R.layout.list_recetas, new RecetasAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(Receta receta, int posicion) {
                                    Intent intent = new Intent(getContext(), RecetasActivity.class);
                                    SingletonReceta singletonReceta = SingletonReceta.getInstance();
                                    singletonReceta.setReceta(receta);
                                    startActivity(intent);

                                }
                            });

                            misRecetas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                            misRecetas.setAdapter(recAdapter);



                        } else {
                            Toast.makeText(getContext(), "nothing", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        return temp;


    }

    private void seleccionarImagen(){

        final CharSequence[] items = { "Galería", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Agregar Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());

                if (items[item].equals("Tomar foto")) {
                    eleccionusuario = "Tomar foto";

                    if(result) {
                        //camaraIntent();
                    } else
                        Toast.makeText(getContext(), "Intente de nuevo", Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("Galería")){
                    eleccionusuario = "Galería";

                    if(result) {
                        galeriaIntent();
                    } else
                        Toast.makeText(getContext(), "Intente de nuevo", Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galeriaIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione Archivo"), SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data){
        Bitmap b = null;

        if(data != null){
            try {
                b = MediaStore.Images.Media.getBitmap(getContext().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        //imageView.setImageBitmap(b);
        hImagenUri = data.getData();
        Picasso.with(getContext()).load(hImagenUri).fit().into(circleImageView);
        uploadFile();
    }

    private String getFileExtension(Uri uri){
        ContentResolver c = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(uri));
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File ima = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        photoPath = ima.getAbsolutePath();
        return ima;
    }

    private void requestStoragePermission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }else if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                Bitmap thumbnail = (Bitmap) extras.get("data");
                //imagen.setImageBitmap(thumbnail);
                hImagenUri = data.getData();
                //String pat = getIntent().getStringExtra("data");

                //hImagenUri = data.getData();
                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, baos);

                //l.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                hImagenUri = getImageUri(thumbnail);
                circleImageView.setImageBitmap(thumbnail);


            }

        }
    }

    private void uploadFile(){
        if(hImagenUri != null) {
            final Map<String, Object> uploadImage = new HashMap<>();
            final StorageReference fileReference = hStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(hImagenUri));

            fileReference.putFile(hImagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(CrearProducto.this, "Imagen cargada", Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            picture = uri.toString();
                            uploadImage.put("image", picture);
                            db.collection("Usuarios").document(recetero.getId()).update(uploadImage);
                            recetero.setImage(picture);
                            sessionManager.createSession(recetero);

                        }
                    });

                    //picture = taskSnapshot.getStorage().getDownloadUrl().toString();
                    /*String uploadId = hDatabaseRef.push().getKey();
                    hDatabaseRef.child(uploadId).setValue(picture);*/
                }
            /*}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CrearProducto.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });


        */
            });
        }
        else {
            Toast.makeText(getContext(), "no entre ", Toast.LENGTH_SHORT).show();
        }
    }



}
