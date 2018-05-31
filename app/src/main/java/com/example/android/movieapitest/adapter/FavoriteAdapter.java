package com.example.android.movieapitest.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieapitest.object.MovieObject;
import com.example.android.movieapitest.R;
import com.example.android.movieapitest.data.Contract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ka171 on 5/9/2018.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private OnItemClickListener mListener;
    private ArrayList<MovieObject> mMovieData;
    private String movieTitle;
    private String date;
    private String rating;
    private String synopsis;
    private String posterPath;
    private String movieID;

    public FavoriteAdapter (Context context){
        mContext = context;
        mMovieData = new ArrayList<MovieObject>();
    }
    public FavoriteAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mMovieData = new ArrayList<MovieObject>(cursor.getCount());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recyclerview_fav_image, parent, false);
        return new FavoriteViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        movieID = mCursor.getString(mCursor.getColumnIndex(Contract.TableEntry.COLUMN_MOVIE_ID));
        movieTitle = mCursor.getString(mCursor.getColumnIndex(Contract.TableEntry.COLUMN_MOVIE_TITLE));
        date = mCursor.getString(mCursor.getColumnIndex(Contract.TableEntry.COLUMN_DATE));
        rating = mCursor.getString(mCursor.getColumnIndex(Contract.TableEntry.COLUMN_RATING));
        synopsis = mCursor.getString(mCursor.getColumnIndex(Contract.TableEntry.COLUMN_SYNOPSIS));
        posterPath = mCursor.getString(mCursor.getColumnIndex(Contract.TableEntry.COLUMN_POSTER));
        mMovieData.add(position, new MovieObject(movieID, posterPath, movieTitle, synopsis, rating, date));
        Picasso.with(mContext).load(posterPath).fit().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public ArrayList<MovieObject> getFavMovieArray(){
        return mMovieData;
    }

    public void setFavMovieData(ArrayList movieData) {
        if (mMovieData.size() != 0) {
            mMovieData.clear();
        }
        for (int i = 0; i < movieData.size(); i++) {
            mMovieData.add((MovieObject) movieData.get(i));
        }
        notifyDataSetChanged();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MovieObject movieObject);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public FavoriteViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.fav_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(mMovieData.get(position));
                        }
                    }
                }
            });
        }
    }
}
