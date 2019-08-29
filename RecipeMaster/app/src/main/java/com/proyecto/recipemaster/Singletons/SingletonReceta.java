package com.proyecto.recipemaster.Singletons;

import com.proyecto.recipemaster.Clases.Receta;

public class SingletonReceta {

    private static SingletonReceta singletonReceta = new SingletonReceta();
    public static SingletonReceta getInstance(){
        return singletonReceta;
    }

    public static void setSingletonPedido(SingletonReceta singletonPedido){
        SingletonReceta.singletonReceta = singletonPedido;
    }

    private Receta receta;

    private SingletonReceta(){

    }

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}
