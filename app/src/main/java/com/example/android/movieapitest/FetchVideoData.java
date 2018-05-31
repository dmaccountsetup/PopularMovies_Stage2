package com.example.android.movieapitest;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapitest.object.VideoObject;
import com.example.android.movieapitest.utilities.NetworkUtils;
import com.example.android.movieapitest.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ka171 on 5/4/2018.
 */

public class FetchVideoData extends AsyncTask<String, Void, ArrayList<VideoObject>> {
    private static final String TAG = "FetchVideoDataTask";
    private Context mContext;
    private AsyncTaskCompleteListener<ArrayList<VideoObject>> mListener;
    public FetchVideoData(Context context, AsyncTaskCompleteListener<ArrayList<VideoObject>> listener){
        this.mContext = context;
        this.mListener = listener;
    }
    @Override
    protected ArrayList<VideoObject> doInBackground(String... params) {
        //params[0] has ID value to build user review link
        URL queryUrl = NetworkUtils.buildVidoesUrl(params[0]);
        ArrayList<VideoObject> simpleJsonData = null;
        try {
            String jsonStr = NetworkUtils.getResponseFromHttpUrl(queryUrl);
            simpleJsonData = OpenMovieJsonUtils.getVideoInfoFromJson(mContext, jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("err", e.getMessage());
        }
        return simpleJsonData;
    }

    @Override
    protected void onPostExecute(ArrayList<VideoObject> result) {
        if (result != null) {
            // method to show data view. use adapter to show data
            super.onPostExecute(result);
            mListener.onTaskComplete(result);
        } else {
            Log.e("err", "The result (Video ArrayList) is empty.");
        }
    }
}