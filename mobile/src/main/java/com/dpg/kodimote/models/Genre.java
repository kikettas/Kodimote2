package com.dpg.kodimote.models;

import java.util.List;

/**
 * Project Kodimote created by enriquedelpozogomez on 21/03/16.
 */
public class Genre {

    private List<MediaItem> mListMovies;
    private String mGenreName;

    public Genre(String genreName, List<MediaItem> listMovies) {
        this.mListMovies = listMovies;
        this.mGenreName = genreName;
    }

    public List<MediaItem> getListMovies() {
        return mListMovies;
    }

    public void setListMovies(List<MediaItem> listMovies) {
        this.mListMovies = listMovies;
    }

    public String getGenreName() {
        return mGenreName;
    }

    public void setGenreName(String genreName) {
        this.mGenreName = genreName;
    }
}
