package com.dpg.kodimote.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by enriquedelpozogomez on 22/01/16.
 */
public class MediaCollection {

    private static MediaCollection sInstance = null;
    private static List<MediaItem> mMovies = null;
    private static List<TVShow> mTVShows = null;
    private static List<MediaItem> mPlaylist = null;

    public static MediaCollection getInstance() {
        if (sInstance == null) {
            sInstance = new MediaCollection();
            mMovies = new LinkedList<>();
            mTVShows = new LinkedList<>();
            mPlaylist = new LinkedList<>();
        }

        return sInstance;
    }

    public MediaItem getItemPlaylist(int position) {
        return mPlaylist.get(position);
    }

    public int getPlaylistCount() {
        return mPlaylist.size();
    }

    public void addItemPlaylist(MediaItem item) {
        if (!mPlaylist.contains(item)) {
            mPlaylist.add(item);
        }
    }

    public void remoteItemPlaylist(int position) {
        mPlaylist.remove(position);
    }

    public void addFilmToCollection(MediaItem mediaItem) {
        if(!mMovies.contains(mediaItem)){
            mMovies.add(mediaItem);
        }
    }

    public int getMoviesCount() {
        return mMovies.size();
    }

    public MediaItem getMovieByID(long id) {
        MediaItem mediaItem = null;
        for (MediaItem f : mMovies) {
            if (f.getMediaID() == id) {
                mediaItem = f;
                break;
            }
        }
        return mediaItem;
    }

    public MediaItem getMovie(int position) {
        return mMovies.get(position);
    }

    public void addTVShowToCollection(TVShow tvShow) {
        if (!mTVShows.contains(tvShow)) {
            mTVShows.add(tvShow);
        }
    }

    public int getTVShowsCount() {
        return mTVShows.size();
    }

    public TVShow getTVShowByID(long id) {
        TVShow tvShow = null;
        for (TVShow f : mTVShows) {
            if (f.getMediaID() == id) {
                tvShow = f;
                break;
            }
        }
        return tvShow;
    }

    public TVShow getTVShowByLocalID(int id) {
        TVShow tvShow = null;
        for (TVShow f : mTVShows) {
            if (f.getTVShowLocalID() == id) {
                tvShow = f;
                break;
            }
        }
        return tvShow;
    }

    public TVShow getTVShow(int position) {
        return mTVShows.get(position);
    }

    public List<TVShow> getAllTVShows() {
        return mTVShows;
    }

    public void wipeMediaCollection() {
        mMovies = new LinkedList<>();
        mTVShows = new LinkedList<>();
    }

}
