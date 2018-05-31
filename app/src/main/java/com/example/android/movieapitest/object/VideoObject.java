package com.example.android.movieapitest.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ka171 on 5/4/2018.
 */

public class VideoObject implements Parcelable{
    private String videoLink;
    private String videoType;
    private String videoName;

    public VideoObject() {
        this.videoLink = "";
        this.videoType ="";
        this.videoName="";
    }

    protected VideoObject(Parcel in) {
        videoLink = in.readString();
        videoType = in.readString();
        videoName = in.readString();
    }

    public static final Creator<VideoObject> CREATOR = new Creator<VideoObject>() {
        @Override
        public VideoObject createFromParcel(Parcel in) {
            return new VideoObject(in);
        }

        @Override
        public VideoObject[] newArray(int size) {
            return new VideoObject[size];
        }
    };

    public void setVideoLink(String v){
        this.videoLink = v;
    }

    public String getVideoLink(){
        return this.videoLink;
    }

    public void setVideoType(String v){
        this.videoType = v;
    }

    public String getVideoType(){
        return this.videoType;
    }

    public void setVideoName(String v){
        this.videoName = v;
    }

    public String getVideoName(){
        return this.videoName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoLink);
        parcel.writeString(videoType);
        parcel.writeString(videoName);
    }
}
