package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.activity.MoviesDirectorOrGenreActivity;
import com.dpg.kodimote.controller.adapter.DirectorAdapter;
import com.dpg.kodimote.models.Directors;

public class DirectorsFragment extends Fragment {
    private static final String TAG = DirectorsFragment.class.getSimpleName();
    public static final String DIRECTOR_NAME = "director_name";
    public static final String SOURCE_TYPE = "source_type";
    public static final int SOURCE_DIRECTOR = 1;


    private OnFragmentInteractionListener mListener;

    public DirectorsFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_directors, container, false);

        ListView listDirectorsView = (ListView)rootView.findViewById(R.id.directors_list);

        listDirectorsView.setAdapter(new DirectorAdapter(getContext()));

        listDirectorsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),MoviesDirectorOrGenreActivity.class);
                intent.putExtra(SOURCE_TYPE, SOURCE_DIRECTOR);

                intent.putExtra(DIRECTOR_NAME,Directors.getInstance().getDirector(position).getName());
                startActivity(intent);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event


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
        // TODO: Change parameter to Director
        void onDirectorSelected(int i);
    }
}
