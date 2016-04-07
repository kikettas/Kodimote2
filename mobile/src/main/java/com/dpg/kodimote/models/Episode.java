package com.dpg.kodimote.models;

import java.util.List;

/**
 * Created by enriquedelpozogomez on 06/02/16.
 */
public class Episode extends MediaItem {
    private String mFirstAiredDate;
    private String mThumbnailUrl;
    private int mEpisodeNumber;
    private long mTVShowID;
    private int mTVShowLocalID;
    private int mSeasonNumber;

    public Episode(String name, int year, String synopsis, Double rating, List<String> genres, String poster, String fanart, long mediaID, String trailer, List<Actor> actors, List<Director> director, List<Person> writers, int votes, String tagline, int duration, String videoQuality, List<String> languageAudios, List<String> languageSubtitles, String videoCodec, String localFileUrl, String firstAiredDate, String thumbnailUrl, int episodeNumber, long tvshowID, int tvshowLocalID, int seasonNumber, int playCount) {
        super(name, year, synopsis, rating, genres, poster, fanart, mediaID, trailer, actors, director, writers, votes, tagline, duration, videoQuality, languageAudios, languageSubtitles, videoCodec, localFileUrl,playCount);
        mFirstAiredDate = firstAiredDate;
        mThumbnailUrl = thumbnailUrl;
        mEpisodeNumber = episodeNumber;
        mSeasonNumber = seasonNumber;
        mTVShowID = tvshowID;
        mTVShowLocalID = tvshowLocalID;
    }

    public int getTVShowLocalID() {
        return mTVShowLocalID;
    }

    public void setTVShowLocalID(int TVShowLocalID) {
        mTVShowLocalID = TVShowLocalID;
    }

    public long getTVShowID() {
        return mTVShowID;
    }

    public void setTVShowID(long TVShowID) {
        mTVShowID = TVShowID;
    }

    public int getSeasonNumber() {
        return mSeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        mSeasonNumber = seasonNumber;
    }

    public String getFirstAiredDate() {
        return mFirstAiredDate;
    }

    public void setFirstAiredDate(String firstAiredDate) {
        mFirstAiredDate = firstAiredDate;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public int getEpisodeNumber() {
        return mEpisodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        mEpisodeNumber = episodeNumber;
    }
}
