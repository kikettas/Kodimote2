package com.dpg.kodimote.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Project Kodimote created by enriquedelpozogomez on 21/03/16.
 */
public class Genres {

    private static Genres sInstance = null;
    private static Map<String,Genre> sGenres = null;

    public static Genres getInstance(){
        if(sInstance ==null){
            sInstance = new Genres();
            sGenres = new TreeMap<>();
        }

        return sInstance;
    }

    public void addMovieToGenre(String genre, MediaItem mediaItem){
        if(!sGenres.containsKey(genre)){
            Genre genreAux = new Genre(genre, new LinkedList<MediaItem>());

            genreAux.getListMovies().add(mediaItem);
            sGenres.put(genre,genreAux);
        }else{
            if(!sGenres.get(genre).getListMovies().contains(mediaItem)){
                sGenres.get(genre).getListMovies().add(mediaItem);
            }
        }
    }

    public Map<String,Genre> getListGenres(){
        return sGenres;
    }

    public Genre getGenreByName(String name){
        return sGenres.get(name);
    }
    public Genre getGenre(int position){
        Genre g =  (Genre)sGenres.values().toArray()[position];
        return g;
    }
}
