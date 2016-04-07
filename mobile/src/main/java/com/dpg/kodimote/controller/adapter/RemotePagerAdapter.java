package com.dpg.kodimote.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.dpg.kodimote.controller.fragment.PlaylistFragment;
import com.dpg.kodimote.controller.fragment.RemoteFragment;

/**
 * Created by enriquedelpozogomez on 22/01/16.
 */
public class RemotePagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = RemotePagerAdapter.class.getSimpleName();


    public RemotePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override

    public Fragment getItem(int position) {
        Log.d(TAG,"getItem "+position);

        if(position == 0){
            RemoteFragment remoteFragment = new RemoteFragment();
            return remoteFragment;
        }else{
            return new PlaylistFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) {
            return "Remote";
        }else{
            return "Playlist";
        }
    }




}
