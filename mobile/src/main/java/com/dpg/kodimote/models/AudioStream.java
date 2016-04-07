package com.dpg.kodimote.models;

/**
 * Created by enriquedelpozogomez on 04/02/16.
 */
public class AudioStream {

    private String language = null;
    private int index = -1;
    private String codec = null;
    private int channels = -1;
    private int bitrate = -1;
    private String name = null;

    public AudioStream(int index, String language, String codec, int channels, int bitrate, String name) {
        this.index = index;
        this.language = language;
        this.codec = codec;
        this.channels = channels;
        this.bitrate = bitrate;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }
}
