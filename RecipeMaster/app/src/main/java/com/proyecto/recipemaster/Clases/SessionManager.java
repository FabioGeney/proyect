package com.proyecto.recipemaster.Clases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.proyecto.recipemaster.MainActivity;
import com.proyecto.recipemaster.Vistas.IniciarSesion;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";

    public static final String USUARIO = "SUARIO";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }
    public void createSession (Recetero recetero){
        Gson gson = new Gson();
        editor.putBoolean(LOGIN, true);
        editor.putString(USUARIO, gson.toJson(recetero));
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public  String getUsuario(){
        return sharedPreferences.getString(USUARIO, null);
    }

    public void checkLogin(){
        if(this.isLoggin()){
           Intent i = new Intent(context, MainActivity.class);
           context.startActivity(i);
           ((IniciarSesion)context).finish();

        }
    }
    /*
    public HashMap<String, String> getUserDetail () {
        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        return user;
    }*/
    public void actualizaUsuario(Usuario usuario, String tipoUsuario){
        editor.clear();
        editor.commit();
        Gson gson = new Gson();
        editor.putBoolean(LOGIN, true);
        editor.putString(USUARIO, gson.toJson(usuario));
        editor.apply();
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, IniciarSesion.class);
        context.startActivity(i);
    }

}
