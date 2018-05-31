package com.example.android.movieapitest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.movieapitest.R;
import com.example.android.movieapitest.object.MovieObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by ka171 on 3/27/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<MovieObject> mMovieData;
    private OnItemClickListener mListener;

    //Constructor will receive the context and the object that will implement the interface
    public MoviesAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mMovieData = new ArrayList<MovieObject>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    // binds the data to the ImageView in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieObject m = mMovieData.get(position);
        String poster = m.getPosterPath();
        Picasso.with(mContext).load(poster).fit().into(holder.myImageView);
    }

    // convenience method for getting data at click position
    public MovieObject getItem(int id) {
        return mMovieData.get(id);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return (mMovieData == null) ? 0 : mMovieData.size();
    }

    public void setMovieData(ArrayList movieData) {
        if (mMovieData.size() != 0) {
            mMovieData.clear();
        }
        for (int i = 0; i < movieData.size(); i++) {
            mMovieData.add((MovieObject) movieData.get(i));
        }
        notifyDataSetChanged();
    }

    public ArrayList<MovieObject> getmMovieDataArray() {
        return mMovieData;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieObject movieObject);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myImageView;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.info_image);

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
