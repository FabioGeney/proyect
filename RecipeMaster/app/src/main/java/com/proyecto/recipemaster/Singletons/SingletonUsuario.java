package com.proyecto.recipemaster.Singletons;
import com.proyecto.recipemaster.Clases.Recetero;


public class SingletonUsuario {

    private static SingletonUsuario singletonUsuario = new SingletonUsuario();
    public static SingletonUsuario getInstance(){
        return singletonUsuario;
    }

    public static void setSingletonPedido(SingletonUsuario singletonUsuario){
        SingletonUsuario.singletonUsuario = singletonUsuario;
    }

    private Recetero recetero;

    private SingletonUsuario(){

    }

    public Recetero getReceta() {
        return recetero;
    }

    public void setReceta(Recetero usuario) {
        this.recetero = usuario;
    }
}
