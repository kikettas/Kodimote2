package com.dpg.kodimote.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.ListHostsAdapter;
import com.dpg.kodimote.controller.network.NsdHelper;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.Directors;
import com.dpg.kodimote.models.MediaCollection;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class AddingMediaCenterActivity extends AppCompatActivity implements NsdHelper.OnDeviceDetectedListener{
    public static final String TAG = AddingMediaCenterActivity.class.getSimpleName();
    public static final String SET_HOSTS_KODI = "set_hosts_kodi";
    public static final String CURRENT_HOST_IP = "current_host_ip";
    public static final String CURRENT_HOST_NAME = "current_host_name";
    public static final String CURRENT_HOST_PORT = "current_host_port";
    private String mLocalIPv4;
    private String mRootIpNetwork;
    private ListHostsAdapter mHostListAdapter;
    private ListView mHostListView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    private boolean hostConnectionWorks = false;
    private NsdHelper mDiscoverHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_media_center);

        mSharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),this.MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        mHostListView = (ListView)findViewById(R.id.new_kodidevices_listview);
        mHostListAdapter = new ListHostsAdapter(this);
        mHostListView.setAdapter(mHostListAdapter);

        mHostListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hostKodi = "";
                List<String> hostList = new LinkedList<>();
                hostList.addAll(mSharedPreferences.getStringSet(AddingMediaCenterActivity.SET_HOSTS_KODI,new HashSet<String>()));

                testHostConnection(hostList.get(position));
            }
        });

        startDiscoveringKodiDevices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDiscoveringKodiDevices();
        Log.d(TAG,"onResume");
    }

    private void startDiscoveringKodiDevices() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.adding_media_centers_mainlayout),"Searching for new Kodi Media Centers...",Snackbar.LENGTH_LONG);
        snackbar.show();

        mSharedPreferencesEditor.putStringSet(SET_HOSTS_KODI,new HashSet<String>());
        mSharedPreferencesEditor.commit();
        mHostListAdapter.notifyDataSetChanged();

        mDiscoverHelper = new NsdHelper(this);
        mDiscoverHelper.initializeNsd();
        mDiscoverHelper.discoverServices();
    }

    @Override
    public void OnHostDetected() {
        mHostListAdapter.notifyDataSetChanged();
        Log.d(TAG,"Nuevo Host");
    }

    public void testHostConnection(String host){
        final String hostName = host.split(":")[0];
        final String hostIp = host.split(":")[1];
        final String hostPort = host.split(":")[2];

        final Snackbar snackbar = Snackbar.make(findViewById(R.id.adding_media_centers_mainlayout),"Testing connection to "+hostName,Snackbar.LENGTH_LONG);
        String url = "http://"+hostIp.substring(1)+":"+hostPort+getString(R.string.ping_json);

        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                Log.d(TAG,"Connection works: "+hostConnectionWorks);
            }
        });

        Log.d(TAG, url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,response.toString());
                        mSharedPreferencesEditor.putString(CURRENT_HOST_NAME,(hostName));
                        mSharedPreferencesEditor.putString(CURRENT_HOST_IP,(hostIp.substring(1)));
                        mSharedPreferencesEditor.putString(CURRENT_HOST_PORT,(hostPort));
                        mSharedPreferencesEditor.putBoolean(MainActivity.IS_FIRST_STARTUP,false);
                        mSharedPreferencesEditor.commit();

                        mDiscoverHelper.stopDiscovery();
                        hostConnectionWorks = true;

                        MediaCollection.getInstance().wipeMediaCollection();
                        Directors.getInstance().wipeDirectorsCollection();

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

        snackbar.show();
        // Access the RequestQueue through your singleton class.

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

}
