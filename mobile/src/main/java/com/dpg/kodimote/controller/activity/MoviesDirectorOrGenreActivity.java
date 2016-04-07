package com.dpg.kodimote.controller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.MoviesDirectorOrGenreGridAdapter;
import com.dpg.kodimote.controller.fragment.DirectorsFragment;
import com.dpg.kodimote.controller.fragment.GenresFragment;
import com.dpg.kodimote.models.Director;
import com.dpg.kodimote.models.Directors;
import com.dpg.kodimote.models.Genre;
import com.dpg.kodimote.models.Genres;
import com.dpg.kodimote.models.MediaItem;

import java.util.List;

public class MoviesDirectorOrGenreActivity extends AppCompatActivity {

    private GridView mMoviesDirectorGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_director_or_genre);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_movies_director);

        List<MediaItem> listMovies = null;

        if(getIntent().getIntExtra(DirectorsFragment.SOURCE_TYPE,-1) == DirectorsFragment.SOURCE_DIRECTOR){
            String directorName = getIntent().getStringExtra(DirectorsFragment.DIRECTOR_NAME);

            Director director = Directors.getInstance().getDirectorByName(directorName);

            toolbar.setTitle(director.getName());
            listMovies = director.getMovies();

        }else if(getIntent().getIntExtra(GenresFragment.SOURCE_TYPE,-1) == GenresFragment.SOURCE_GENRE){
            String genreName = getIntent().getStringExtra(GenresFragment.GENRE_NAME);

            Genre genre = Genres.getInstance().getGenreByName(genreName);

            toolbar.setTitle(genre.getGenreName());
            listMovies = genre.getListMovies();
        }



        mMoviesDirectorGridView = (GridView)findViewById(R.id.movies_director_grid);
        mMoviesDirectorGridView.setAdapter(new MoviesDirectorOrGenreGridAdapter(getApplicationContext(), listMovies));
        mMoviesDirectorGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),FilmActivity.class);
                intent.putExtra(FilmActivity.MEDIA_TYPE,FilmActivity.FILM_TYPE);
                intent.putExtra(FilmActivity.ID_FILM_SELECTED,id);
                startActivity(intent);
            }
        });
    }
}
