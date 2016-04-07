package com.dpg.kodimote.models;

import java.util.List;

/**
 * Created by enriquedelpozogomez on 06/02/16.
 */
public class Season {
    private List<Episode> mEpisodeList;
    private String mBannerUrl;
    private String mFanartUrl;
    private String mPosterUrl;
    private int mSeasonNumber;
    private int mSeasonID;
    private String mTitle;
    private int mWatchedEpisodes;


    public Season(List<Episode> episodeList, String bannerUrl, String fanartUrl, String posterUrl, int seasonNumber, int seasonID, String title, int watchedEpisodes) {
        mEpisodeList = episodeList;
        mBannerUrl = bannerUrl;
        mFanartUrl = fanartUrl;
        mPosterUrl = posterUrl;
        mSeasonNumber = seasonNumber;
        mSeasonID = seasonID;
        mTitle = title;
        mWatchedEpisodes = watchedEpisodes;
    }

    public Episode getEpisodeByID (long episodeID){
        Episode episodeAux = null;
        for(Episode e: mEpisodeList){
            if (e.getMediaID() == episodeID) {
                episodeAux = e;
            }
        }

        return episodeAux;
    }

    public List<Episode> getEpisodeList() {
        return mEpisodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        mEpisodeList = episodeList;
    }

    public String getBannerUrl() {
        return mBannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        mBannerUrl = bannerUrl;
    }

    public String getFanartUrl() {
        return mFanartUrl;
    }

    public void setFanartUrl(String fanartUrl) {
        mFanartUrl = fanartUrl;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
    }

    public int getSeasonNumber() {
        return mSeasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        mSeasonNumber = seasonNumber;
    }

    public int getSeasonID() {
        return mSeasonID;
    }

    public void setSeasonID(int seasonID) {
        mSeasonID = seasonID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getWatchedEpisodes() {
        return mWatchedEpisodes;
    }

    public void setWatchedEpisodes(int watchedEpisodes) {
        mWatchedEpisodes = watchedEpisodes;
    }
}
