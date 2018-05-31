package com.example.android.movieapitest.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapitest.AsyncTaskCompleteListener;
import com.example.android.movieapitest.FetchReviewData;
import com.example.android.movieapitest.FetchVideoData;
import com.example.android.movieapitest.object.MovieObject;
import com.example.android.movieapitest.R;
import com.example.android.movieapitest.adapter.UserReviewAdapter;
import com.example.android.movieapitest.object.UserReviewObject;
import com.example.android.movieapitest.adapter.VideoAdapter;
import com.example.android.movieapitest.object.VideoObject;
import com.example.android.movieapitest.data.Contract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/*
*Activity for Movie Detail display
*/
public class DetailActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener {

    public static final String MOVIE_OBJECT = "MovieObject";
    public static final String MY_FAV_PREF = "myfavpref";

    public static final String MOVIE_DATA = "movie_data";
    public static final String BUNDLE_RECYCLER1_LAYOUT = "bundle_recycler1_layout";
    public static final String BUNDLE_RECYCLER2_LAYOUT = "bundle_recycler2_layout";
    public static final String VIDEO_DATA = "video_data";
    public static final String USER_REVIEW_DATA = "user_data";

    //keys
    private static final String KEY_SCROLLVIEW_POSITION = "ScrollViewPosition";

    MovieObject mMovieObject;
    ScrollView mScrollView;
    //RecyclerView for User Trailers
    RecyclerView mRecyclerView1;
    VideoAdapter mAdapter1;
    RecyclerView.LayoutManager mLayoutManager1;
    //RecyclerView for User Reviews
    RecyclerView mRecyclerView2;
    UserReviewAdapter mAdapter2;
    RecyclerView.LayoutManager mLayoutManager2;
    CheckBox mFavoriteCheckBox;
    SharedPreferences sharedPreferences;
    private ImageView mImageView;
    private TextView mSynopsis;
    private String title;
    private String posterPath;
    private String rating;
    private String date;
    private String synopsis;
    //private String shareTrailer;
    private boolean checkBoxCond;
    //private ShareActionProvider mShareActionProvider;
    private boolean isExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Movie Detail");
        ActionBar mActionBar = this.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mScrollView = findViewById(R.id.scrollview_detail_activity);
        //fetch video links and user reviews from here
        mRecyclerView1 = findViewById(R.id.trailer_recycler_view);
        mRecyclerView1.setHasFixedSize(true);
        mLayoutManager1 = new LinearLayoutManager(this);
        mAdapter1 = new VideoAdapter();

        mRecyclerView2 = findViewById(R.id.review_recycler_view);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new LinearLayoutManager(this);
        mAdapter2 = new UserReviewAdapter();

        //RecyclerView for Trailers (Video Links)
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        mRecyclerView1.setAdapter(mAdapter1);
        mAdapter1.setOnItemClickListender(DetailActivity.this);

        //RecyclerView for User Reviews
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setAdapter(mAdapter2);

