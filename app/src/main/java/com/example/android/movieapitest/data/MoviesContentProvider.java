package com.example.android.movieapitest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.movieapitest.data.Contract;
/**
 * Created by ka171 on 5/10/2018.
 */

public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private DBHelper mDBHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAVORITES, MOVIES);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_FAVORITES + "/#",MOVIE_WITH_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDBHelper = new DBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] stringArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match){
            case MOVIES:
                returnCursor = db.query(Contract.TableEntry.TABLE_NAME, projection, selection, stringArgs, null,null,sortOrder);
                break;

            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String [] mSelectionArgs = new String[]{id};
                returnCursor = db.query(Contract.TableEntry.TABLE_NAME, projection, mSelection, mSelectionArgs, null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIES:
                long id = db.insert(Contract.TableEntry.TABLE_NAME, null, values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(Contract.TableEntry.CONTENT_URI,id);
                }else {
                    throw new SQLException("Failed to insert row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final  SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int taskDeleted;
        switch (match){
            case  MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                taskDeleted = db.delete(Contract.TableEntry.TABLE_NAME, "movieId=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if (taskDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return taskDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        //doesn't need to implement for current app
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
