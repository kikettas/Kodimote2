package com.dpg.kodimote.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.ActorsGridAdapter;

public class CastActivity extends AppCompatActivity {

    private GridView mCastGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_cast);
        toolbar.setTitle("Cast");





        mCastGridView = (GridView)findViewById(R.id.cast_grid);
        long movieID = getIntent().getLongExtra(FilmActivity.ID_FILM_SELECTED,-1);

        mCastGridView.setAdapter(new ActorsGridAdapter(getApplicationContext(),movieID, getIntent().getLongExtra(FilmActivity.ID_TVSHOW_SELECTED,-1),getIntent().getIntExtra(FilmActivity.ID_SEASON_SELECTED,-1)));
    }

}
