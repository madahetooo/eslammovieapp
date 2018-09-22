package com.eslammovieapp.eslammovieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eslammovieapp.eslammovieapp.R;
import com.eslammovieapp.eslammovieapp.response.TrailerResponse;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    Context mContext;
    List<TrailerResponse.ResultsBean> mResponses;

    public TrailerAdapter(Context mContext, List<TrailerResponse.ResultsBean> mResponses) {
        this.mContext = mContext;
        this.mResponses = mResponses;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrailerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        holder.tvTrailerTitle.setText(mResponses.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mResponses.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrailerTitle;
        ImageView ivTrailerImage;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            tvTrailerTitle = itemView.findViewById(R.id.trailer_title);
            ivTrailerImage = itemView.findViewById(R.id.trailer_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String videoKey = mResponses.get(position).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoKey));
                        intent.putExtra("VIDEO_ID", videoKey);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
