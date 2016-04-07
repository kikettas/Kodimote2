package com.dpg.kodimote.controller.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;
import com.dpg.kodimote.models.Utils;

import org.json.JSONObject;

public class FilmActivity extends AppCompatActivity {
    public static final String ID_FILM_SELECTED = "id_film_selected";
    public static final String ID_TVSHOW_SELECTED = "id_tvshow_selected";
    public static final String ID_SEASON_SELECTED = "id_season_selected";
    public static final String EPISODE_SELECTED = "episode_selected";
    public static final int FILM_TYPE = 0;
    public static final int EPISODE_TYPE = 1;
    private boolean mIsMovie = false;


    public static final String MEDIA_TYPE = "media_type_to_reproduce";
    private static final String TAG = FilmActivity.class.getSimpleName();

    private TextView mMediaSynopsisTextView;
    private TextView mMediaYearTextView;
    private TextView mMediaDurationTextView;
    private TextView mMediaGenresTextView;
    private TextView mMediaDirectorsTextView;
    private TextView mMediaWritersTextView;
    private TextView mMediaCastTextView;
    private Button mPlayTrailerButton;
    private Button mAddToPlayListButton;

    private MediaItem mMediaItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        // Change status bar color for devices over 5.0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


        // Get information from parent activity

        Intent intent = getIntent();

        if (intent.getIntExtra(MEDIA_TYPE, -1) == FILM_TYPE) {
            mMediaItem = MediaCollection.getInstance().getMovieByID(intent.getLongExtra(ID_FILM_SELECTED, -1));
            mIsMovie = true;

        } else if (intent.getIntExtra(MEDIA_TYPE, -1) == EPISODE_TYPE) {
            mMediaItem = MediaCollection.getInstance().getTVShowByID(intent.getLongExtra(ID_TVSHOW_SELECTED, -1)).getSeasonByID(intent.getIntExtra(ID_SEASON_SELECTED, -1)).getEpisodeList().get(intent.getIntExtra(EPISODE_SELECTED, -1));
        }

        // Give a reference to the Toolbar, set Title with film name and set back button.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_film);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout_film);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbar.setTitle(mMediaItem.getName());
        toolbar.setTitleTextColor(Color.BLUE);
        setSupportActionBar(toolbar);

        // Controllers ---> View

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.play_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FilmActivity.this, "Playing", Toast.LENGTH_SHORT).show();
                playVideoOrAddToPlaylist(true,mMediaItem.getLocalFileUrl());
            }
        });

        mMediaSynopsisTextView = (TextView) findViewById(R.id.media_synopsis);
        mMediaYearTextView = (TextView) findViewById(R.id.media_year);
        mMediaDurationTextView = (TextView) findViewById(R.id.media_duration);
        mMediaGenresTextView = (TextView) findViewById(R.id.genres);
        mMediaDirectorsTextView = (TextView) findViewById(R.id.directors);
        mMediaWritersTextView = (TextView) findViewById(R.id.writers);
        mMediaCastTextView = (TextView) findViewById(R.id.cast);
        mPlayTrailerButton = (Button) findViewById(R.id.play_trailer_button);
        mAddToPlayListButton = (Button) findViewById(R.id.add_playlist_button);


        Log.d(TAG, mMediaItem.getActors() + "");


        mPlayTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mMediaItem.getTrailer()));
                startActivity(intent);
            }
        });

        mAddToPlayListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideoOrAddToPlaylist(false, mMediaItem.getLocalFileUrl());
            }
        });

        mMediaSynopsisTextView.setText(mMediaItem.getSynopsis());
        mMediaYearTextView.setText(mMediaItem.getYear() + "");
        mMediaDurationTextView.setText(mMediaItem.getDuration() + "");
        mMediaGenresTextView.setText(Utils.getInstance().listToStringSeparatedByCommas(mMediaItem.getGenres(), Utils.LIST_TYPE_STRINGS));
        mMediaDirectorsTextView.setText(Utils.getInstance().listToStringSeparatedByCommas(mMediaItem.getDirectors(), Utils.LIST_TYPE_PERSON));
        mMediaWritersTextView.setText(Utils.getInstance().listToStringSeparatedByCommas(mMediaItem.getWriters(), Utils.LIST_TYPE_PERSON));
        mMediaCastTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCast = new Intent(getApplicationContext(), CastActivity.class);
                intentCast.putExtra(ID_TVSHOW_SELECTED, getIntent().getLongExtra(ID_TVSHOW_SELECTED, -1));
                intentCast.putExtra(ID_SEASON_SELECTED, getIntent().getIntExtra(ID_SEASON_SELECTED, -1));
                intentCast.putExtra(ID_FILM_SELECTED, mMediaItem.getMediaID()); // If it is an episode, it is used too.

                startActivity(intentCast);
            }
        });


        ImageRequest request = new ImageRequest(mMediaItem.getUrlFanart(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        appBarLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);


    }

    public void playVideoOrAddToPlaylist(boolean isForPlayingVideo, String file) {

        String fileEncode = Utils.getInstance().encodeUrl(file);
        String url;

        if (isForPlayingVideo) {
            url = "http://"+Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort()+"/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.Open\",\"params\":{\"item\":{\"file\":\"" + fileEncode + "\"}}}";
        }else{
            url = "http://"+Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort()+"/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Playlist.Add\",\"params\":{\"item\":{\"file\":\"" + fileEncode + "\"},\"playlistid\":1}}";
        }

        Log.d(TAG, fileEncode);


        Log.d(TAG, getString(R.string.allmovies_url_request));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getApplicationContext(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getApplicationContext(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void addToPlaylist(){

    }
}
