package com.example.movieapp;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String m_title;
    private String m_year;
    private String m_imdbID;
    private String m_type;
    private String m_posterURL;
    private boolean m_featured;
    private String m_trailer;

    public Movie() {
    }

    public boolean isM_featured() {
        return m_featured;
    }

    public void setM_featured(boolean m_featured) {
        this.m_featured = m_featured;
    }

    public Movie(String title, String year, String imdbID, String type, String posterURL, boolean featured, String trailer) {
        this.m_title = title;
        this.m_year = year;
        this.m_imdbID = imdbID;
        this.m_type = type;
        this.m_posterURL = posterURL;
        this.m_featured = featured;
        this.m_trailer = trailer;
    }

    protected Movie(Parcel in) {
        m_title = in.readString();
        m_year = in.readString();
        m_imdbID = in.readString();
        m_type = in.readString();
        m_posterURL = in.readString();
        m_trailer = in.readString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            m_featured = in.readBoolean();
        }

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {


        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_title);
        dest.writeString(m_year);
        dest.writeString(m_imdbID);
        dest.writeString(m_type);
        dest.writeString(m_posterURL);
        dest.writeString(m_trailer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(m_featured);
        }
    }

    public String getM_title() {
        return m_title;
    }

    public void setM_title(String title) {
        this.m_title = title;
    }

    public String getM_year() {
        return m_year;
    }

    public void setM_year(String year) {
        this.m_year = year;
    }

    public String getM_imdbID() {
        return m_imdbID;
    }

    public void setM_imdbID(String imdbID) {
        this.m_imdbID = imdbID;
    }

    public String getM_type() {
        return m_type;
    }

    public void setM_type(String type) {
        this.m_type = type;
    }

    public String getM_posterURL() {
        return m_posterURL;
    }

    public void setM_posterURL(String posterURL) {
        this.m_posterURL = posterURL;
    }

    public String getM_trailer() {
        return m_trailer;
    }

    public void setM_trailer(String trailer) {
        this.m_trailer = trailer;
    }
}
