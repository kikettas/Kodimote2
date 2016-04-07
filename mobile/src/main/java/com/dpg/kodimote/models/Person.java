package com.dpg.kodimote.models;

import java.util.Date;

/**
 * Created by enriquedelpozogomez on 23/01/16.
 */
public class Person {

    private String mName = null;
    private String mSurname = null;
    private Date mBirthDate = null;
    private String mPhotoUrl = null;
    private String mBirthCountry = null;

    public Person(String photo, String name, String surname, Date birthDate, String birthCountry) {
        mPhotoUrl = photo;
        mName = name;
        mSurname = surname;
        mBirthDate = birthDate;
        mBirthCountry = birthCountry;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSurname() {
        return mSurname;
    }

    public void setSurname(String surname) {
        mSurname = surname;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(Date birthDate) {
        mBirthDate = birthDate;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhoto(String photo) {
        mPhotoUrl = photo;
    }

    public String getBirthCountry() {
        return mBirthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        mBirthCountry = birthCountry;
    }
}
