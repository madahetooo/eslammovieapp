
package com.eslammovieapp.eslammovieapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eslammovieapp.eslammovieapp.R;
import com.eslammovieapp.eslammovieapp.response.ReviewResponse;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.TrailerViewHolder> {
    Context mContext;
    List<ReviewResponse.ResultsBean> mResponses;

    public ReviewAdapter(Context mContext, List<ReviewResponse.ResultsBean> mResponses) {
        this.mContext = mContext;
        this.mResponses = mResponses;
    }

    @NonNull
    @Override
    public ReviewAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.TrailerViewHolder holder, int position) {
        holder.tvReview.setText(mResponses.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mResponses.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView tvReview;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            tvReview = itemView.findViewById(R.id.tv_review);
        }
    }
}
