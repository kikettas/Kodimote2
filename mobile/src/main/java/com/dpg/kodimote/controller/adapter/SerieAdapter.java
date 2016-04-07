package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.Season;
import com.dpg.kodimote.models.TVShow;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by enriquedelpozogomez on 22/01/16.
 */
public class SerieAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private ImageLoader mImageLoader;
    private NetworkImageView mPosterNetworkImageView;
    private TextView mSeasonName;
    private List<String> list;
    private View mSeparatorView;

    public SerieAdapter(Context context) {
        super();
        this.mContext = context;

        list = new LinkedList<String>();
        list.add("HOla");
        list.add("QUe");
        list.add("tal?");
    }

    @Override
    public int getGroupCount() {
        return MediaCollection.getInstance().getTVShowsCount();
    }

    @Override
    public Object getGroup(int position) {
        return MediaCollection.getInstance().getTVShow(position);
    }

    @Override
    public long getGroupId(int position) {
        return ((TVShow)getGroup(position)).getMediaID();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((TVShow)getGroup(groupPosition)).getSeasonsList().size();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((TVShow)getGroup(groupPosition)).getSeasonsList().get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((Season)getChild(groupPosition,childPosition)).getSeasonID();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.serie_grid_item, parent,false);
        }


        // TODO Fix this mess.
        final TVShow tvShow = ((TVShow)getGroup(groupPosition));


        mPosterNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.playlist_movie_poster);
        mImageLoader = RequestQueueSingleton.getInstance(mContext).getImageLoader();
        mPosterNetworkImageView.setImageUrl(tvShow.getBannerUrl(), mImageLoader);
        mSeparatorView = convertView.findViewById(R.id.separator_view);
        if(isExpanded){
            mSeparatorView.setVisibility(View.GONE);
        }else{
            mSeparatorView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.season_list_item, parent,false);
        }

        mSeasonName = (TextView)convertView.findViewById(R.id.season_name);
        mSeasonName.setText(((Season)getChild(groupPosition,childPosition)).getTitle());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
