package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.activity.MoviesDirectorOrGenreActivity;
import com.dpg.kodimote.controller.adapter.GenreAdapter;
import com.dpg.kodimote.models.Directors;
import com.dpg.kodimote.models.Genres;

public class GenresFragment extends Fragment {
    private static final String TAG = GenresFragment.class.getSimpleName();
    public static final String GENRE_NAME = "genre_name";
    public static final String SOURCE_TYPE = "source_type";
    public static final int SOURCE_GENRE = 0;


    public GenresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genres, container, false);

        ListView listGenresView = (ListView)rootView.findViewById(R.id.genres_list);

        listGenresView.setAdapter(new GenreAdapter(getContext()));

        listGenresView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),MoviesDirectorOrGenreActivity.class);
                intent.putExtra(SOURCE_TYPE,SOURCE_GENRE);
                intent.putExtra(GENRE_NAME, Genres.getInstance().getGenre(position).getGenreName());
                startActivity(intent);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
