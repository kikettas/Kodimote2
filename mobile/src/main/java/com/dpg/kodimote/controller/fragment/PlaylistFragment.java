package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.PlaylistAdapter;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.controller.parser.JSONParser;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.Utils;
import com.dpg.kodimote.view.SpacesItemDecoration;
import com.dpg.kodimote.view.SwipeableRecyclerViewTouchListener;

import org.json.JSONObject;

public class PlaylistFragment extends Fragment {

    public static final String TAG = PlaylistFragment.class.getSimpleName();
    private RecyclerView mPlaylistRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlaylistAdapter mAdapter;

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
        mAdapter = new PlaylistAdapter(getContext());

        mPlaylistRecyclerView = (RecyclerView) rootView.findViewById(R.id.playlist_list);

        //mPlaylistRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());

        mPlaylistRecyclerView.setLayoutManager(mLayoutManager);
        mPlaylistRecyclerView.addItemDecoration(new SpacesItemDecoration(30));

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mPlaylistRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return false;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    MediaCollection.getInstance().remoteItemPlaylist(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });


        mPlaylistRecyclerView.setAdapter(mAdapter);
        //mPlaylistRecyclerView.addOnItemTouchListener(swipeTouchListener);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        //getPlaylist();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getPlaylist() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + getString(R.string.get_playlist_items);
        Log.d(TAG, "getPlaylist");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONParser.parsePlaylist(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);

    }

}
