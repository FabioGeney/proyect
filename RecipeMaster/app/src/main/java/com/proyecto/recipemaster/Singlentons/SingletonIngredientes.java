package com.proyecto.recipemaster.Singlentons;

import com.proyecto.recipemaster.Clases.Ingredientes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SingletonIngredientes {

    private static SingletonIngredientes singletonIngredientes = new SingletonIngredientes();
    public static SingletonIngredientes getInstance(){
        return singletonIngredientes;
    }

    public static void setSingletonCanasta(SingletonIngredientes singletonIngredientes){
        SingletonIngredientes.singletonIngredientes = singletonIngredientes;
    }

    private HashMap<String, Ingredientes> ingredientes = new HashMap<>();


    private SingletonIngredientes(){

    }

    public void setIngredientes(String key, Ingredientes texto){
        ingredientes.put(key, texto);
    }
    public void remove(String key){ingredientes.remove(key);}
    public void removeAll(){ingredientes = new HashMap<>();}
    public int getSize(){
        return ingredientes.size();
    }
    public ArrayList<Ingredientes> getPasos(){
        ArrayList<Ingredientes> temp;
        Collection<Ingredientes> getValues = ingredientes.values();
        temp = new ArrayList<> (getValues);
        return temp;
    }
}
