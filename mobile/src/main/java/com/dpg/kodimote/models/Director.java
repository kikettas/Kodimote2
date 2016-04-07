package com.dpg.kodimote.models;

import java.util.Date;
import java.util.List;

/**
 * Created by enriquedelpozogomez on 23/01/16.
 */
public class Director extends Person{

    private List<MediaItem> mMovies = null;
    private long mDirectorId = 0;

    public Director(String photo, String name, String surname, Date birthDate, String birthCountry, List<MediaItem> movies, long directorId) {
        super(photo, name, surname, birthDate, birthCountry);
        this.mMovies = movies;
        this.mDirectorId = directorId;
    }

    public List<MediaItem> getMovies() {
        return mMovies;
    }

    public void setMovies(List<MediaItem> movies) {
        mMovies = movies;
    }

    public long getDirectorId() {
        return mDirectorId;
    }

    public void setDirectorId(long directorId) {
        mDirectorId = directorId;
    }

    public boolean equals(Object v) {
        if (v instanceof Director){
            Director ptr = (Director) v;
            return (ptr.getName().equals(this.getName()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }


}
