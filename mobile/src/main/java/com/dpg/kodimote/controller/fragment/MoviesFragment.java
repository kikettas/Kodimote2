package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.MoviesPagerAdapter;


public class MoviesFragment extends Fragment {

    private static final String TAG = MoviesFragment.class.getSimpleName();


    private ViewPager mPager = null;
    public MoviesPagerAdapter sAdapter = null;

    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        (getActivity()).setTitle("Movies");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        sAdapter = new MoviesPagerAdapter(getChildFragmentManager());

        View root = inflater.inflate(R.layout.fragment_movies, container, false);
        mPager = (ViewPager) root.findViewById(R.id.moviesPager);
        mPager.setAdapter(sAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected "+position);
                if(position==0) {
                    (getActivity()).setTitle("Movies");
                }else if(position == 1){
                    (getActivity()).setTitle("Directors");
                }else{
                    (getActivity()).setTitle("Genres");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.d(TAG,"onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d(TAG,"onDetach");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
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
        void onMovieSelected(Uri uri);
    }
}
