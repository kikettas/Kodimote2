package com.dpg.kodimote.controller.parser;

import android.content.Context;
import android.util.Log;

import com.dpg.kodimote.controller.fragment.RemoteFragment;
import com.dpg.kodimote.models.Actor;
import com.dpg.kodimote.models.Director;
import com.dpg.kodimote.models.Directors;
import com.dpg.kodimote.models.Episode;
import com.dpg.kodimote.models.Genre;
import com.dpg.kodimote.models.Genres;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;
import com.dpg.kodimote.models.Person;
import com.dpg.kodimote.models.Season;
import com.dpg.kodimote.models.TVShow;
import com.dpg.kodimote.models.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class JSONParser {
    public static String TAG = JSONParser.class.getSimpleName();

    public static void parseMovies(JSONObject response, Context context) {

        try {
            MediaItem mediaItem;
            Director director;

            int nFilms = response.getJSONObject("result").getJSONArray("movies").length();

            for (int i = 0; i < nFilms; i++) {
                JSONObject filmJSON = null;
                JSONObject actorCast = null;
                List<Director> directorsFilm = new LinkedList<>();

                try {
                    filmJSON = (JSONObject) response.getJSONObject("result").getJSONArray("movies").get(i);

                    // Check if directors array is empty and if It isn't, we add it yo Directors Collection
                    if (filmJSON.getJSONArray("director").length() > 0) {
                        for (int j = 0; j < filmJSON.getJSONArray("director").length(); j++) {
                            director = Directors.getInstance().getDirectorByName(filmJSON.getJSONArray("director").getString(j));
                            if (director == null) {
                                director = new Director("urlfoto", filmJSON.getJSONArray("director").getString(j), "", null, null, new LinkedList<MediaItem>() {
                                }, j);
                            }
                            directorsFilm.add(director);
                        }
                    }

                    // Parsing Genres
                    List<String> listGenres = new LinkedList<>();
                    for(int j =0;j<filmJSON.getJSONArray("genre").length();j++){
                        listGenres.add(filmJSON.getJSONArray("genre").getString(j));
                    }

                    // Parsing writers

                    List <Person> writersFilm = new LinkedList<>();
                    Person writer;
                    for(int j = 0;j<filmJSON.getJSONArray("writer").length();j++){
                        writer = new Person(null,filmJSON.getJSONArray("writer").getString(j),null,null,null);
                        writersFilm.add(writer);
                    }


                    // Parsing the Cast
                    List<Actor> actorsList = new LinkedList<>();
                    Actor actor = null;
                    if (filmJSON.getJSONArray("cast").length() > 0) {
                        for (int j = 0; j < filmJSON.getJSONArray("cast").length(); j++) {
                            String thumbnailUrl = "";
                            String finalThumbnailUrl = "";
                            actorCast = (JSONObject) (filmJSON.getJSONArray("cast")).get(j);
                            if (!actorCast.isNull("thumbnail")) {
                                thumbnailUrl = actorCast.getString("thumbnail");
                                finalThumbnailUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), thumbnailUrl);
                            }
                            actor = new Actor(finalThumbnailUrl, actorCast.getString("name"), "", null, null, null, actorCast.getString("name").hashCode(), actorCast.getString("role"));
                            actorsList.add(actor);
                        }
                    }


                    // we deal with decoded url from every film, for fanart, poster and trailer.
                    String posterUrl = filmJSON.getJSONObject("art").getString("poster");
                    String fanartUrl = filmJSON.getJSONObject("art").getString("fanart");
                    String trailerURL = transformUrlTrailer(filmJSON.getString("trailer"));
                    String finalPosterUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), posterUrl);
                    String finalFanartUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), fanartUrl);

                    // We add every film to movies collection
                    // TODO change variables


                    mediaItem = new MediaItem(filmJSON.getString("label"), filmJSON.getInt("year"), filmJSON.getString("plot"), filmJSON.getDouble("rating"),
                            listGenres, finalPosterUrl, finalFanartUrl, Long.parseLong(filmJSON.getString("imdbnumber").substring(2)), trailerURL,
                            actorsList, directorsFilm, writersFilm, 0, filmJSON.getString("tagline"), filmJSON.getInt("runtime") / 60, null, null, null, null, filmJSON.getString("file"),filmJSON.getInt("playcount"));

                    MediaCollection.getInstance().addFilmToCollection(mediaItem);

                    addDirectorsToDirectorsCollection(directorsFilm, mediaItem);
                    addGenresToGenresCollection(listGenres,mediaItem);

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage() + " " + filmJSON.getString("label"));
                }
            }

            Log.d(TAG, "FINISHED!!!!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseTVShows(JSONObject response, Context context) {
        try {
            int nTVShows = response.getJSONObject("result").getJSONArray("tvshows").length();

            for (int i = 0; i < nTVShows; i++) {

                JSONObject tvshowJSON = (JSONObject) response.getJSONObject("result").getJSONArray("tvshows").get(i);

                Log.d(TAG, tvshowJSON.getJSONArray("studio").toString()+" "+tvshowJSON.getInt("season")+" "+tvshowJSON.getInt("episode")+" "+tvshowJSON.getString("label"));

                List<String> listNetworks = new LinkedList<>();

                // Parsing Studios/Networks
                for(int j = 0; j<tvshowJSON.getJSONArray("studio").length();j++){
                    listNetworks.add(tvshowJSON.getJSONArray("studio").get(j).toString());
                }


                // Parsing Genres
                List<String> listGenres = new LinkedList<>();
                for(int j =0;j<tvshowJSON.getJSONArray("genre").length();j++){
                    listGenres.add(tvshowJSON.getJSONArray("genre").getString(j));
                }

                String posterUrl = tvshowJSON.getJSONObject("art").getString("poster");
                String fanartUrl = tvshowJSON.getJSONObject("art").getString("fanart");
                String bannerUrl = tvshowJSON.getJSONObject("art").getString("banner");

                String finalPosterUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), posterUrl);
                String finalFanartUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), fanartUrl);
                String finalBannerUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), bannerUrl);
                int nStudios = tvshowJSON.getJSONArray("studio").length();
                for(int j = 0; j<nStudios;j++){
                    listNetworks.add((String)tvshowJSON.getJSONArray("studio").get(j));
                }

                TVShow tvShow = new TVShow(tvshowJSON.getString("label"), tvshowJSON.getInt("year"), tvshowJSON.getString("plot"), tvshowJSON.getDouble("rating"), listGenres, finalPosterUrl,
                        finalFanartUrl, Long.parseLong(tvshowJSON.getString("imdbnumber")),tvshowJSON.getInt("votes"),"tag",new LinkedList<Season>(), finalBannerUrl,listNetworks,tvshowJSON.getInt("tvshowid"),tvshowJSON.getInt("episode"),tvshowJSON.getInt("season"),tvshowJSON.getInt("playcount"));

                MediaCollection.getInstance().addTVShowToCollection(tvShow);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parseSeasonsTVShows(JSONObject response, Long tvShowImdbId) {
        try {
            int nSeasons = response.getJSONObject("result").getJSONArray("seasons").length();

            List<Season> listSeason = new LinkedList<>();

            for (int i = 0; i < nSeasons; i++) {
                JSONObject nSeasonJSON = (JSONObject) response.getJSONObject("result").getJSONArray("seasons").get(i);

                String posterUrl = nSeasonJSON.getJSONObject("art").getString("tvshow.poster");
                String fanartUrl = nSeasonJSON.getJSONObject("art").getString("tvshow.fanart");
                String bannerUrl = nSeasonJSON.getJSONObject("art").getString("tvshow.banner");

                String finalPosterUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), posterUrl);
                String finalFanartUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), fanartUrl);
                String finalBannerUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), bannerUrl);

                Season season = new Season(null,finalBannerUrl,finalFanartUrl,finalPosterUrl,nSeasonJSON.getInt("season"),nSeasonJSON.getInt("seasonid"),nSeasonJSON.getString("label"),nSeasonJSON.getInt("watchedepisodes"));
                listSeason.add(season);
            }

            MediaCollection.getInstance().getTVShowByID(tvShowImdbId).setSeasonsList(listSeason);

            List<Season> listl= MediaCollection.getInstance().getTVShowByID(tvShowImdbId).getSeasonsList();
            listl.size();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parseEpisodes(JSONObject response, Long tvShowImdbId, int seasonID, int seasonNumber) {
        try {
            int nEpisodes = response.getJSONObject("result").getJSONArray("episodes").length();

            List<Episode> listEpisodes = new LinkedList<>();
            List<Director> directorsFilm;
            List<Person> writersFilm;

            TVShow mCurrentTVShow = MediaCollection.getInstance().getTVShowByID(tvShowImdbId);
            Season mCurrentSeason = mCurrentTVShow.getSeasonByID(seasonID);

            for (int i = 0; i < nEpisodes; i++) {
                JSONObject nEpisodeJSON = (JSONObject) response.getJSONObject("result").getJSONArray("episodes").get(i);

                String posterUrl = nEpisodeJSON.getJSONObject("art").getString("tvshow.poster");
                String fanartUrl = nEpisodeJSON.getJSONObject("art").getString("tvshow.fanart");
                String bannerUrl = nEpisodeJSON.getJSONObject("art").getString("tvshow.banner");
                String thumbUrl = "";

                // TODO Check if every single variable is null or not
                if(!nEpisodeJSON.getJSONObject("art").isNull("thumb"))
                {
                    thumbUrl = nEpisodeJSON.getJSONObject("art").getString("thumb");

                }
                String finalPosterUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), posterUrl);
                String finalFanartUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), fanartUrl);
                String finalBannerUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), bannerUrl);
                String finalThumbUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), thumbUrl);

                // Check if directors array is empty and if It isn't, we add it yo Directors Collection
                directorsFilm = new LinkedList<>();
                Director director;
                // TODO Make available to add films without director
                if (nEpisodeJSON.getJSONArray("director").length() > 0) {
                    for (int j = 0; j < nEpisodeJSON.getJSONArray("director").length(); j++) {
                        director = Directors.getInstance().getDirectorByName(nEpisodeJSON.getJSONArray("director").getString(j));
                        if (director == null) {
                            director = new Director("urlfoto", nEpisodeJSON.getJSONArray("director").getString(j), "", null, null, new LinkedList<MediaItem>() {
                            }, j);
                        }
                        directorsFilm.add(director);
                    }
                }

                // Parsing the Cast
                List<Actor> actorsList = new LinkedList<>();
                Actor actor = null;
                JSONObject actorCast;
                if (nEpisodeJSON.getJSONArray("cast").length() > 0) {
                    for (int j = 0; j < nEpisodeJSON.getJSONArray("cast").length(); j++) {
                        String thumbnailUrl = "";
                        String finalThumbnailUrl = "";
                        actorCast = (JSONObject) (nEpisodeJSON.getJSONArray("cast")).get(j);
                        if (!actorCast.isNull("thumbnail")) {
                            thumbnailUrl = actorCast.getString("thumbnail");
                            finalThumbnailUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), thumbnailUrl);
                        }

                        actor = new Actor(finalThumbnailUrl, actorCast.getString("name"), "", null, null, null, actorCast.getString("name").hashCode(), actorCast.getString("role"));
                        actorsList.add(actor);
                    }
                }

                // Parsing writers
                writersFilm = new LinkedList<>();
                Person writer;
                for(int j = 0;j<nEpisodeJSON.getJSONArray("writer").length();j++){
                    writer = new Person(null,nEpisodeJSON.getJSONArray("writer").getString(j),null,null,null);
                    writersFilm.add(writer);
                }

                Episode episode = new Episode(nEpisodeJSON.getString("title"),-1,nEpisodeJSON.getString("plot"),nEpisodeJSON.getDouble("rating"),mCurrentTVShow.getGenres(),finalPosterUrl,finalFanartUrl,nEpisodeJSON.getInt("episode"),null,actorsList,directorsFilm,writersFilm,nEpisodeJSON.getInt("votes"),"",nEpisodeJSON.getInt("runtime")/60,null,null,null,null,nEpisodeJSON.getString("file"),nEpisodeJSON.getString("firstaired"),finalThumbUrl,nEpisodeJSON.getInt("episode"),tvShowImdbId,nEpisodeJSON.getInt("tvshowid"),seasonNumber, nEpisodeJSON.getInt("playcount"));

                listEpisodes.add(episode);

            }

            MediaCollection.getInstance().getTVShowByID(tvShowImdbId).getSeasonByID(seasonID).setEpisodeList(listEpisodes);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parsePlaylist(JSONObject response){
        try {
            int nItemsPlaylist = response.getJSONObject("result").getJSONArray("items").length();

            MediaItem mediaItem = null;

            for (int i = 0; i < nItemsPlaylist; i++) {
                JSONObject nItemJSON = (JSONObject) response.getJSONObject("result").getJSONArray("items").get(i);

                Log.d(TAG, nItemJSON.toString());


                if (nItemJSON.getString("type").equals("movie")) {
                    // we deal with decoded url from every film, for fanart, poster and trailer.
                    String posterUrl = nItemJSON.getJSONObject("art").getString("poster");
                    String fanartUrl = nItemJSON.getJSONObject("art").getString("fanart");

                    String finalPosterUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), posterUrl);
                    String finalFanartUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), fanartUrl);


                    mediaItem = new MediaItem(nItemJSON.getString("label"), nItemJSON.getInt("year"), nItemJSON.getString("plot"), nItemJSON.getDouble("rating"),
                            null, finalPosterUrl, finalFanartUrl, Long.parseLong(nItemJSON.getString("imdbnumber").substring(2)), null,
                            null, null, null, 0, nItemJSON.getString("tagline"), nItemJSON.getInt("runtime") / 60, null, null, null, null, nItemJSON.getString("file"), nItemJSON.getInt("playcount"));
                } else {
                    if (nItemJSON.getString("type").equals("episode")) {

                        String posterUrl = nItemJSON.getJSONObject("art").getString("tvshow.poster");
                        String fanartUrl = nItemJSON.getJSONObject("art").getString("tvshow.fanart");
                        String bannerUrl = nItemJSON.getJSONObject("art").getString("tvshow.banner");
                        String thumbUrl = "";

                        // TODO Check if every single variable is null or not
                        if(!nItemJSON.getJSONObject("art").isNull("thumb"))
                        {
                            thumbUrl = nItemJSON.getJSONObject("art").getString("thumb");
                        }
                        String finalPosterUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), posterUrl);
                        String finalFanartUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), fanartUrl);
                        String finalThumbUrl = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), thumbUrl);

                        mediaItem = new Episode(nItemJSON.getString("label"),-1,nItemJSON.getString("plot"),nItemJSON.getDouble("rating"),null,finalPosterUrl,finalFanartUrl,nItemJSON.getInt("episode"),null,null,null,null,nItemJSON.getInt("votes"),"",nItemJSON.getInt("runtime")/60,null,null,null,null,nItemJSON.getString("file"),null,finalThumbUrl,nItemJSON.getInt("episode"),-1,nItemJSON.getInt("tvshowid"),nItemJSON.getInt("season"), nItemJSON.getInt("playcount"));

                    } else {
                    }
                }

                if(mediaItem!=null){
                    MediaCollection.getInstance().addItemPlaylist(mediaItem);
                }


            }
        } catch (JSONException e) {
            Log.e(TAG,e.getMessage());
        }



    }

    public static int parseServerNotification(String notification){
        int notificationID = -1;
        try {
            JSONObject notificationJSON = new JSONObject(notification);
            if(notificationJSON.get("method").equals("Player.OnPlay")){
                notificationID = RemoteFragment.NOTIFICATION_ONPLAY;
            }else{
                if(notificationJSON.get("method").equals("Player.OnPause")){
                    notificationID = RemoteFragment.NOTIFICATION_ONPAUSE;
                }else{
                    if(notificationJSON.get("method").equals("Player.OnStop")){
                        notificationID = RemoteFragment.NOTIFICATION_ONSTOP;
                    }else{
                        if(notificationJSON.get("method").equals("Player.OnSeek")){
                            notificationID = RemoteFragment.NOTIFICATION_ONSEEK;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notificationID;
    }

    private static String transformUrlTrailer(String previousUrl) {
        String[] splitedPreviousURL = previousUrl.split("videoid=");

        if (splitedPreviousURL.length > 1) {
            String definitiveURL = (previousUrl.split("videoid="))[1];

            if (definitiveURL.length() == 11) {
                return "https://www.youtube.com/watch?v=" + definitiveURL;

            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static void addDirectorsToDirectorsCollection(List<Director> directors, MediaItem mediaItem) {
        for (Director d : directors) {
            Directors.getInstance().addDirectorToCollectionFromFilm(d, mediaItem);
        }
    }

    private static void addGenresToGenresCollection(List<String> genres, MediaItem mediaItem){
        for(String g: genres){
            Genres.getInstance().addMovieToGenre(g,mediaItem);
        }
    }

}
