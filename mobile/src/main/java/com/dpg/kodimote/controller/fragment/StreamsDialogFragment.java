package com.dpg.kodimote.controller.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.dpg.kodimote.models.AudioStream;
import com.dpg.kodimote.models.SubtitleStream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enriquedelpozogomez on 04/02/16.
 */
public class StreamsDialogFragment extends DialogFragment {
    private static final String TAG = StreamsDialogFragment.class.getSimpleName();

    private List<?> mListStreams;
    private ArrayList mNameStreams;
    private int mStreamSelected = -1;
    private Intent mIntent;
    private int mCurrentStreamIndex;
    private String mTitle = null;
    private int mCurrentStreamType = -1;

    public StreamsDialogFragment(List<?> listStreams, int index, int streamType) {
        this.mListStreams = listStreams;
        this.mNameStreams = new ArrayList();
        this.mIntent = new Intent();
        this.mCurrentStreamIndex = index;
        this.mCurrentStreamType = streamType;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mTitle = (mCurrentStreamType == RemoteFragment.DIALOG_AUDIO) ? "audio" : "subtitle";

        for (Object stream : mListStreams) {
            if (mCurrentStreamType == RemoteFragment.DIALOG_AUDIO) {
                String language = ((AudioStream) stream).getLanguage();
                String name = ((AudioStream) stream).getName();
                if (language.equals("")) {
                    language = "unknown";
                }
                if (name.equals("")) {
                    name = "unknown";
                }
                mNameStreams.add(language + " - " + name);
            } else {
                if (mCurrentStreamType == RemoteFragment.DIALOG_SUBTITLE) {
                    String language = ((SubtitleStream) stream).getLanguage();
                    if (language.equals("")) {
                        language = "unknown";
                    }
                    mNameStreams.add(language);
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select " + mTitle)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems((CharSequence[]) mNameStreams.toArray(new CharSequence[mNameStreams.size()]), mCurrentStreamIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIntent.putExtra("index", which);
                    }
                })
                // Set the action buttons
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(mCurrentStreamType, RemoteFragment.DIALOG_CHANGE, mIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(mCurrentStreamType, RemoteFragment.DIALOG_CANCEL, mIntent);
                    }
                });

        return builder.create();
    }
}
