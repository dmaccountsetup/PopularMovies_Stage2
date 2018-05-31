package com.example.android.movieapitest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapitest.object.MovieObject;
import com.example.android.movieapitest.utilities.NetworkUtils;
import com.example.android.movieapitest.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ka171 on 4/6/2018.
 */

public class FetchMovieData extends AsyncTask<String, Void, ArrayList<MovieObject>> {
    private static final String TAG = "FetchMovieDataTask";
    private Context mContext;
    private AsyncTaskCompleteListener<ArrayList<MovieObject>> mListener;
    public FetchMovieData(Context context, AsyncTaskCompleteListener<ArrayList<MovieObject>> listener){
        this.mContext = context;
        this.mListener = listener;
    }
    @Override
    protected ArrayList<MovieObject> doInBackground(String... params) {
        //params[0] has menu option
        URL movieDataRequestUrl = NetworkUtils.buildUrl(params[0]);

        ArrayList<MovieObject> simpleJsonMovieData = null;
        try {
            String jsonMovieDataResponse = NetworkUtils.getResponseFromHttpUrl(movieDataRequestUrl);
            simpleJsonMovieData = OpenMovieJsonUtils.getMovieContentValuesFromJson(mContext, jsonMovieDataResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("err", e.getMessage());
        }
        return simpleJsonMovieData;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieObject> movieData) {
        if (movieData != null) {
            // method to show data view. use adapter to show data
            super.onPostExecute(movieData);
            mListener.onTaskComplete(movieData);
        } else {
            // Log error message.
            Log.e("err", "The movieData (ArrayList) is empty.");
        }
    }
}