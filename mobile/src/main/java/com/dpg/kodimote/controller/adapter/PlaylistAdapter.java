package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.Episode;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;


/**
 * Created by enriquedelpozogomez on 23/01/16.
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private Context mContext = null;

    public PlaylistAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public NetworkImageView mPlaylistPosterFilmNetworkImageView;
        public TextView mPlayListNameFilm;
        public TextView mPlayListTagFilm;
        public TextView mPlayListDurationFilm;

        public ViewHolder(View v) {
            super(v);
            mPlaylistPosterFilmNetworkImageView = (NetworkImageView) v.findViewById(R.id.playlist_movie_poster);
            mPlayListNameFilm = (TextView) v.findViewById(R.id.playlist_movie_title);
            mPlayListTagFilm = (TextView) v.findViewById(R.id.playlist_movie_tagline);
            mPlayListDurationFilm = (TextView) v.findViewById(R.id.playlist_movie_duration_year);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_movie_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // ...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MediaItem item = MediaCollection.getInstance().getItemPlaylist(position);

        // TODO Check from local instead of recently downloaded
        if (item != null) {
            holder.mPlaylistPosterFilmNetworkImageView.setImageUrl(item.getUrlPoster(), RequestQueueSingleton.getInstance(mContext).getImageLoader());
            holder.mPlayListNameFilm.setText(item.getName());
            if (item instanceof Episode) {
                Episode episodeAux = (Episode) item;
                holder.mPlayListTagFilm.setText(MediaCollection.getInstance().getTVShowByLocalID(episodeAux.getTVShowLocalID()).getName() + "\n" + mContext.getString(R.string.season) + " " + episodeAux.getSeasonNumber() + " | " + mContext.getString(R.string.episode) + " " + episodeAux.getEpisodeNumber());
            } else {
                // TODO add music field
                holder.mPlayListTagFilm.setText(item.getTagline());
            }
            holder.mPlayListDurationFilm.setText(item.getDuration() + "min");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return MediaCollection.getInstance().getPlaylistCount();
    }


}


