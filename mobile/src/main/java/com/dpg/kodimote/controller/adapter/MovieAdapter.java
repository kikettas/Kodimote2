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
import com.android.volley.toolbox.NetworkImageView;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;

/**
 * Created by enriquedelpozogomez on 22/01/16.
 */
public class MovieAdapter extends BaseAdapter{

    private Context mContext;
    private TextView nameMovie;
    private ImageLoader mImageLoader;
    private NetworkImageView mPosterNetworkImageView;
    private ImageView mTickWatched;

    public MovieAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return MediaCollection.getInstance().getMoviesCount();
    }

    @Override
    public Object getItem(int position) {
        return MediaCollection.getInstance().getMovie(position);
    }

    @Override
    public long getItemId(int position) {
        return ((MediaItem)getItem(position)).getMediaID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_grid_item, parent,false);
        }

        //nameMovie = (TextView) convertView.findViewById(R.id.name_movie);

        // TODO Fix this mess.
        final MediaItem mediaItem = ((MediaItem)getItem(position));


        mPosterNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.playlist_movie_poster);
        mTickWatched = (ImageView) convertView.findViewById(R.id.tick_watched);

        // Get the ImageLoader through your singleton class.
        mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader(); //
        // mImageLoader.get(film.getPoster(), ImageLoader.getImageListener(imageMoviePoster,
        // R.drawable.poster, R.drawable.poster));
        mPosterNetworkImageView.setImageUrl(mediaItem.getUrlPoster(), mImageLoader);
        if(position == 0){
            Log.d("asda","as");
        }

        if(mediaItem.getPlayCount()<1){
            mTickWatched.setVisibility(View.INVISIBLE);
        }
        //nameMovie.setText(mediaItem.getName());

        return convertView;
    }


}
