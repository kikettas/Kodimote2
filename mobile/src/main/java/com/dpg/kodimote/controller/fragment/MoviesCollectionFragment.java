package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.activity.FilmActivity;
import com.dpg.kodimote.controller.adapter.MovieAdapter;
import com.dpg.kodimote.models.MediaItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesCollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesCollectionFragment extends Fragment{
    private static final String TAG = MoviesCollectionFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public MoviesCollectionFragment() {
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
    public static MoviesCollectionFragment newInstance(String param1, String param2) {

        return new MoviesCollectionFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_movies_collection, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_movies);
        gridView.setAdapter(new MovieAdapter(getContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),FilmActivity.class);
                intent.putExtra(FilmActivity.MEDIA_TYPE,FilmActivity.FILM_TYPE);
                intent.putExtra(FilmActivity.ID_FILM_SELECTED,id);

                startActivity(intent);
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
}
