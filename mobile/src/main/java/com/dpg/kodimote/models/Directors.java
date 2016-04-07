package com.dpg.kodimote.models;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by enriquedelpozogomez on 23/01/16.
 */
public class Directors {

    private static Directors sInstance = null;
    private static Map<String,Director> sDirectors = null;

    public static Directors getInstance(){
        if(sInstance ==null){
            sInstance = new Directors();
            sDirectors = new TreeMap<>();
        }

        return sInstance;
    }

    public void addDirectorToCollectionFromFilm(Director director, MediaItem mediaItem){
        if(!sDirectors.containsKey(director.getName())){
            director.getMovies().add(mediaItem);
            sDirectors.put(director.getName(),director);
        }else{
            if(!sDirectors.get(director.getName()).getMovies().contains(mediaItem)){
                sDirectors.get(director.getName()).getMovies().add(mediaItem);
            }
        }
    }

    public int getCount(){
        return sDirectors.size();
    }

    public Director getDirector(int position){

        return (Director)sDirectors.values().toArray()[position];
    }

    public Director getDirectorByName(String name){
        return sDirectors.get(name);
    }

    public void wipeDirectorsCollection(){
        sDirectors = new TreeMap<>();
    }
}
