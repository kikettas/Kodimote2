package com.dpg.kodimote.models;

import java.util.List;

/**
 * Created by enriquedelpozogomez on 06/02/16.
 */
public class TVShow extends MediaItem {

    private List<Season> mSeasonsList;
    private String mBannerUrl;
    private List<String> mListNetworks;
    private int mTVShowLocalID;
    private int mLocalSeasonsCount;
    private int mLocalEpisodesCount;

    public TVShow(String name, int year, String synopsis, Double rating, List<String> genres, String poster, String fanart, long mediaID, int votes, String tagline, List<Season> seasonsList, String banner, List<String> listNetworks, int tvShowLocalID, int localEpisodesCount, int localSeasonsCount, int playCount){
        super(name, year, synopsis, rating, genres, poster, fanart, mediaID, null, null, null, null, votes, tagline, 0, null, null, null, null, null,playCount);
        mSeasonsList = seasonsList;
        mBannerUrl = banner;
        mListNetworks = listNetworks;
        mTVShowLocalID = tvShowLocalID;
        mLocalEpisodesCount = localEpisodesCount;
        mLocalSeasonsCount = localSeasonsCount;
    }


    public Season getSeasonByID(int seasonId){
        Season seasonAux = null;
        for(Season season: mSeasonsList){
            if(season.getSeasonID() == seasonId){
                seasonAux = season;
                break;
            }
        }
        return seasonAux;
    }

    public int getLocalEpisodesCount() {
        return mLocalEpisodesCount;
    }

    public void setLocalEpisodesCount(int localEpisodesCount) {
        mLocalEpisodesCount = localEpisodesCount;
    }

    public int getLocalSeasonsCount() {
        return mLocalSeasonsCount;
    }

    public void setLocalSeasonsCount(int localSeasonsCount) {
        mLocalSeasonsCount = localSeasonsCount;
    }

    public int getTVShowLocalID() {
        return mTVShowLocalID;
    }

    public void setTVShowLocalID(int TVShowLocalID) {
        mTVShowLocalID = TVShowLocalID;
    }

    public List<String> getListNetworks() {
        return mListNetworks;
    }

    public void setListNetworks(List<String> listNetworks) {
        mListNetworks = listNetworks;
    }

    public String getBannerUrl() {
        return mBannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        mBannerUrl = bannerUrl;
    }

    public List<Season> getSeasonsList() {
        return mSeasonsList;
    }

    public void setSeasonsList(List<Season> seasonsList) {
        mSeasonsList = seasonsList;
    }
}
