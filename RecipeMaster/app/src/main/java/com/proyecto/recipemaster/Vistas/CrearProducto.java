package com.proyecto.recipemaster.Vistas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.proyecto.recipemaster.Adapter.IngredientesAdapter;
import com.proyecto.recipemaster.Adapter.PasosAdapter;
import com.proyecto.recipemaster.Clases.Ingredientes;
import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.Clases.Receta;
import com.proyecto.recipemaster.Clases.Recetero;
import com.proyecto.recipemaster.Clases.SessionManager;
import com.proyecto.recipemaster.Clases.Utility;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singletons.SingletonUsuario;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrearProducto extends AppCompatActivity {
    private ImageView imageView;
    private EditText nombre;
    private EditText descripcion;
    private EditText tipo;
    private RecyclerView listIngredientes;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManagerIng;
    private IngredientesAdapter ingredientesAdapter;
    private StorageReference hStorageRef;
    private Button ingrediente;
    private Button pasos;
    private RecyclerView lisPasos;
    private String picture;
    private int SELECT_FILE = 1;
    private int REQUEST_CAMERA = 0;
    private String photoPath;
    private String eleccionusuario;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri hImagenUri;
    private PasosAdapter pasosAdapter;
    private Button publicar;
    private Recetero receteroU;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Crear Receta");
        publicar = findViewById(R.id.publicar);
        imageView = findViewById(R.id.imageView);
        nombre = findViewById(R.id.nombre);
        descripcion = findViewById(R.id.descripcion);
        tipo = findViewById(R.id.tipo);
        listIngredientes = findViewById(R.id.listIngredientes);
        lisPasos = findViewById(R.id.pasosList);
        ingrediente = findViewById(R.id.button2);
        pasos = findViewById(R.id.pasos);
        Picasso.with(this).load(R.drawable.placeholder).fit().into(imageView);
        layoutManager = new LinearLayoutManager(this);
        layoutManagerIng = new LinearLayoutManager(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarImagen();
            }
        });

        hStorageRef = FirebaseStorage.getInstance().getReference("Subidas");
        descripcion.setCompoundDrawablesWithIntrinsicBounds(R.drawable.description, 0, 0, 0);
        nombre.setCompoundDrawablesWithIntrinsicBounds(R.drawable.receta, 0, 0, 0);
        tipo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tipo, 0, 0, 0);
        tipo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);

        tipo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    seleccionaTipo();
                }

                return false;
            }
        });

        pasosAdapter = new PasosAdapter(this, new PasosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Pasos paso, int posicion) {

            }
        });
        ingredientesAdapter = new IngredientesAdapter(this,new  IngredientesAdapter.OnItemClickListener(){
            @Override
            public void OnItemClick(Ingredientes ingrediente, int posicion) {

            }
        });

        SessionManager sessionManager = new SessionManager(this);
        Gson gson = new Gson();
        String userGson = sessionManager.getUsuario();
        receteroU = gson.fromJson(userGson,Recetero.class);


        Toast.makeText(this, receteroU.getNombre(), Toast.LENGTH_SHORT).show();

        setIngredientes();
        setPasos();

        pasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasosAdapter.agregarPaso(new Pasos());

            }
        });

        ingrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientesAdapter.agregarIngrediente(new Ingredientes());
            }
        });
        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Pasos> pasos = pasosAdapter.getPasos();
                String temp = "";
                for(Pasos index: pasos){
                    temp = temp+" "+index.getDescripcion();
                }
                Toast.makeText(CrearProducto.this, ""+temp, Toast.LENGTH_SHORT).show();

                uploadFile();
            }
        });


        listIngredientes.setLayoutManager(layoutManagerIng);
        listIngredientes.setAdapter(ingredientesAdapter);
        lisPasos.setLayoutManager(layoutManager);
        lisPasos.setAdapter(pasosAdapter);
    }

    private void setPasos(){

        pasosAdapter.agregarPaso(new Pasos());

        pasosAdapter.agregarPaso(new Pasos());



    }
    private void setIngredientes(){
        ingredientesAdapter.agregarIngrediente(new Ingredientes());
        ingredientesAdapter.agregarIngrediente(new Ingredientes());


    }

    private void seleccionarImagen(){

        final CharSequence[] items = { "Galería", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(CrearProducto.this);

                if (items[item].equals("Tomar foto")) {
                    eleccionusuario = "Tomar foto";

                    if(result) {
                            camaraIntent();
                    } else
                        Toast.makeText(CrearProducto.this, "Intente de nuevo", Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("Galería")){
                    eleccionusuario = "Galería";

                    if(result) {
                        galeriaIntent();
                    } else
                        Toast.makeText(CrearProducto.this, "Intente de nuevo", Toast.LENGTH_SHORT).show();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                imageView.setImageBitmap(thumbnail);
            }
        }
    }
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void camaraIntent(){
        requestStoragePermission();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                Uri imageUri = FileProvider.getUriForFile(this, authorities, photoFile);
                intent.putExtra("data", imageUri);
                startActivityForResult(intent, REQUEST_CAMERA);
                //hImagenUri = imageUri;
            }
        }
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
        Dexter.withActivity(this)
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
    private void onSelectFromGalleryResult(Intent data){
        Bitmap b = null;

        if(data != null){
            try {
                b = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        //imageView.setImageBitmap(b);
        hImagenUri = data.getData();
        Picasso.with(this).load(hImagenUri).fit().into(imageView);
    }
    private void galeriaIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione Archivo"), SELECT_FILE);
    }

    private String getFileExtension(Uri uri){
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(uri));
    }

    private void uploadFile(){
        if(hImagenUri != null) {

            final StorageReference fileReference = hStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(hImagenUri));

            fileReference.putFile(hImagenUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(CrearProducto.this, "Imagen cargada", Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            picture = uri.toString();
                            guardarReceta();
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

        }
    }

    private void guardarReceta(){

        CollectionReference enviarReceta = db.collection("Recetas");
        String idUsuario = receteroU.getId();
        String name = nombre.getText().toString();
        String descrip = descripcion.getText().toString();
        String tipoR = tipo.getText().toString() ;
        List<Pasos> pasos = pasosAdapter.getPasos();
        List<Ingredientes> ingredientes = ingredientesAdapter.getIngredientes();
        String lowerCase = "";
        for(Ingredientes ingredientes1: ingredientes){
            lowerCase = lowerCase + " " +ingredientes1.getNombreCantidad();
        }

        Receta receta = new Receta(idUsuario, name, receteroU.getNombre(), descrip, tipoR, picture, pasos, ingredientes, lowerCase);
        enviarReceta.add(receta);
    }

    private void seleccionaTipo(){
        final CharSequence[] item = {"Típica","Fast","Fit","Oriental", "Europea", "Otro"};
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Seleccione un tipo");
        alert.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (item[which].toString()){
                    case "Típica":
                        tipo.setText(item[which].toString());
                        break;
                    case "Fast":
                        tipo.setText(item[which].toString());
                        break;
                    case "Fit":
                        tipo.setText(item[which].toString());
                        break;
                    case "Europea":
                        tipo.setText(item[which].toString());
                        break;
                    case "Oriental":
                        tipo.setText(item[which].toString());
                        break;
                    case "Otro":
                        tipo.setText(item[which].toString());
                        break;
                    default:
                        dialog.dismiss();

                }
            }
        });
        alert.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
