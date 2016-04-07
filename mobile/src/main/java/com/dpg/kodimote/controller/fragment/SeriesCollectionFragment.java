package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.activity.SeasonActivity;
import com.dpg.kodimote.controller.adapter.SerieAdapter;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.controller.parser.JSONParser;
import com.dpg.kodimote.models.MediaCollection;
import com.dpg.kodimote.models.MediaItem;
import com.dpg.kodimote.models.Season;
import com.dpg.kodimote.models.TVShow;
import com.dpg.kodimote.models.Utils;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeriesCollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeriesCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeriesCollectionFragment extends Fragment{
    private static final String ID_SERIES_SELECTED = "id_series_selected";
    private static final String TAG = SeriesCollectionFragment.class.getSimpleName();
    private Season mSeason;
    private TVShow mTVShow;
    private Intent intent;

    private OnFragmentInteractionListener mListener;

    public SeriesCollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesCollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeriesCollectionFragment newInstance(String param1, String param2) {

        return new SeriesCollectionFragment();
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
        Log.d(TAG,"onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_series_collection, container, false);
        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView_series);

        expandableListView.setAdapter(new SerieAdapter(getContext()));

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG,"Name: "+ id);

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG,"Name: "+ id);

                intent = new Intent(getContext(),SeasonActivity.class);
                mTVShow = MediaCollection.getInstance().getTVShow(groupPosition);
                mSeason = mTVShow.getSeasonsList().get(childPosition);

                intent.putExtra("TVShowID",mTVShow.getMediaID());
                intent.putExtra("seasonid",mSeason.getSeasonID());


                // When you are downloading the episodes for a specified season, you pass the TVShowLocalId not the MediaID
                downloadEpisodesForTVShow();


                return false;
            }
        });


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        Log.d(TAG,"onAttach");

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG,"onDetach");

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMovieSelectedCollection(MediaItem mediaItem);
    }

    public void downloadEpisodesForTVShow() {
        String url = "http://"+ Utils.getInstance().getCurrentHostIP()+":"+Utils.getInstance().getCurrentHostPort()+"/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"VideoLibrary.GetEpisodes\",\"params\":{\"tvshowid\":" + mTVShow.getTVShowLocalID() + ",\"season\":" + mSeason.getSeasonNumber()+ ",\"properties\":[\"title\",\"thumbnail\",\"plot\",\"art\",\"votes\",\"rating\",\"firstaired\",\"runtime\",\"episode\",\"file\",\"writer\",\"director\",\"uniqueid\",\"tvshowid\",\"cast\",\"playcount\"]}}";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        JSONParser.parseEpisodes(response, mTVShow.getMediaID(),mSeason.getSeasonID(), mSeason.getSeasonNumber());
                        Toast.makeText(getContext(), "Episodes updated", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getContext(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getContext(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(jsObjRequest);
    }
}
