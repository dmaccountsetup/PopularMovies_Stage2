package com.example.android.movieapitest.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ka171 on 5/3/2018.
 */

public class UserReviewObject implements Parcelable {
    private String user;
    private String content;

    public static final Parcelable.Creator<UserReviewObject> CREATOR = new Parcelable.Creator<UserReviewObject>() {
        public UserReviewObject createFromParcel(Parcel in) {
            return new UserReviewObject(in);
        }

        @Override
        public UserReviewObject[] newArray(int i) {
            return new UserReviewObject[i];
        }
    };

    public UserReviewObject() {
        this.user = "";
        this.content = "";
    }

    public UserReviewObject(Parcel in){
        this.user = in.readString();
        this.content = in.readString();
    }
    public void setUser(String u){
        this.user = u;
    }
    public String getUser(){
        return user;
    }

    public void setContent(String c){
        this.content = c;
    }
    public String getContent(){
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(user);
        parcel.writeString(content);

    }
}
