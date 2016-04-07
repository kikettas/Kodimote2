package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.models.Genre;
import com.dpg.kodimote.models.Genres;

/**
 * Project Kodimote created by enriquedelpozogomez on 21/03/16.
 */
public class GenreAdapter extends BaseAdapter {
    private Context mContext;

    public GenreAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Genres.getInstance().getListGenres().size();
    }

    @Override
    public Object getItem(int position) {
        return Genres.getInstance().getGenre(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.genre_list_item, parent,false);
        }

        TextView moviesGenreCountTextView = (TextView) convertView.findViewById(R.id.movies_count);
        TextView nameGenreTextView = (TextView) convertView.findViewById(R.id.name_genre);

        Genre genre = (Genre)getItem(position);

        moviesGenreCountTextView.setText(genre.getListMovies().size()+"");

        nameGenreTextView.setText(genre.getGenreName());

        return convertView;
    }
}
