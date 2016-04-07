package com.dpg.kodimote.controller.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.EpisodeAdapter;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.Season;
import com.dpg.kodimote.models.TVShow;
import com.dpg.kodimote.view.CoordinatedImageView;
import com.dpg.kodimote.view.ExpandableHeightGridView;

public class SeasonActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = SeasonActivity.class.getSimpleName();
    private TVShow mTVShow;
    private Season mCurrentSeason;
    private TextView mTVShowName;
    private TextView mNameSeason;
    private TextView mEpisodesCount;
    private ImageView mNetwork;
    private CoordinatedImageView mCoordinatePasterImageView;
    private Toolbar mToolbar;

    private ExpandableHeightGridView mGridEpisodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_season);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout_season);
        appBarLayout.addOnOffsetChangedListener(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mTVShow = MediaCollection.getInstance().getTVShowByID(getIntent().getLongExtra("TVShowID", -1));
        mCurrentSeason = mTVShow.getSeasonByID(getIntent().getIntExtra("seasonid", -1));


        mNetwork = (ImageView)findViewById(R.id.season_network);
        mNetwork.setImageDrawable(getResourceNetworkID(mTVShow.getListNetworks().get(0)));

        mTVShowName = (TextView) findViewById(R.id.tvshow_name);
        mTVShowName.setText(mTVShow.getName());
        mNameSeason = (TextView) findViewById(R.id.season_name);
        mNameSeason.setText("Season " + mCurrentSeason.getSeasonNumber());

        mEpisodesCount = (TextView) findViewById(R.id.episodes_count);
        String episodesCountText = mCurrentSeason.getEpisodeList().size() + " episodes";
        if(mCurrentSeason.getEpisodeList().size()<2){
            episodesCountText = episodesCountText.substring(0,episodesCountText.length()-1);
        }
        mEpisodesCount.setText(episodesCountText);

        mCoordinatePasterImageView = (CoordinatedImageView) findViewById((R.id.poster_season));

        ImageRequest fanartRequest = new ImageRequest(mCurrentSeason.getPosterUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mCoordinatePasterImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueueSingleton.getInstance(this).addToRequestQueue(fanartRequest);

        ImageRequest request = new ImageRequest(mCurrentSeason.getFanartUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        appBarLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,error.getMessage());
                    }
                });
        // Access the RequestQueue through your singleton class.
        request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);


        mGridEpisodes = (ExpandableHeightGridView) findViewById(R.id.grid_episodes);
        mGridEpisodes.setExpanded(true);
        mGridEpisodes.setFocusable(false);
        mGridEpisodes.setAdapter(new EpisodeAdapter(getApplicationContext(), mCurrentSeason));
        mGridEpisodes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),FilmActivity.class);
                intent.putExtra(FilmActivity.MEDIA_TYPE,FilmActivity.EPISODE_TYPE);
                intent.putExtra(FilmActivity.ID_TVSHOW_SELECTED,mTVShow.getMediaID());
                intent.putExtra(FilmActivity.ID_SEASON_SELECTED,mCurrentSeason.getSeasonID());
                intent.putExtra(FilmActivity.EPISODE_SELECTED,position);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        float statusBarHeight = (appBarLayout.getY() - verticalOffset);
        if(verticalOffset == -(appBarLayout.getHeight() - mToolbar.getMinimumHeight())){
            Log.d(TAG,"verticalOffeset: Collapsed");
            this.setTitle(mTVShow.getName());

        }else{
            Log.d(TAG,"-----------------------------------------------------------");
            startAlphaAnimation(mToolbar,200,View.INVISIBLE);
            Log.d(TAG,"verticalOffset: "+verticalOffset);
            Log.d(TAG,"appBarLayout Height: "+ appBarLayout.getHeight());
            Log.d(TAG,"appBarLayout Y: "+ appBarLayout.getY());
            Log.d(TAG,"appBarLayout Measured Height: "+ appBarLayout.getMeasuredHeight());
            Log.d(TAG,"appBarLayout Minimun Height: "+ appBarLayout.getMinimumHeight());
            Log.d(TAG,"Toolbar Y: "+mToolbar.getY());
            Log.d(TAG,"Toolbar Height"+mToolbar.getHeight());
            Log.d(TAG,"Toolbar Measured Height: "+mToolbar.getMeasuredHeight());
            Log.d(TAG,"Toolbar Minimun Height: "+mToolbar.getMinimumHeight());
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public Drawable getResourceNetworkID(String network){
        // TODO Make a deafault network logo
        Drawable drawableNetwork = null;
        if(network.equals("FX") || network.equals("FX (US)")){
            drawableNetwork =  getResources().getDrawable(R.drawable.fx);
        }else{
            if(network.equals("AMC")){
                drawableNetwork =  getResources().getDrawable(R.drawable.amc);
            }else{
                if(network.equals("Netflix")){
                    drawableNetwork =  getResources().getDrawable(R.drawable.netflix);
                }else{
                    if(network.equals("Lifetime")){
                        drawableNetwork =  getResources().getDrawable(R.drawable.lifetime);
                    }else{
                        if(network.equals("CBS")){
                            drawableNetwork =  getResources().getDrawable(R.drawable.cbs);
                        }else{
                            if(network.equals("USA Network")){
                                drawableNetwork =  getResources().getDrawable(R.drawable.usanetwork);
                            }else{
                                if(network.equals("Showtime")){
                                    drawableNetwork =  getResources().getDrawable(R.drawable.showtime);
                                }else{
                                    if(network.equals("HBO")){
                                        drawableNetwork =  getResources().getDrawable(R.drawable.hbo);
                                    }else{
                                        if(network.equals("BBC Two")){
                                            drawableNetwork =  getResources().getDrawable(R.drawable.bbctwo);
                                        }else{
                                            if(network.equals("History")){
                                                drawableNetwork =  getResources().getDrawable(R.drawable.history);
                                            }else if(network.equals("Abc")){
                                                drawableNetwork = getResources().getDrawable(R.drawable.abc);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return drawableNetwork;
    }
}
