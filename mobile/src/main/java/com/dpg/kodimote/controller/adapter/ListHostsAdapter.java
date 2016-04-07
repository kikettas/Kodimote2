package com.dpg.kodimote.controller.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.activity.AddingMediaCenterActivity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Project Kodimote created by enriquedelpozogomez on 18/02/16.
 */
public class ListHostsAdapter extends BaseAdapter {

    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private TextView mHostNameTextView;
    private TextView mHostIpTextView;
    private TextView mHostPortTextView;

    public ListHostsAdapter(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key),context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return mSharedPreferences.getStringSet(AddingMediaCenterActivity.SET_HOSTS_KODI,new HashSet<String>()).size();
    }

    @Override
    public Object getItem(int position) {
        List<String> hostList = new LinkedList<>();
        hostList.addAll(mSharedPreferences.getStringSet(AddingMediaCenterActivity.SET_HOSTS_KODI,new HashSet<String>()));

        return hostList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.host_list_item,parent, false);
        }

        mHostNameTextView = (TextView)convertView.findViewById(R.id.host_name);
        mHostIpTextView = (TextView)convertView.findViewById(R.id.host_ip);
        mHostPortTextView = (TextView)convertView.findViewById(R.id.host_port);

        String[] hostProperties = ((String)getItem(position)).split(":");

        mHostNameTextView.setText(hostProperties[0]);
        mHostIpTextView.setText((hostProperties[1]).substring(1));
        mHostPortTextView.setText(hostProperties[2]);


        return convertView;
    }
}
