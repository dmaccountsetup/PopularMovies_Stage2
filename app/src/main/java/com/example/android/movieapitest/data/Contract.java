package com.example.android.movieapitest.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ka171 on 5/10/2018.
 */

public class Contract {
    public static final String AUTHORITY = "com.example.android.movieapitest";
    public static final String URL = "content://" + AUTHORITY;
    public static final Uri BASE_CONTENT_URI = Uri.parse(URL);
    public static final String PATH_FAVORITES = "favorites";

    private Contract() {}

    public static final class TableEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favoriteMovieList";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_POSTER = "poster";
    }
}
