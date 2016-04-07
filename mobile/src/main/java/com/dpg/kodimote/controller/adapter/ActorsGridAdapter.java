package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.Actor;
import com.dpg.kodimote.models.Episode;
import com.dpg.kodimote.models.MediaCollection;

/**
 * Created by enriquedelpozogomez on 29/01/16.
 */
public class ActorsGridAdapter extends BaseAdapter {
    public static String TAG = ActorsGridAdapter.class.getSimpleName();
    private Context mContext = null;
    private long mMediaId = -1;
    private long mTVShowId = -1;
    private int mSeasonId = -1;

    private ImageView mPosterNetworkImageView;
    private ImageLoader mImageLoader;

    public ActorsGridAdapter(Context context, long mediaid, long tvshowid, int seasonid) {
        super();
        this.mContext = context;
        this.mMediaId = mediaid;
        this.mTVShowId = tvshowid;
        this.mSeasonId = seasonid;
    }

    @Override
    public int getCount() {
        if (mTVShowId == -1) {
            return MediaCollection.getInstance().getMovieByID(this.mMediaId).getActors().size();
        } else {
            return MediaCollection.getInstance().getTVShowByID(mTVShowId).getSeasonByID(mSeasonId).getEpisodeByID(mMediaId).getActors().size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mTVShowId == -1) {
            return MediaCollection.getInstance().getMovieByID(this.mMediaId).getActors().get(position);
        } else {
            Episode e = MediaCollection.getInstance().getTVShowByID(mTVShowId).getSeasonByID(mSeasonId).getEpisodeByID(mMediaId);
            return e.getActors().get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        if (mTVShowId == -1) {
            return MediaCollection.getInstance().getMovieByID(this.mMediaId).getActors().get(position).getActorID();
        } else {
            return MediaCollection.getInstance().getTVShowByID(mTVShowId).getSeasonByID(mSeasonId).getEpisodeByID(mMediaId).getActors().get(position).getActorID();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.actor_grid_item, parent, false);
        }

        ImageView photoDirectorImageView = (ImageView) convertView.findViewById(R.id.image_photo_actor);
        TextView nameDirectorImageView = (TextView) convertView.findViewById(R.id.name_actor);

        Actor actor = (Actor) getItem(position);


        mPosterNetworkImageView = (ImageView) convertView.findViewById(R.id.image_photo_actor);
        // Get the ImageLoader through your singleton class.
        mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader(); //
        // mImageLoader.get(film.getPoster(), ImageLoader.getImageListener(imageMoviePoster,
        // R.drawable.poster, R.drawable.poster));

        if(actor.getPhotoUrl() != null){
            Log.d(TAG,actor.getPhotoUrl());
        }else{
            Log.d(TAG,actor.getName());
        }
        mImageLoader.get(actor.getPhotoUrl(), ImageLoader.getImageListener(mPosterNetworkImageView,
                R.drawable.empty_avatar, R.drawable.empty_avatar));

//        mPosterNetworkImageView.setImageUrl(actor.getPhotoUrl(), mImageLoader);
        nameDirectorImageView.setText(actor.getName());

        return convertView;
    }
}
