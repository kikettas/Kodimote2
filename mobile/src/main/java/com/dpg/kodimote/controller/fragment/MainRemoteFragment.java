package com.dpg.kodimote.controller.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.adapter.RemotePagerAdapter;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.models.Utils;
import com.viewpagerindicator.CirclePageIndicator;


public class MainRemoteFragment extends Fragment {
    private static final String TAG = MainRemoteFragment.class.getSimpleName();

    private ViewPager mPager = null;
    public RemotePagerAdapter sAdapter = null;
    public CirclePageIndicator mCirclePageIndicator;
    public RelativeLayout mMainPagerRelativeLayout;


    public MainRemoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainRemoteFragment newInstance(String param1, String param2) {
        MainRemoteFragment fragment = new MainRemoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        sAdapter = new RemotePagerAdapter(getChildFragmentManager());

        View root = inflater.inflate(R.layout.fragment_main_remote, container, false);
        mMainPagerRelativeLayout = (RelativeLayout)root.findViewById(R.id.main_pager_fragment_layout);
        mPager = (ViewPager) root.findViewById(R.id.remote_pager);
        mPager.setAdapter(sAdapter);

        mCirclePageIndicator = (CirclePageIndicator)root.findViewById(R.id.circleindicator);
        mCirclePageIndicator.setViewPager(mPager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected "+position);
                if(position==0) {
                    (getActivity()).setTitle("Remote");
                }else{
                    (getActivity()).setTitle("Playlist");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged "+state);


            }
        });


        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }


    public void setFanartBackground(String url){
        Log.d(TAG,url);
        ImageRequest fanartRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mMainPagerRelativeLayout.setBackground(new BitmapDrawable(getResources(), Utils.getInstance().centerCropBitmap(bitmap)));
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(fanartRequest);
    }

    public void removeFanartBackground(){
        mMainPagerRelativeLayout.setBackgroundResource(R.color.backgroundFragmentsDark);
    }

}
