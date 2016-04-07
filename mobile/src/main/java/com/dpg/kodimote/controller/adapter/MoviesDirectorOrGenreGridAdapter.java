package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.Actor;
import com.dpg.kodimote.models.Director;
import com.dpg.kodimote.models.Episode;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;

import java.util.List;

/**
 * Created by enriquedelpozogomez on 29/01/16.
 */
public class MoviesDirectorOrGenreGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<MediaItem> mListMovies;
    private NetworkImageView mPosterNetworkImageView;
    private TextView mMovieTitle;
    private TextView mMovieTagline;
    private TextView mMovieDurationYear;

    public MoviesDirectorOrGenreGridAdapter(Context context, List<MediaItem> listMovies) {
        mContext = context;
        mListMovies = listMovies;
    }

    @Override
    public int getCount() {
        return mListMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mListMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mListMovies.get(position).getMediaID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movies_director_grid_item, parent, false);
        }
        mPosterNetworkImageView = (NetworkImageView)convertView.findViewById(R.id.director_movie_poster);
        mMovieTitle = (TextView) convertView.findViewById(R.id.director_movie_title);
        mMovieTagline = (TextView) convertView.findViewById(R.id.director_movie_tagline);
        mMovieDurationYear = (TextView) convertView.findViewById(R.id.director_movie_duration_year);

        MediaItem movie = (MediaItem)getItem(position);

        mPosterNetworkImageView.setImageUrl(movie.getUrlPoster(), RequestQueueSingleton.getInstance(mContext).getImageLoader());
        mMovieTitle.setText(movie.getName());
        mMovieTagline.setText(movie.getTagline());
        mMovieDurationYear.setText(movie.getYear()+" | "+movie.getDuration()+"min");



        return convertView;
    }
}
