package com.dpg.kodimote.controller.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.dpg.kodimote.R;
import com.dpg.kodimote.controller.network.RequestQueueSingleton;
import com.dpg.kodimote.controller.network.TcpClient;
import com.dpg.kodimote.controller.parser.JSONParser;
import com.dpg.kodimote.models.AudioStream;
import com.dpg.kodimote.models.SubtitleStream;
import com.dpg.kodimote.models.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RemoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RemoteFragment extends Fragment {
    private static final String TAG = RemoteFragment.class.getSimpleName();
    public static final int DIALOG_AUDIO = 0;
    public static final int DIALOG_SUBTITLE = 1;
    public static final int DIALOG_CANCEL = 0;
    public static final int DIALOG_CHANGE = 1;
    public static final int NOTIFICATION_ONPLAY = 0;
    public static final int NOTIFICATION_ONSTOP = 1;
    public static final int NOTIFICATION_ONPAUSE = 2;
    public static final int NOTIFICATION_ONSEEK = 3;
    public static final int MAX_PROGRESS_SEEKBAR = 10000000;//10^7
    public static final int MULTIPLIER_PROGRESS_SEEKBAR = 100000;//10^5


    private int mPlayerID = -1;
    private NetworkImageView mPosterNetworkImageView;
    private ImageLoader mImageLoader;
    private TextView mMediaTitle;
    private ImageView mPlayPauseButton;
    private ImageView mStopButton;
    private ImageView mRightButton;
    private ImageView mLeftButton;
    private ImageView mUpButton;
    private ImageView mDownButton;
    private ImageView mCenterButton;
    private ImageView mInfoButton;
    private ImageView mContextMenuButton;
    private ImageView mReturnButton;
    private LinearLayout mSmallMediaButtonsLayout;
    private LinearLayout mMainLayout;
    private Button mAudioButton;
    private Button mSubtitlesButton;
    private TextView mCurrentRuntimeTextView;
    private TextView mCurrentFinalRuntimeTextView;
    private TextView mCurrentFinalTimeTextView;
    private SeekBar mRuntimeSeekBar;

    private boolean mPlaying = false;
    private boolean mPause = false;
    private int mCurrentRuntimeInMillis;
    private int mCurrentFinalRuntimeInMillis;
    private int mCurrentFinalTimeInMillis;
    private int mCurrentSeekPercentage;
    private List<AudioStream> mAudioStreams;
    private List<SubtitleStream> mSubtitleStreams;
    private TcpClient mTcpClient;
    private Timer mTimer;
    private Calendar cal;
    private MainRemoteFragment mParentFragment;

    private AudioStream mCurrentAudioStream = null;
    private SubtitleStream mCurrentSubstitleStream = null;


    private OnFragmentInteractionListener mListener;

    public RemoteFragment() {
        // Required empty public constructor
    }

    /**
     * All methods relative to Fragment Lifecycle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        (getActivity()).setTitle("Remote");

        mParentFragment = (MainRemoteFragment) getParentFragment();
        mTimer = new Timer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        final View rootView = inflater.inflate(R.layout.fragment_remote, container, false);
        mMediaTitle = (TextView) rootView.findViewById(R.id.name_playing_film);
        mPosterNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.poster_playing);

        mImageLoader = RequestQueueSingleton.getInstance(getActivity()).getImageLoader();
        mSmallMediaButtonsLayout = (LinearLayout) rootView.findViewById(R.id.layout_media_small_buttons);
        mMainLayout = (LinearLayout) rootView.findViewById(R.id.main_remote_layout);

        mPlayPauseButton = (ImageView) rootView.findViewById(R.id.play_pause_button);
        mStopButton = (ImageView) rootView.findViewById(R.id.stop_button);
        mLeftButton = (ImageView) rootView.findViewById(R.id.left_button);
        mRightButton = (ImageView) rootView.findViewById(R.id.right_button);
        mUpButton = (ImageView) rootView.findViewById(R.id.up_button);
        mDownButton = (ImageView) rootView.findViewById(R.id.down_button);
        mCenterButton = (ImageView) rootView.findViewById(R.id.center_button);
        mInfoButton = (ImageView) rootView.findViewById(R.id.info_button);
        mContextMenuButton = (ImageView) rootView.findViewById(R.id.contextmenu_button);
        mReturnButton = (ImageView) rootView.findViewById(R.id.return_button);

        mCurrentRuntimeTextView = (TextView) rootView.findViewById(R.id.current_runtime);
        mCurrentFinalRuntimeTextView = (TextView) rootView.findViewById(R.id.current_final_runtime);
        mCurrentFinalTimeTextView = (TextView) rootView.findViewById(R.id.current_final_time);
        mRuntimeSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar_runtime);
        mRuntimeSeekBar.setMax(MAX_PROGRESS_SEEKBAR);
        mRuntimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //mCurrentRuntimeInMillis = (progress*100/MAX_PROGRESS_SEEKBAR)*mCurrentFinalRuntimeInMillis/100;
                //changeRuntimeCurrentPlaying();

                if (fromUser) {
                    double percentage = (double) progress / (double) MAX_PROGRESS_SEEKBAR;
                    Log.d(TAG, "percentage: " + percentage);
                    mCurrentRuntimeInMillis = (int) (percentage * (double) mCurrentFinalRuntimeInMillis);
                    mCurrentSeekPercentage = progress;
                    changeRuntimeCurrentPlaying();

                } else {
                    changeRuntimeCurrentPlaying();
                }

                Log.d(TAG, "progress bar:" + progress + " max seek bar: " + MAX_PROGRESS_SEEKBAR + " currentMilis: " + mCurrentRuntimeInMillis + " currentFinalMilis: " + mCurrentFinalRuntimeInMillis);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                Log.d(TAG, "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch");

                Log.d(TAG, "STOP TRACKINGC TOUCH: progress bar:" + mCurrentSeekPercentage + " max seek bar: " + MAX_PROGRESS_SEEKBAR + " currentMilis: " + mCurrentRuntimeInMillis + " currentFinalMilis: " + mCurrentFinalRuntimeInMillis);

                seekThroughPlayingItem();
            }
        });


        mAudioButton = (Button) rootView.findViewById(R.id.audios_button);
        mSubtitlesButton = (Button) rootView.findViewById(R.id.subtitles_button);

        mAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaying) {
                    DialogFragment dialogFrag = new StreamsDialogFragment(mAudioStreams, mCurrentAudioStream.getIndex(), DIALOG_AUDIO);
                    dialogFrag.setTargetFragment(RemoteFragment.this, DIALOG_AUDIO);
                    dialogFrag.show(getFragmentManager(), "audioStreams");
                }
            }
        });
        mSubtitlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaying) {
                    if (mSubtitleStreams.size() > 0) {
                        DialogFragment dialogFrag = new StreamsDialogFragment(mSubtitleStreams, mCurrentSubstitleStream.getIndex(), DIALOG_SUBTITLE);
                        dialogFrag.setTargetFragment(RemoteFragment.this, DIALOG_SUBTITLE);
                        dialogFrag.show(getFragmentManager(), "subsStreams");
                    } else {
                        Snackbar.make(rootView.findViewById(R.id.main_remote_layout), "No subtitles available", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Left");
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Right");
            }
        });

        mUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Up");
            }
        });

        mDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Down");
            }
        });

        mCenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Select");
            }
        });

        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Info");
            }
        });

        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.Back");
            }
        });

        mContextMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputWithMethod("Input.ContextMenu");
            }
        });

        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playPauseMedia();
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
            }
        });

        setEmptyViews();
        return rootView;
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        getPlayerInfo();

        ConnectTask task = new ConnectTask(getActivity());
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //getPlayerInfo();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (mTcpClient != null) {
            mTcpClient.stopClient();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mTcpClient != null) {
            mTcpClient.stopClient();
        }
        mTimer.cancel();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DIALOG_AUDIO:
                if (resultCode == DIALOG_CHANGE) {
                    Log.d(TAG, resultCode + " OK AUDIO: " + data.getIntExtra("index", mCurrentAudioStream.getIndex()));
                    if (data.getIntExtra("index", mCurrentAudioStream.getIndex()) != mCurrentAudioStream.getIndex()) {
                        changeStream(DIALOG_AUDIO, data.getIntExtra("index", mCurrentAudioStream.getIndex()));
                    }
                } else if (resultCode == DIALOG_CANCEL) {
                    Log.d(TAG, resultCode + "Cancel dialog ");
                }
                break;
            case DIALOG_SUBTITLE:
                if (resultCode == DIALOG_CHANGE) {
                    Log.d(TAG, resultCode + " OK Subtitle: " + data.getIntExtra("index", mCurrentSubstitleStream.getIndex()));
                    if (data.getIntExtra("index", mCurrentSubstitleStream.getIndex()) != mCurrentSubstitleStream.getIndex()) {
                        changeStream(DIALOG_SUBTITLE, data.getIntExtra("index", mCurrentSubstitleStream.getIndex()));
                    }
                } else if (resultCode == DIALOG_CANCEL) {
                    Log.d(TAG, resultCode + "Cancel dialog ");
                }
                break;
        }
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
        void onRemoteButtonPressed(Uri uri);
    }


    /**
     * All methods relative to DATA MANAGEMENT (download status, process information...)
     */

    public void getPlayerInfo() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + getString(R.string.get_player_state);
        Log.d(TAG, "getPlayerInfo");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            if (response.getJSONArray("result").length() > 0) {
                                mPlayerID = ((JSONObject) response.getJSONArray("result").get(0)).getInt("playerid");
                                if (mPlayerID != -1) {
                                    mPlaying = true;
                                    getMediaStatus();
                                    setCurrentPlayingToView();
                                }
                            } else {
                                setEmptyViews();
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);

    }

    public void getStreams(JSONObject result) {
        mAudioStreams = new LinkedList<>();
        mSubtitleStreams = new LinkedList<>();


        try {
            JSONArray audioStreamArray = result.getJSONArray("audiostreams");
            JSONArray subtitlesStreamArray = result.getJSONArray("subtitles");
            JSONObject audioStreamObject;
            JSONObject subtitleStreamObject;
            AudioStream audioStream;
            SubtitleStream subtitleStream;

            // Get AudioStreams

            for (int i = 0; i < audioStreamArray.length(); i++) {
                audioStreamObject = (JSONObject) audioStreamArray.get(i);
                audioStream = new AudioStream(audioStreamObject.getInt("index"), audioStreamObject.getString("language"), audioStreamObject.getString("codec"), audioStreamObject.getInt("channels"), audioStreamObject.getInt("bitrate"), audioStreamObject.getString("name"));
                mAudioStreams.add(audioStream);
            }

            // Get SubtitleStreams

            for (int i = 0; i < subtitlesStreamArray.length(); i++) {
                subtitleStreamObject = (JSONObject) subtitlesStreamArray.get(i);
                subtitleStream = new SubtitleStream(subtitleStreamObject.getInt("index"), subtitleStreamObject.getString("language"), subtitleStreamObject.getString("name"));
                mSubtitleStreams.add(subtitleStream);
            }


            // Add 'disable subtitles' option if there are subtitles within the list
            if (mSubtitleStreams.size() > 0) {
                subtitleStream = new SubtitleStream(mSubtitleStreams.size(), "Disable subtitles", "Disable subtitles");
                mSubtitleStreams.add(subtitleStream);
            }


        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }


    }

    public void getMediaStatus() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.GetProperties\",\"params\":{\"playerid\":" + mPlayerID + ",\"properties\":[\"speed\",\"totaltime\",\"percentage\",\"audiostreams\",\"subtitles\",\"currentaudiostream\",\"currentsubtitle\",\"time\"]}}";

        Log.d(TAG, url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            if (response.getJSONObject("result").getInt("speed") == 1) {
                                mPause = false;
                                startTimer();
                            } else {
                                mPause = true;
                            }

                            if (mAudioStreams == null || mSubtitleStreams == null) {
                                getStreams(response.getJSONObject("result"));
                            }
                            if (!mAudioStreams.isEmpty()) {
                                mCurrentAudioStream = mAudioStreams.get(response.getJSONObject("result").getJSONObject("currentaudiostream").getInt("index"));
                            } else {
                                mAudioButton.setEnabled(false);
                            }
                            if (!mSubtitleStreams.isEmpty()) {
                                mCurrentSubstitleStream = mSubtitleStreams.get(response.getJSONObject("result").getJSONObject("currentsubtitle").getInt("index"));
                            }

                            getRuntimeStatus(response);

                        } catch (JSONException e) {
                            Log.d(TAG, "Error: " + e.getMessage());
                        }

                        changeButtonStatePlayPause();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    public void getRuntimeStatus(JSONObject response) {

        try {
            JSONObject currentTimeJSON = response.getJSONObject("result").getJSONObject("time");
            Log.d(TAG, response.toString());

            JSONObject currenTotalTimeJSON = response.getJSONObject("result").getJSONObject("totaltime");
            mCurrentRuntimeInMillis = currentTimeJSON.getInt("milliseconds") + currentTimeJSON.getInt("seconds") * 1000 + currentTimeJSON.getInt("minutes") * 60000 + currentTimeJSON.getInt("hours") * 3600000;
            mCurrentFinalRuntimeInMillis = currenTotalTimeJSON.getInt("milliseconds") + currenTotalTimeJSON.getInt("seconds") * 1000 + currenTotalTimeJSON.getInt("minutes") * 60000 + currenTotalTimeJSON.getInt("hours") * 3600000;


            mCurrentSeekPercentage = (int) (response.getJSONObject("result").getDouble("percentage") * (double) MULTIPLIER_PROGRESS_SEEKBAR);

            mRuntimeSeekBar.setProgress(mCurrentSeekPercentage);

            changeRuntimeCurrentPlaying();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onNotificationReceived(String notification) {
        switch (JSONParser.parseServerNotification(notification)) {
            case RemoteFragment.NOTIFICATION_ONPLAY:
                getPlayerInfo();
                break;
            case RemoteFragment.NOTIFICATION_ONPAUSE:
                mPause = true;
                changeButtonStatePlayPause();
                break;
            case RemoteFragment.NOTIFICATION_ONSTOP:
                stopPlaying();
                mPause = false;
                mPlaying = false;
                setEmptyViews();
                break;
            case RemoteFragment.NOTIFICATION_ONSEEK:
                getMediaStatus();

                break;
            default:
                break;
        }
    }

    public void startTimer(){
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            mCurrentRuntimeInMillis = mCurrentRuntimeInMillis + 1000;
                            changeRuntimeCurrentPlaying();
                    }
                });
            }
        }, 1000, 1000);
    }

    /**
     * All methods relative to BUTTONS
     */

    public void playPauseMedia() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.PlayPause\",\"params\":{\"playerid\":" + mPlayerID + "}}";
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            mPause = response.getJSONObject("result").getInt("speed") != 1;
                            if(mPause){
                                mTimer.cancel();
                            }

                        } catch (JSONException e) {
                            Log.d(TAG, "Error: " + e.getMessage());
                        }

                        changeButtonStatePlayPause();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest2);
    }

    public void stopPlaying() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.Stop\",\"params\":{\"playerid\":" + mPlayerID + "}}";
        Log.d(TAG, url);
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        mPause = false;
                        mPlaying = false;
                        setEmptyViews();
                        mTimer.cancel();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest2);

    }

    public void inputWithMethod(final String methodName) {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"" + methodName + "\"}";
        Log.d(TAG, url);
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString() + methodName);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest2);
    }

    public void changeStream(final int streamType, final int streamIndex) {
        String url = null;


        if (streamType == DIALOG_AUDIO) {
            url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.SetAudioStream\",\"params\":{\"playerid\":" + mPlayerID + ",\"stream\":" + streamIndex + "}}";
        } else {
            if (streamType == DIALOG_SUBTITLE) {
                if (streamIndex == mSubtitleStreams.size() + 1) {
                    url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.SetSubtitle\",\"params\":{\"playerid\":" + mPlayerID + ",\"subtitle\":" + 0 + ",\"enable\":false}}";

                } else {
                    url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.SetSubtitle\",\"params\":{\"playerid\":" + mPlayerID + ",\"subtitle\":" + streamIndex + ",\"enable\":true}}";
                }
            }
        }

        Log.d(TAG, url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        if (streamType == DIALOG_AUDIO) {
                            mCurrentAudioStream = mAudioStreams.get(streamIndex);
                        } else {
                            if (streamType == DIALOG_SUBTITLE) {
                                mCurrentSubstitleStream = mSubtitleStreams.get(streamIndex);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest);
    }

    public void seekThroughPlayingItem() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.Seek\",\"params\":{\"playerid\":" + mPlayerID + ",\"value\":" + Utils.getInstance().millisecondsToTimeString(mCurrentRuntimeInMillis) + "}}";
        Log.d(TAG, "seek: " + url);
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "seek: " + response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest2);

    }

    /**
     * All methods relative to VIEWS
     */

    public void setCurrentPlayingToView() {
        String url = "http://" + Utils.getInstance().getCurrentHostIP() + ":" + Utils.getInstance().getCurrentHostPort() + "/jsonrpc?request={\"jsonrpc\":\"2.0\",\"id\":\"1\",\"method\":\"Player.GetItem\",\"params\":{\"playerid\":" + mPlayerID + ",\"properties\":[\"title\",\"thumbnail\",\"tagline\",\"fanart\",\"art\"]}}";
        JsonObjectRequest jsObjRequest2 = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            makeViewsVisible();

                            // TODO check if the media playing is the same as a possible previous one, in order to avoid another change of views
                            mMediaTitle.setText(response.getJSONObject("result").getJSONObject("item").getString("label"));
                            String urlThumbnail = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), response.getJSONObject("result").getJSONObject("item").getString("thumbnail"));
                            mPosterNetworkImageView.setImageUrl(urlThumbnail, mImageLoader);

                            String urlFanart = Utils.getInstance().transformImageUrl(Utils.getInstance().getCurrentHostIP(),Utils.getInstance().getCurrentHostPort(), response.getJSONObject("result").getJSONObject("item").getString("fanart"));

                            mParentFragment.setFanartBackground(urlFanart);
                            mPlayPauseButton.setEnabled(true);

                            mPlaying = true;

                        } catch (JSONException e) {
                            Log.d(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            Toast.makeText(getActivity(), "errorMessage:" + response.statusCode, Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = error.getClass().getSimpleName();
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(getActivity(), "errorMessage:" + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        // Access the RequestQueue through your singleton class.
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest2);

    }

    public void setEmptyViews() {
        mPosterNetworkImageView.setVisibility(View.GONE);
        mMediaTitle.setText("Nothing playing");
        mSmallMediaButtonsLayout.setVisibility(View.GONE);
        mPlayPauseButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        mParentFragment.removeFanartBackground();
        mAudioStreams = null;
        mSubtitleStreams = null;
    }

    public void makeViewsVisible() {
        mPosterNetworkImageView.setVisibility(View.VISIBLE);
        mSmallMediaButtonsLayout.setVisibility(View.VISIBLE);
        mPlayPauseButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
    }

    public void changeButtonStatePlayPause() {
        if (mPause) {
            mPlayPauseButton.setImageResource(R.drawable.play_button_selector);
            mPause = false;
        } else {
            mPlayPauseButton.setImageResource(R.drawable.pause_button_selector);
            mPause = true;
        }
    }

    public void changeRuntimeCurrentPlaying() {
        mCurrentRuntimeTextView.setText(Utils.getInstance().convertMillisecondToTimeUnit("%02d:%02d:%02d", mCurrentRuntimeInMillis));
        mCurrentFinalRuntimeTextView.setText(Utils.getInstance().convertMillisecondToTimeUnit("%02d:%02d:%02d", mCurrentFinalRuntimeInMillis));
        cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, mCurrentFinalRuntimeInMillis - mCurrentRuntimeInMillis);

        if (cal.get(Calendar.MINUTE) < 10) {
            mCurrentFinalTimeTextView.setText("[" + cal.get(Calendar.HOUR_OF_DAY) + ":0" + cal.get(Calendar.MINUTE) + "]");
        } else {
            mCurrentFinalTimeTextView.setText("[" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "]");
        }
    }


    class ConnectTask extends AsyncTask {
        private Context mContext;

        public ConnectTask(Context context) {
            mContext = context;
        }

        @Override
        protected TcpClient doInBackground(Object[] params) {
            //we create a TCPClient object and
            Log.i(TAG, "doInBackground");
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(final String message) {
                    //this method calls the onProgressUpdate
                    //publishProgress(message);
                    Log.i(TAG, "Input message: " + message);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onNotificationReceived(message);
                        }
                    });

                }
            });

            mTcpClient.run();


            return null;
        }
    }
}
