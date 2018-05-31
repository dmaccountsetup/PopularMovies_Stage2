package com.example.android.movieapitest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapitest.object.UserReviewObject;
import com.example.android.movieapitest.utilities.NetworkUtils;
import com.example.android.movieapitest.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ka171 on 5/4/2018.
 */

public class FetchReviewData extends AsyncTask<String, Void, ArrayList<UserReviewObject>> {
    private static final String TAG = "FetchReviewDataTask";
    private Context mContext;
    private AsyncTaskCompleteListener<ArrayList<UserReviewObject>> mListener;
    public FetchReviewData(Context context, AsyncTaskCompleteListener<ArrayList<UserReviewObject>> listener){
        this.mContext = context;
        this.mListener = listener;
    }
    @Override
    protected ArrayList<UserReviewObject> doInBackground(String... params) {
        //params[0] has ID value to build user review link
        URL queryUrl = NetworkUtils.buildReviewsUrl(params[0]);
        ArrayList<UserReviewObject> simpleJsonData = null;
        try {
            String jsonStr = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            simpleJsonData = OpenMovieJsonUtils.getReviewInfoFromJson(mContext, jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("err", e.getMessage());
        }
        return simpleJsonData;
    }

    @Override
    protected void onPostExecute(ArrayList<UserReviewObject> result) {
        if (result != null) {
            // method to show data view. use adapter to show data
            super.onPostExecute(result);
            mListener.onTaskComplete(result);
        } else {
            Log.e("err", "The result (User Review ArrayList) is empty.");
        }
    }
}
