package com.example.android.movieapitest.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.android.movieapitest.AsyncTaskCompleteListener;
import com.example.android.movieapitest.FetchMovieData;
import com.example.android.movieapitest.R;
import com.example.android.movieapitest.adapter.FavoriteAdapter;
import com.example.android.movieapitest.adapter.MoviesAdapter;
import com.example.android.movieapitest.data.Contract;
import com.example.android.movieapitest.object.MovieObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVORITE = "favorite";
    private static int NUM_OF_GRID_COL = 2;
    private static String sOption;
    //keys
    private static final String KEY_MOVIE_OBJECT = "MovieObject";
    private static final String KEY_FAV_MOVIE_ARRAY = "FavMovieObjectArray";
    private static final String KEY_MOVIE_OBJ_ARRAY = "MovieObjectArray";
    private static final String KEY_OPTION = "Option";

    MoviesAdapter moviesAdapter;
    FavoriteAdapter favoriteAdapter;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        sOption = POPULAR;
        setActionBarTitle(sOption);

        //setting up main recyclerview with gridview layout
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            NUM_OF_GRID_COL = 3;
        }else
        {
            NUM_OF_GRID_COL = 2;
        }
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(NUM_OF_GRID_COL, LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_GRID_COL));
        recyclerView.setLayoutManager(gridLayoutManager);
        moviesAdapter = new MoviesAdapter(this);
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setOnItemClickListener(new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieObject movieObject) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(KEY_MOVIE_OBJECT, movieObject);
                startActivity(intent);
            }
        });
        //initializing favoriteAdapter
        favoriteAdapter = new FavoriteAdapter(getApplicationContext());

        //if savedInstantState has previous data, retrieve them
        if (savedInstanceState != null) {
            sOption = savedInstanceState.getString(KEY_OPTION);
            setActionBarTitle(sOption);
            if (sOption == FAVORITE) {
                favoriteAdapter.setFavMovieData(savedInstanceState.getParcelableArrayList(KEY_FAV_MOVIE_ARRAY));
                setupFavoriteMoviesView();
                //Toast.makeText(getApplicationContext(), "retrieved favorite movies", Toast.LENGTH_LONG).show();
            } else {
                moviesAdapter.setMovieData(savedInstanceState.getParcelableArrayList(KEY_MOVIE_OBJ_ARRAY));
            }
        } else {
            //if there is no saveinstancestate fetch new data
            if (sOption == FAVORITE) {
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(Contract.TableEntry.CONTENT_URI, null, null, null, null);
                favoriteAdapter = new FavoriteAdapter(getApplicationContext(), cursor);

                setupFavoriteMoviesView();
            } else {
                loadMovieData();
            }
        }

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_OPTION, sOption);
        outState.putParcelableArrayList(KEY_MOVIE_OBJ_ARRAY, moviesAdapter.getmMovieDataArray());
        outState.putParcelableArrayList(KEY_FAV_MOVIE_ARRAY, favoriteAdapter.getFavMovieArray());
        super.onSaveInstanceState(outState);
    }

    /*
     *Check if there is Network Connection
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    /*
    Start AsyncTask to fetch Movie Data
     */
    private void loadMovieData() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_image);
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUM_OF_GRID_COL));
        recyclerView.setAdapter(moviesAdapter);
        if (isOnline()) {
            new FetchMovieData(this, new FetchMyDataTaskCompleteListener()).execute(sOption);
        } else {
            Toast.makeText(getApplicationContext(), "No Network Connection.", Toast.LENGTH_LONG).show();
        }
    }

    /*
    Create sOption menu for Sort type
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    /*
    functions for Option Menu
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.option_popular:
                sOption = POPULAR;
                setActionBarTitle(sOption);
                loadMovieData();
                return true;

            case R.id.option_top_rated:
                sOption = TOP_RATED;
                setActionBarTitle(sOption);
                loadMovieData();
                return true;

            case R.id.option_favorite:
                sOption = FAVORITE;
                setActionBarTitle(sOption);
                setupFavoriteMoviesView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*method to set actionbar title*/
    private void setActionBarTitle(String optionSelected) {

        if (optionSelected == POPULAR) setTitle("Popular Movies");
        else if (optionSelected == TOP_RATED) setTitle("Top Rated Movies");
        else setTitle("Favorite Movies");

    }

    /*
    *This sets up Favorite Movies View
    * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupFavoriteMoviesView() {
        RecyclerView favRecyclerView = (RecyclerView) findViewById(R.id.rv_image);
        favRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        favRecyclerView.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieObject movieObject) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(KEY_MOVIE_OBJECT, movieObject);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(Contract.TableEntry.CONTENT_URI, null, null, null, null);
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteAdapter.swapCursor(null);
    }

    /*
Fetch Movie Data using AsyncTask
 */
    public class FetchMyDataTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<MovieObject>> {
        @Override
        public void onTaskComplete(ArrayList<MovieObject> result) {
            moviesAdapter.setMovieData(result);
        }
    }
}
