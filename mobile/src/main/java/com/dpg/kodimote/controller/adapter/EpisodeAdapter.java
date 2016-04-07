package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.Episode;
import com.dpg.kodimote.models.Season;

import java.text.DecimalFormat;

/**
 * Created by enriquedelpozogomez on 09/02/16.
 */
public class EpisodeAdapter extends BaseAdapter{

    private Context mContext;
    private Season mCurrentSeason;
    private TextView mEpisodeName;
    private TextView mEpisodeFirstAired;
    private TextView mEpisodeDuration;
    private TextView mEpisodeRating;
    private NetworkImageView mEpisodePoster;
    private ImageView mTickWatched;
    private Episode mCurrenEpisode;

    public EpisodeAdapter(Context context, Season currentSeason) {
        mContext = context;
        mCurrentSeason = currentSeason;
    }

    @Override
    public int getCount() {
        return mCurrentSeason.getEpisodeList().size();
    }

    @Override
    public Object getItem(int position) {
        return mCurrentSeason.getEpisodeList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return (mCurrentSeason.getEpisodeList().get(position)).getMediaID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.episode_grid_item,parent, false);
        }

        mCurrenEpisode = (Episode)getItem(position);

        mEpisodeName = (TextView) convertView.findViewById(R.id.host_name);
        mEpisodeFirstAired = (TextView) convertView.findViewById(R.id.episode_firstaired);
        mEpisodeDuration = (TextView) convertView.findViewById(R.id.host_ip);
        mEpisodeRating = (TextView) convertView.findViewById(R.id.episode_rating);
        mEpisodePoster = (NetworkImageView) convertView.findViewById(R.id.episode_poster);
        mTickWatched = (ImageView) convertView.findViewById(R.id.tick_watched);


        mEpisodeName.setText(((Episode) getItem(position)).getEpisodeNumber()+". "+mCurrenEpisode.getName());
        mEpisodePoster.setImageUrl(mCurrenEpisode.getThumbnailUrl(), RequestQueueSingleton.getInstance(mContext).getImageLoader());
        DecimalFormat df = new DecimalFormat("#.#");
        mEpisodeRating.setText(""+df.format(mCurrenEpisode.getRating()));
        mEpisodeDuration.setText(mCurrenEpisode.getDuration() + " min");
        mEpisodeFirstAired.setText(mCurrenEpisode.getFirstAiredDate());

        if(mCurrenEpisode.getPlayCount()<1){
            mTickWatched.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
