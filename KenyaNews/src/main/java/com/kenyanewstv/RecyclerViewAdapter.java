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

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.VideoItemViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private final ArrayList<String> mVideoTitles;
    private final ArrayList<String> mThumbnails;
    private final ArrayList<String> mDaysElapsed;
    private final ArrayList<Date> mPublishedDate;
    private final ArrayList<Integer> mViewsList;
    private final ArrayList<String> mVideoIDs;
    private final ArrayList<String> mVideoSummaries;
    private final String mTVChannelName;
    private final int[] mTVColors;
    private final Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> videoTitles, ArrayList<String> thumbnails, ArrayList<String> daysElapsed, ArrayList<Date> publishedDate, ArrayList<Integer> views, ArrayList<String> videoIDs, ArrayList<String> videoSummaries, String tvChannelName, int[] tvColors) {
        this.mVideoTitles = videoTitles;
        this.mThumbnails = thumbnails;
        this.mDaysElapsed = daysElapsed;
        this.mPublishedDate = publishedDate;
        this.mViewsList = views;
        this.mVideoIDs = videoIDs;
        this.mVideoSummaries = videoSummaries;
        this.mContext = context;
        this.mTVChannelName = tvChannelName;
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
                .load(mThumbnails.get(i))
                .into(viewHolder.thumbnail);
        viewHolder.videoTitle.setText(mVideoTitles.get(i));
        viewHolder.views.setText(HelperUtilities.pluralizeItem(mViewsList.get(i), "view"));
        viewHolder.daysElapsed.setText(mDaysElapsed.get(i));

        //Click events
        viewHolder.parentLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on: " + mVideoTitles.get(i));

            Toast.makeText(mContext, mVideoTitles.get(i), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra("tvChannelName", mTVChannelName);
            intent.putExtra("tvColors", mTVColors);
            intent.putExtra("title", mVideoTitles.get(i));
            intent.putExtra("views", mViewsList.get(i));
            intent.putExtra("daysElapsed", mDaysElapsed.get(i));
            intent.putExtra("publishedDate", mPublishedDate.get(i).getTime());
            intent.putExtra("videoID", mVideoIDs.get(i));
            intent.putExtra("videoSummary", mVideoSummaries.get(i));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mVideoTitles.size();
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
