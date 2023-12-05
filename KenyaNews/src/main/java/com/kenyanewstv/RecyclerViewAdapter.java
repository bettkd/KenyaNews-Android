package com.kenyanewstv;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.VideoItemViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private final ArrayList<VideoContainer> mVideos;
    private final String mTVChannelName;
    private final String mTVChannelURL;
    private final int[] mTVColors;
    private final Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<VideoContainer> videos, String tvChannelName, String tvChannelURL, int[] tvColors) {
        this.mVideos = videos;
        this.mContext = context;
        this.mTVChannelName = tvChannelName;
        this.mTVChannelURL = tvChannelURL;
        this.mTVColors = tvColors;
    }

    @NonNull
    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_videoitem, parent, false);
        return new VideoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        //Thumbnails
        Glide.with(mContext)
                .asBitmap()
                .load(mVideos.get(i).getThumbnail())
                .into(viewHolder.thumbnail);
        viewHolder.videoTitle.setText(mVideos.get(i).getTitle());
        viewHolder.views.setText(HelperUtilities.pluralizeItem(mVideos.get(i).getViews(), "view"));
        viewHolder.daysElapsed.setText(HelperUtilities.getTimePeriodForDaysElapsed(mVideos.get(i).getAgeDays()));

        //Click events
        viewHolder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on: " + mVideos.get(i).getTitle());

            Toast.makeText(mContext, mVideos.get(i).getTitle(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra("tvChannelName", mTVChannelName);
            intent.putExtra("tvChannelURL", mTVChannelURL);
            intent.putExtra("tvColors", mTVColors);
            intent.putExtra("currentVideo", mVideos.get(i));
            ArrayList<VideoContainer> relatedVideos = HelperUtilities.sortVideosByViewsDesc(mVideos);
            intent.putExtra("relatedVideos", relatedVideos);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public static class VideoItemViewHolder extends RecyclerView.ViewHolder {
        CircleImageView thumbnail;
        TextView videoTitle;
        TextView daysElapsed;
        TextView views;
        RelativeLayout parentLayout;

        public VideoItemViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            videoTitle = itemView.findViewById(R.id.video_title);
            daysElapsed = itemView.findViewById(R.id.days_elapsed);
            views = itemView.findViewById(R.id.view_count);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
