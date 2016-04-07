package com.dpg.kodimote.models;

import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by enriquedelpozogomez on 26/01/16.
 */
public class Utils {
    public static final int LIST_TYPE_PERSON = 0;
    public static final int LIST_TYPE_STRINGS = 1;
    private static Utils sUtils = null;
    private String mCurrentHostName = "";
    private String mCurrentHostIP = "";
    private String mCurrentHostPort = "";

    public static Utils getInstance() {
        if (sUtils == null) {
            sUtils = new Utils();
        }
        return sUtils;
    }

    public String transformImageUrl(String ip, String port, String urlDecoded) {
        String url = null;

        try {
            url = "http://" + ip +":"+port+ "/image/" + URLEncoder.encode(urlDecoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return url;
    }

    public String encodeUrl(String url) {
        String finalUrl = null;

        try {
            finalUrl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return finalUrl;
    }

    public Bitmap centerCropBitmap(Bitmap srcBmp) {
        Bitmap dstBmp;

        if (srcBmp.getWidth() >= srcBmp.getHeight()) {
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                    0,
                    srcBmp.getHeight(),
                    srcBmp.getHeight()
            );

        } else {
            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                    srcBmp.getWidth(),
                    srcBmp.getWidth()
            );
        }

        return dstBmp;
    }

    public String listToStringSeparatedByCommas(List<?> list, int listType) {
        String finalStringSeparatedByCommas = "";

        if (listType == LIST_TYPE_STRINGS) {
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    finalStringSeparatedByCommas = finalStringSeparatedByCommas + list.get(i);
                } else {
                    finalStringSeparatedByCommas = finalStringSeparatedByCommas + list.get(i) + ", ";
                }
            }
        } else {
            if (listType == LIST_TYPE_PERSON) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        finalStringSeparatedByCommas = finalStringSeparatedByCommas + ((Person) list.get(i)).getName();
                    } else {
                        finalStringSeparatedByCommas = finalStringSeparatedByCommas + ((Person) list.get(i)).getName() + ", ";
                    }
                }
            }
        }
        return finalStringSeparatedByCommas;
    }

    public void setHostProperties(String name, String ip, String port) {
        mCurrentHostName = name;
        mCurrentHostIP = ip;
        mCurrentHostPort = port;
    }

    public String getCurrentHostName() {
        return mCurrentHostName;
    }

    public String getCurrentHostIP() {
        return mCurrentHostIP;
    }

    public String getCurrentHostPort() {
        return mCurrentHostPort;
    }

    public String convertMillisecondToTimeUnit(String timeUnitFormat, int millis) {
        return String.format(timeUnitFormat, TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    public String millisecondsToTimeString(int millis) {
        // {"hours":0,"milliseconds":150,"minutes":20,"seconds":37}
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1);
        long milliseconds = millis - (hours * 3600000 + minutes * 60000 + seconds * 1000);


        String timeString = "{\"hours\":" + hours + ",";
        timeString = timeString + "\"minutes\":" + minutes + ",";
        timeString = timeString + "\"seconds\":" + seconds + ",";
        timeString = timeString + "\"milliseconds\":" + milliseconds + "}";
        return timeString;

    }
}
