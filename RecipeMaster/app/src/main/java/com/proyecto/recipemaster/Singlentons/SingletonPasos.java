package com.proyecto.recipemaster.Singlentons;

import com.proyecto.recipemaster.Clases.Pasos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SingletonPasos {

    private static SingletonPasos singletonPasos = new SingletonPasos();
    public static SingletonPasos getInstance(){
        return singletonPasos;
    }

    public static void setSingletonCanasta(SingletonPasos singletonPasos){
        SingletonPasos.singletonPasos = singletonPasos;
    }

    private HashMap<String, Pasos> pasos = new HashMap<>();


    private SingletonPasos(){

    }

    public void setPaso(String key, Pasos texto){
        pasos.put(key, texto);
    }
    public void remove(String key){pasos.remove(key);}
    public void removeAll(){pasos = new HashMap<>();}
    public int getSize(){
        return pasos.size();
    }
    public ArrayList<Pasos> getPasos(){
        ArrayList<Pasos> temp;
        Collection<Pasos> getValues = pasos.values();
        temp = new ArrayList<> (getValues);
        return temp;
    }


}
