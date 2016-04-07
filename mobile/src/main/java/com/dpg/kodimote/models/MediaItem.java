package com.dpg.kodimote.models;

import java.util.List;

/**
 * Created by enriquedelpozogomez on 21/01/16.
 */
public class MediaItem {

    private String mName = null;
    private int mYear = 0;
    private String mSynopsis = null;
    private Double mRating = null;
    private List<String>  mGenres = null;
    private String mUrlPoster = null;
    private String mUrlFanart = null;
    private long mMediaID = 0;
    private String mTrailer = null;
    private List<Actor> mActors = null;
    private List<Director> mDirectors = null;
    private List<Person> mWriters = null;
    private int mVotes = 0;
    private String mTagline = null;
    private int mDuration = 0;
    private String mVideoQuality = null;
    private List<String> mLanguageAudios = null;
    private List<String> mLanguageSubtitles = null;
    private String mVideoCodec = null;
    private String mLocalFileUrl = null;
    private int mPlayCount = 0;

    public MediaItem(String name, int year, String synopsis, Double rating, List<String> genres, String poster, String fanart,
                     long mediaID, String trailer, List<Actor> actors, List<Director> director, List<Person> writers, int votes,
                     String tagline, int duration, String videoQuality, List<String> languageAudios, List<String> languageSubtitles,
                     String videoCodec, String localFileUrl, int playCount) {
        mName = name;
        mYear = year;
        mSynopsis = synopsis;
        mRating = rating;
        mGenres = genres;
        mUrlPoster = poster;
        mUrlFanart = fanart;
        mMediaID = mediaID;
        mTrailer = trailer;
        mActors = actors;
        mDirectors = director;
        mWriters = writers;
        mVotes = votes;
        mTagline = tagline;
        mDuration = duration;
        mVideoQuality = videoQuality;
        mLanguageAudios = languageAudios;
        mLanguageSubtitles = languageSubtitles;
        mVideoCodec = videoCodec;
        mLocalFileUrl = localFileUrl;
        mPlayCount = playCount;
    }

    public int getPlayCount() {
        return mPlayCount;
    }

    public void setPlayCount(int playCount) {
        mPlayCount = playCount;
    }

    public List<Person> getWriters() {
        return mWriters;
    }

    public void setWriters(List<Person> writers) {
        mWriters = writers;
    }

    public long getMediaID() {
        return mMediaID;
    }

    public void setMediaID(long mediaID) {
        mMediaID = mediaID;
    }

    public String getLocalFileUrl() {
        return mLocalFileUrl;
    }

    public void setLocalFileUrl(String localFileUrl) {
        mLocalFileUrl = localFileUrl;
    }

    public List<Director> getDirectors() {
        return mDirectors;
    }

    public void setDirectors(List<Director> directors) {
        mDirectors = directors;
    }

    public String getVideoCodec() {
        return mVideoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        mVideoCodec = videoCodec;
    }

    public String getVideoQuality() {
        return mVideoQuality;
    }

    public void setVideoQuality(String videoQuality) {
        mVideoQuality = videoQuality;
    }

    public List<String> getLanguageAudios() {
        return mLanguageAudios;
    }

    public void setLanguageAudios(List<String> languageAudios) {
        mLanguageAudios = languageAudios;
    }

    public List<String> getLanguageSubtitles() {
        return mLanguageSubtitles;
    }

    public void setLanguageSubtitles(List<String> languageSubtitles) {
        mLanguageSubtitles = languageSubtitles;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getUrlFanart() {
        return mUrlFanart;
    }

    public void setUrlFanart(String urlFanart) {
        mUrlFanart = urlFanart;
    }

    public String getUrlPoster() {
        return mUrlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        mUrlPoster = urlPoster;
    }


    public String getTrailer() {
        return mTrailer;
    }

    public void setTrailer(String trailer) {
        mTrailer = trailer;
    }

    public List<Actor> getActors() {
        return mActors;
    }

    public void setActors(List<Actor> actors) {
        mActors = actors;
    }

    public int getVotes() {
        return mVotes;
    }

    public void setVotes(int votes) {
        mVotes = votes;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public void setSynopsis(String synopsis) {
        mSynopsis = synopsis;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(Double rating) {
        mRating = rating;
    }

    public List<String> getGenres() {
        return mGenres;
    }

    public void setGenres(List<String> genres) {
        mGenres = genres;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof MediaItem){
            MediaItem item = (MediaItem) o;
            return (item.getName().equals(this.getName()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
}
