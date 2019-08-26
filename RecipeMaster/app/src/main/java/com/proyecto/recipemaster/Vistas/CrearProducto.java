package com.proyecto.recipemaster.Vistas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.proyecto.recipemaster.Adapter.IngredientesAdapter;
import com.proyecto.recipemaster.Adapter.PasosAdapter;
import com.proyecto.recipemaster.Clases.Ingredientes;
import com.proyecto.recipemaster.Clases.Pasos;
import com.proyecto.recipemaster.Clases.Utility;
import com.proyecto.recipemaster.R;
import com.proyecto.recipemaster.Singlentons.SingletonPasos;
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
    private RecyclerView listIngredientes;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManagerIng;
    private IngredientesAdapter ingredientesAdapter;
    private Button ingrediente;
    private Button pasos;
    private RecyclerView lisPasos;
    private int SELECT_FILE = 1;
    private int REQUEST_CAMERA = 0;
    private String photoPath;
    private String eleccionusuario;

    private Uri hImagenUri;
    private PasosAdapter pasosAdapter;
    private int contador=1;
    private int contador2=1;
    private Button publicar;
    private SingletonPasos singletonPasos = SingletonPasos.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_producto);
        publicar = findViewById(R.id.publicar);
        imageView = findViewById(R.id.imageView);
        nombre = findViewById(R.id.nombre);
        descripcion = findViewById(R.id.descripcion);
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
                ArrayList<Pasos> pasos = singletonPasos.getPasos();
                String temp = "";
                for(Pasos index: pasos){
                    temp = temp+" "+index.getDescripcion();
                }
                Toast.makeText(CrearProducto.this, ""+temp, Toast.LENGTH_SHORT).show();
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
        contador2++;
        ingredientesAdapter.agregarIngrediente(new Ingredientes());
        contador2++;

    }

    private void seleccionarImagen(){

        final CharSequence[] items = {"Tomar foto", "Galería", "Cancelar"};
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
        imageView.setImageBitmap(b);
        hImagenUri = data.getData();
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


}
