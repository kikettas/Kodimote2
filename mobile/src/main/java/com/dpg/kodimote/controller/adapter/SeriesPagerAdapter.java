package com.dpg.kodimote.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.dpg.kodimote.controller.fragment.SeriesCollectionFragment;

/**
 * Created by enriquedelpozogomez on 22/01/16.
 */
public class SeriesPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = SeriesPagerAdapter.class.getSimpleName();


    public SeriesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override

    public Fragment getItem(int position) {
        Log.d(TAG,"getItem "+position);

        if(position == 0){
            SeriesCollectionFragment moviesCollectionFragment = new SeriesCollectionFragment();
            Log.d(TAG,"Series");
            return moviesCollectionFragment;
        }else{
            SeriesCollectionFragment moviesCollectionFragment = new SeriesCollectionFragment();
            Log.d(TAG,"Series");
            return moviesCollectionFragment;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "All TV-Shows";
        }else{
            return "Networks";
        }
    }


}
