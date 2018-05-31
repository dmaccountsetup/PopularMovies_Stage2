package com.example.android.movieapitest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapitest.R;
import com.example.android.movieapitest.object.UserReviewObject;

import java.util.ArrayList;

/**
 * Created by ka171 on 5/3/2018.
 */

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder>{
    private ArrayList<UserReviewObject> mReviewList;


    public UserReviewAdapter(ArrayList<UserReviewObject> reviewList){
        mReviewList = reviewList;
    }

    public UserReviewAdapter(){
        mReviewList = new ArrayList<UserReviewObject>();
    }


    @Override
    public UserReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        UserReviewViewHolder userReviewViewHolder = new UserReviewViewHolder(v);
        return userReviewViewHolder;
    }

    @Override
    public void onBindViewHolder(UserReviewViewHolder holder, int position) {
        UserReviewObject currentItem = mReviewList.get(position);
        holder.mAuthorTextView.setText(currentItem.getUser());
        holder.mReviewTextView.setText(currentItem.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public static class UserReviewViewHolder extends RecyclerView.ViewHolder{
        public TextView mAuthorTextView;
        public TextView mReviewTextView;
        public UserReviewViewHolder(View itemView) {
            super(itemView);
            mAuthorTextView = itemView.findViewById(R.id.author);
            mReviewTextView = itemView.findViewById(R.id.text_review);
        }
    }

    public void setUserReviewData(ArrayList<UserReviewObject> result){
        if (mReviewList.size() != 0) {
            mReviewList.clear();
        }
        for (int i = 0; i < result.size(); i++) {
            mReviewList.add((UserReviewObject) result.get(i));
        }
        notifyDataSetChanged();
    }

    public ArrayList<UserReviewObject> getUserReviewData(){
        return mReviewList;
    }
}
