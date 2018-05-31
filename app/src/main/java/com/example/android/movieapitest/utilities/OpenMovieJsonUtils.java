package com.example.android.movieapitest.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.movieapitest.object.MovieObject;
import com.example.android.movieapitest.object.UserReviewObject;
import com.example.android.movieapitest.object.VideoObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ka171 on 3/19/2018.
 * Utility to get data from JSON array
 */

public final class OpenMovieJsonUtils {
    //final String MOVIE_RESULTS = "results";
    public static ArrayList getMovieContentValuesFromJson(Context context, String movieJsonStr) throws JSONException {

        final String MOVIE_RESULTS = "results";
        final String ID = "id";
        final String POSTER_PATH = "poster_path";
        final String TITLE = "title";
        final String SYNOPSIS = "overview";
        final String RATING = "vote_average";
        final String DATE = "release_date";

        JSONObject movieJSONObject = new JSONObject(movieJsonStr);
        JSONArray jsonMovieArray = movieJSONObject.getJSONArray(MOVIE_RESULTS);

        ArrayList<MovieObject> mMovieArrayList = new ArrayList();
        //parsing values from jsonMovieArray
        for (int i = 0; i < jsonMovieArray.length(); i++) {
            //movieInfo contains vote count, id, title, poster, etc.
            JSONObject movieInfo = jsonMovieArray.getJSONObject(i);
            MovieObject movieData = new MovieObject();
            movieData.setId(movieInfo.getString(ID));
            movieData.setTitle(movieInfo.getString(TITLE));
            movieData.setSynopsis(movieInfo.getString(SYNOPSIS));
            movieData.setRating(movieInfo.getString(RATING));
            movieData.setDate(movieInfo.getString(DATE));
            movieData.setPosterPath("https://image.tmdb.org/t/p/w342/" + movieInfo.getString(POSTER_PATH));

            mMovieArrayList.add(movieData);
        }
        return mMovieArrayList;
    }

    public static ArrayList getReviewInfoFromJson(Context context, String jsonStr) throws JSONException {
        final String MOVIE_RESULTS = "results";
        final String CONTENT = "content";
        final String AUTHOR = "author";
        //preparing for Video Links and Review Links
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray(MOVIE_RESULTS);

        ArrayList<UserReviewObject> tempList = new ArrayList();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject currentJSON = jsonArray.getJSONObject(j);
            UserReviewObject uro = new UserReviewObject();
            uro.setUser(currentJSON.getString(AUTHOR));
            uro.setContent(currentJSON.getString(CONTENT));
            tempList.add(uro);
            //tempList.add("https://www." + currentJSON.getString(SITE) + ".com/watch?v=" + currentJSON.getString(VIDEO_KEY));
            Log.v("content", currentJSON.getString(CONTENT));
        }

        return tempList;
    }

    public static ArrayList getVideoInfoFromJson(Context context, String jsonStr) throws JSONException {
        final String MOVIE_RESULTS = "results";
        final String VIDEO_KEY = "key";
        final String SITE = "site";
        final String VIDEO_NAME = "name";
        final String VIDEO_TYPE = "type";
        //preparing for Video Links and Review Links
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray(MOVIE_RESULTS);

        ArrayList<VideoObject> tempList = new ArrayList();
        for (int j = 0; j < jsonArray.length(); j++) {
            JSONObject currentJSON = jsonArray.getJSONObject(j);
            VideoObject uro = new VideoObject();
            uro.setVideoName(currentJSON.getString(VIDEO_NAME));
            uro.setVideoType(currentJSON.getString(VIDEO_TYPE));
            uro.setVideoLink("https://www." + currentJSON.getString(SITE) + ".com/watch?v=" + currentJSON.getString(VIDEO_KEY));
            tempList.add(uro);
            Log.v("Video Link", uro.getVideoLink());
        }

        return tempList;
    }

}