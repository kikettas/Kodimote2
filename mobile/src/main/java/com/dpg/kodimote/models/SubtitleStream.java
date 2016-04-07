package com.dpg.kodimote.models;

/**
 * Created by enriquedelpozogomez on 04/02/16.
 */
public class SubtitleStream {

    private int index = -1;
    private String Language = null;
    private String name = null;

    public SubtitleStream(int index, String language, String name) {
        this.index = index;
        Language = language;
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
