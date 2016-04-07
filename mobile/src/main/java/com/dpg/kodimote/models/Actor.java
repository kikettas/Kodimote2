package com.dpg.kodimote.models;

import java.util.Date;
import java.util.List;

/**
 * Project Kodimote created by enriquedelpozogomez on 26/01/16.
 */
public class Actor extends Person {

    private List<MediaItem> mMovies = null;
    private long mActorID = 0;
    private String mCharacter = null;

    public Actor(String photo, String name, String surname, Date birthDate, String birthCountry, List<MediaItem> movies, long actorID, String character) {
        super(photo, name, surname, birthDate, birthCountry);
        mMovies = movies;
        mActorID = actorID;
        mCharacter = character;
    }

    public List<MediaItem> getMovies() {
        return mMovies;
    }

    public void setMovies(List<MediaItem> movies) {
        mMovies = movies;
    }

    public long getActorID() {
        return mActorID;
    }

    public void setActorID(long actorID) {
        mActorID = actorID;
    }
}
