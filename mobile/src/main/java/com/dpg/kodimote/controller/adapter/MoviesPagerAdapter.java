package com.dpg.kodimote.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.dpg.kodimote.controller.fragment.DirectorsFragment;
import com.dpg.kodimote.controller.fragment.GenresFragment;
import com.dpg.kodimote.controller.fragment.MoviesCollectionFragment;

/**
 * Created by enriquedelpozogomez on 22/01/16.
 */
public class MoviesPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = MoviesPagerAdapter.class.getSimpleName();


    public MoviesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override

    public Fragment getItem(int position) {
        Log.d(TAG,"getItem "+position);

        if(position == 0){
            MoviesCollectionFragment moviesCollectionFragment = new MoviesCollectionFragment();
            Log.d(TAG,"Movies");
            return moviesCollectionFragment;
        }else{
            if (position == 1){
                return new DirectorsFragment();
            }else{
                return new GenresFragment();
            }
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "All movies";
        }else{
            if (position == 1){
                return "Directors";
            }else{
                return "Genres";
            }
        }
    }


}
