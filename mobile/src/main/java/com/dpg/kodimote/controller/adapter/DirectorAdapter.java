package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.models.Director;
import com.dpg.kodimote.models.Directors;


/**
 * Created by enriquedelpozogomez on 23/01/16.
 */
public class DirectorAdapter extends BaseAdapter {

    private Context mContext = null;

    public DirectorAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return Directors.getInstance().getCount();
    }

    @Override
    public Object getItem(int position) {
        return Directors.getInstance().getDirector(position);
    }

    @Override
    public long getItemId(int position) {
        return Directors.getInstance().getDirector(position).getDirectorId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.director_list_item, parent,false);
        }

        TextView moviesDirectorCount = (TextView) convertView.findViewById(R.id.movies_count);
        TextView nameDirectorImageView = (TextView) convertView.findViewById(R.id.name_director);


        Director director = (Director)getItem(position);
        moviesDirectorCount.setText(director.getMovies().size()+"");
        nameDirectorImageView.setText(director.getName());


        return convertView;
    }
}