        if (savedInstanceState != null) {
            mMovieObject = savedInstanceState.getParcelable(MOVIE_DATA);
            Parcelable savedRecycler1LayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER1_LAYOUT);
            mRecyclerView1.getLayoutManager().onRestoreInstanceState(savedRecycler1LayoutState);
            Parcelable savedRecycler2LayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER2_LAYOUT);
            mRecyclerView2.getLayoutManager().onRestoreInstanceState(savedRecycler2LayoutState);
            mAdapter1.setVideoData(savedInstanceState.<VideoObject>getParcelableArrayList(VIDEO_DATA));
            mAdapter2.setUserReviewData(savedInstanceState.<UserReviewObject>getParcelableArrayList(USER_REVIEW_DATA));

            final int[] position = savedInstanceState.getIntArray(KEY_SCROLLVIEW_POSITION);
            if(position != null)
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(position[0], position[1]);
                    }
                });
            //Toast.makeText(getBaseContext(),"saved instantce not null",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = getIntent();
            mMovieObject = intent.getParcelableExtra(MOVIE_OBJECT);
            loadDetailedData(mMovieObject);
            mScrollView.smoothScrollTo(0,0);
            //Toast.makeText(getBaseContext(),"saved instantce null",Toast.LENGTH_SHORT).show();
        }


        //setting up for Movie Detail
        posterPath = mMovieObject.getPosterPath();
        title = mMovieObject.getTitle();
        synopsis = mMovieObject.getSynopsis();
        rating = mMovieObject.getRating();
        date = mMovieObject.getDate();


        //adding scrollable function to Synopsis TextView
        mSynopsis = (TextView) findViewById(R.id.synopsis);
        mSynopsis.setMovementMethod(new ScrollingMovementMethod());

        //setting up Movie Detail
        mImageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load(posterPath).fit().into(mImageView);

        TextView mMovieTitle = findViewById(R.id.movie_title);
        mMovieTitle.setText(title);

        TextView mSynopsis = findViewById(R.id.synopsis);
        mSynopsis.setText(synopsis);

        TextView mRating = findViewById(R.id.user_rating);
        mRating.setText("Rating: " + rating + "/10");

        TextView mDate = findViewById(R.id.release_date);
        mDate.setText(date);

        //favorite checkbox and its function
        mFavoriteCheckBox = (CheckBox) findViewById(R.id.checkbox_favorite);
        mFavoriteCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxCond = mFavoriteCheckBox.isChecked();
                if (checkBoxCond) {
                    //add to Favorite Movies
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(mMovieObject.getId(), checkBoxCond);
                    editor.commit();
                    addToFavorites(mMovieObject);
                } else {
                    //remove from Favorite Movies
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(mMovieObject.getId(), checkBoxCond);
                    editor.commit();
                    removeFromFavorites(mMovieObject);
                }
            }
        });

        //SharedPreferences for favorite checkbox
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(MY_FAV_PREF, Context.MODE_PRIVATE);
        mFavoriteCheckBox.setChecked(sharedPreferences.getBoolean(mMovieObject.getId(), false));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putString("message", "This is my message to be reloaded");
        outState.putParcelable(MOVIE_DATA, mMovieObject);
        outState.putParcelable(BUNDLE_RECYCLER1_LAYOUT, mRecyclerView1.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(BUNDLE_RECYCLER2_LAYOUT, mRecyclerView1.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(VIDEO_DATA, mAdapter1.getVideoData());
        outState.putParcelableArrayList(USER_REVIEW_DATA, mAdapter2.getUserReviewData());
        outState.putIntArray(KEY_SCROLLVIEW_POSITION, new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
        super.onSaveInstanceState(outState);
    }

    private void removeFromFavorites(MovieObject mMovieObject) {
        Uri uri = Contract.TableEntry.CONTENT_URI.buildUpon().appendPath(mMovieObject.getId()).build();
        ContentResolver cr = getContentResolver();
        cr.delete(uri, null, null);
        Toast.makeText(getBaseContext(), mMovieObject.getTitle() + " deleted from Favorite Movies.", Toast.LENGTH_LONG).show();
    }

    private void addToFavorites(MovieObject mMovieObject) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.TableEntry.COLUMN_DATE, mMovieObject.getDate());
        cv.put(Contract.TableEntry.COLUMN_MOVIE_ID, mMovieObject.getId());
        cv.put(Contract.TableEntry.COLUMN_MOVIE_TITLE, mMovieObject.getTitle());
        cv.put(Contract.TableEntry.COLUMN_RATING, mMovieObject.getRating());
        cv.put(Contract.TableEntry.COLUMN_SYNOPSIS, mMovieObject.getSynopsis());
        cv.put(Contract.TableEntry.COLUMN_POSTER, mMovieObject.getPosterPath());
        Uri uri = getContentResolver().insert(Contract.TableEntry.CONTENT_URI, cv);
        Toast.makeText(getBaseContext(), mMovieObject.getTitle() + " added to Favorite Movies", Toast.LENGTH_LONG).show();
        //Log.v("Content Uri :", uri.toString());
    }

    //initiating background threads to fetch information for Trailer and User Reviews
    private void loadDetailedData(MovieObject m) {
        new FetchVideoData(this, new FetchVideoDataTaskCompleteListener()).execute(m.getId());
        new FetchReviewData(this, new FetchReviewDataTaskCompleteListener()).execute(m.getId());
    }

    /*
Create option menu for Sort type
 */
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        MenuItem item = menu.findItem(R.id.share_movie_detail);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareTrailer = mAdapter1.getVideoItem(0).getVideoLink();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.parse(shareTrailer));
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
        //return super.onCreateOptionsMenu(menu);
        return true;
    }/*/

    @Override
    public void onItemClicked(int position) {
        VideoObject clickedItem = mAdapter1.getVideoItem(position);
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickedItem.getVideoLink()));

        startActivity(trailerIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchReviewDataTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<UserReviewObject>> {
        @Override
        public void onTaskComplete(ArrayList<UserReviewObject> result) {
            mAdapter2.setUserReviewData(result);
        }
    }

    public class FetchVideoDataTaskCompleteListener implements AsyncTaskCompleteListener<ArrayList<VideoObject>> {
        @Override
        public void onTaskComplete(ArrayList<VideoObject> result) {
            mAdapter1.setVideoData(result);
            //Toast.makeText(getBaseContext(), "Video link data loaded", Toast.LENGTH_SHORT).show();
        }
    }

    ////////////////////////////////////////
}
