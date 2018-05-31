package com.example.android.movieapitest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapitest.R;
import com.example.android.movieapitest.object.VideoObject;

import java.util.ArrayList;

/**
 * Created by ka171 on 5/3/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    private ArrayList<VideoObject> mVideoList;
    private OnItemClickListener mListener;


    public VideoAdapter(ArrayList<VideoObject> reviewList){
        mVideoList = reviewList;
    }

    public void setOnItemClickListender(OnItemClickListener listener){
        this.mListener = listener;
    }

    public VideoAdapter(){
        mVideoList = new ArrayList<VideoObject>();
    }

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }


    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(v);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoObject currentItem = mVideoList.get(position);
        holder.mVideoNameTextView.setText(currentItem.getVideoName());
        holder.mVideoTypeTextView.setText(currentItem.getVideoType());
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public VideoObject getVideoItem(int position){
        return mVideoList.get(position);
    }
    public class VideoViewHolder extends RecyclerView.ViewHolder{
        public ImageView mPlayImage;
        public TextView mVideoNameTextView;
        public TextView mVideoTypeTextView;
        public VideoViewHolder(View itemView) {
            super(itemView);
            mPlayImage = itemView.findViewById(R.id.image_play);
            mVideoNameTextView = itemView.findViewById(R.id.video_name);
            mVideoTypeTextView = itemView.findViewById(R.id.video_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClicked(position);
                        }
                    }
                }
            });
        }
    }

    public void setVideoData(ArrayList<VideoObject> result){
        if (mVideoList.size() != 0) {
            mVideoList.clear();
        }
        for (int i = 0; i < result.size(); i++) {
            mVideoList.add((VideoObject) result.get(i));
        }
        notifyDataSetChanged();
    }

    public ArrayList<VideoObject> getVideoData(){
        return mVideoList;
    }
}
