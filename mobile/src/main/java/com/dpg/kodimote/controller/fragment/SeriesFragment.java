package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.SeriesPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SeriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SeriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeriesFragment extends Fragment {
    private static final String TAG = SeriesFragment.class.getSimpleName();

    private ViewPager mPager = null;
    public SeriesPagerAdapter sAdapter = null;

    private OnFragmentInteractionListener mListener;

    public SeriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeriesFragment newInstance(String param1, String param2) {
        SeriesFragment fragment = new SeriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        (getActivity()).setTitle("TV Shows");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        sAdapter = new SeriesPagerAdapter(getChildFragmentManager());

        View rootView = inflater.inflate(R.layout.fragment_series, container, false);
        mPager = (ViewPager) rootView.findViewById(R.id.seriesPager);
        mPager.setAdapter(sAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected "+position);
                if(position==0) {
                    (getActivity()).setTitle("TV Shows");
                }else{
                    (getActivity()).setTitle("Networks");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onSerieSelected(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
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
        void onSerieSelected(int position);
    }
}
