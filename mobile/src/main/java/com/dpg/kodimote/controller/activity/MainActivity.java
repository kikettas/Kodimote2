package com.dpg.kodimote.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.fragment.DirectorsFragment;
import com.dpg.kodimote.controller.fragment.MainRemoteFragment;
import com.dpg.kodimote.controller.fragment.MoviesCollectionFragment;
import com.dpg.kodimote.controller.fragment.MoviesFragment;
import com.dpg.kodimote.controller.fragment.RemoteFragment;
import com.dpg.kodimote.controller.fragment.SeriesCollectionFragment;
import com.dpg.kodimote.controller.fragment.SeriesFragment;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.controller.parser.JSONParser;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;
import com.dpg.kodimote.models.TVShow;
import com.dpg.kodimote.models.Utils;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SeriesFragment.OnFragmentInteractionListener,
        MoviesFragment.OnFragmentInteractionListener,
        RemoteFragment.OnFragmentInteractionListener,
        MoviesCollectionFragment.OnFragmentInteractionListener,
        DirectorsFragment.OnFragmentInteractionListener,
        SeriesCollectionFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String IS_FIRST_STARTUP = "is_first_startup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),this.MODE_PRIVATE);

        setTitle("Remote");

        if(sharedPreferences.getBoolean(IS_FIRST_STARTUP,true)){
            Log.d(TAG,"IS first startup");
            startActivity(new Intent(this, FirstStartActivity.class));
        }else {
            Log.d(TAG, "ISN'T first startup");
            Utils.getInstance().setHostProperties(sharedPreferences.getString(AddingMediaCenterActivity.CURRENT_HOST_NAME,"No host connected"),sharedPreferences.getString(AddingMediaCenterActivity.CURRENT_HOST_IP,""),sharedPreferences.getString(AddingMediaCenterActivity.CURRENT_HOST_PORT,""));

            MainRemoteFragment remoteFragment = new MainRemoteFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, remoteFragment)
                    .commit();


            //ViewServer.get(this).addWindow(this);


            setContentView(R.layout.activity_main);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            if (savedInstanceState != null) {
                Log.d(TAG, "savedInstanceState");
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            ImageView changeHostButton = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.change_kodi_button);
            changeHostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),AddingMediaCenterActivity.class));
                }
            });

            TextView hostNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.host_name);
            hostNameTextView.setText(Utils.getInstance().getCurrentHostName());

            TextView hostIPTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.host_ip);
            hostIPTextView.setText(Utils.getInstance().getCurrentHostIP());


            downloadLibraryFromRemote();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putString("A", "E");
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.update_library:
                Log.d(TAG,"Update library");
                generalJSONRequest("VideoLibrary.Scan");
                downloadLibraryFromRemote();

                break;
            case R.id.clean_library:
                Log.d(TAG,"Clean library");
                generalJSONRequest("VideoLibrary.Clean");

                break;
            case R.id.reboot_system:
                Log.d(TAG,"Reboot system");
                generalJSONRequest("System.Reboot");

                break;
            case R.id.shutdown_system:
                Log.d(TAG,"Shutdown system");
                generalJSONRequest("System.Shutdown");


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.remote) {
            MainRemoteFragment remoteFragment = new MainRemoteFragment();
            replaceFragment(remoteFragment);
        } else if (id == R.id.movies) {
            MoviesFragment moviesFragment = new MoviesFragment();
            replaceFragment(moviesFragment);

        } else if (id == R.id.tvshows) {
            SeriesFragment seriesFragment = new SeriesFragment();
            replaceFragment(seriesFragment);
            for(TVShow tvs:MediaCollection.getInstance().getAllTVShows()){
                Log.d(TAG,tvs.getName()+" "+tvs.getSeasonsList().size());
            }


        } else if (id == R.id.music) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // Method for replacing existent fragments.
    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void downloadLibraryFromRemote() {
        String url = "http://"+Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort() + getString(R.string.allmovies_url_request);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.parseMovies(response, getApplicationContext());
                        Toast.makeText(getApplicationContext(), "Movies updated", Toast.LENGTH_SHORT).show();
                        downloadTVShowsFromRemote();
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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void downloadTVShowsFromRemote() {
        String url = "http://"+Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort() + getString(R.string.allseries_url_request);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.parseTVShows(response, getApplicationContext());

                        Toast.makeText(getApplicationContext(), "Series updated", Toast.LENGTH_SHORT).show();
                        for (TVShow tvShow : MediaCollection.getInstance().getAllTVShows()) {
                            downloadSeasonsForEachTVShow(tvShow.getMediaID(), tvShow.getTVShowLocalID());
                        }


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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void downloadSeasonsForEachTVShow(final Long tvshowid, int tvShowLocalId) {
        String url = "http://"+Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort()+"/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"VideoLibrary.GetSeasons\",\"params\":{\"tvshowid\":" + tvShowLocalId +",\"properties\":[\"showtitle\",\"thumbnail\",\"watchedepisodes\",\"fanart\",\"art\",\"season\"]}}";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.parseSeasonsTVShows(response,tvshowid);

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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    public void generalJSONRequest(String method) {
        String url = "http://"+Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort()+"/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\""+method+"\"}";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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

    /**
     * All interaction Activity-Fragments methods
     * For further information about it:
     * http://developer.android.com/intl/es/training/basics/fragments/communicating.html
     */
    @Override
    public void onSerieSelected(int position) {

    }

    @Override
    //This method is unused, because we only use the other one with the same name
    public void onMovieSelected(Uri uri) {

    }

    @Override
    public void onRemoteButtonPressed(Uri uri) {

    }

    @Override
    public void onDirectorSelected(int i) {

    }

    @Override
    public void onMovieSelectedCollection(MediaItem mediaItem) {

    }
}