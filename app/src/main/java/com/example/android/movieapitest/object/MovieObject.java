package com.example.android.movieapitest.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ka171 on 3/29/2018.
 */

public class MovieObject implements Parcelable {

    public static final Parcelable.Creator<MovieObject> CREATOR = new Parcelable.Creator<MovieObject>() {
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int i) {
            return new MovieObject[i];
        }
    };

    private String posterPath;
    private String title;
    private String id;
    private String synopsis;
    private String rating;
    private String date;

    public MovieObject() {
        id = "";
        posterPath = "";
        title = "";
        synopsis = "";
        rating = "";
        date = "";
    }

    public MovieObject(String id, String posterPath, String title, String synopsis, String rating, String date)
    {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.synopsis = synopsis;
        this.rating = rating;
        this.date = date;
    }

    public MovieObject(Parcel in) {
        this();
        this.id=in.readString();
        this.posterPath = in.readString();
        this.title = in.readString();
        this.synopsis = in.readString();
        this.rating = in.readString();
        this.date = in.readString();
    }

    public String getId(){return id;}

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String s) {
        posterPath = s;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String s) {
        title = s;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String s) {
        synopsis = s;
    }

    public String getRating() {
        return rating;
    }

    public void setId(String i) {id = i;}

    public void setRating(String s) {
        rating = s;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String s) {
        date = s;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(posterPath);
        parcel.writeString(title);
        parcel.writeString(synopsis);
        parcel.writeString(rating);
        parcel.writeString(date);
    }
}
