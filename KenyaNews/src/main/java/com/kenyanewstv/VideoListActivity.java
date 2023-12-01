package com.kenyanewstv;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class VideoListActivity extends AppCompatActivity {
    private static final String TAG = "VideoListActivity";

    private String tvChannelURL;
    private String tvChannelName;
    private int[] tvColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        Log.d(TAG, "onCreate: started.");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getIncomingIntent();
        initViewHeader(tvChannelName, tvColors);
        initRecyclerViewItems(tvChannelURL);

        // Implement Ad banner
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if (getIntent().hasExtra("name")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            tvChannelName = getIntent().getStringExtra("name");
            tvChannelURL = getIntent().getStringExtra("url");
            tvColors = getIntent().getIntArrayExtra("colors");
        }
    }

    private void initViewHeader(String name, int[] colors) {
        TextView mHeaderRecyclerView = findViewById(R.id.videolist_header);
        mHeaderRecyclerView.setText(name);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        mHeaderRecyclerView.setBackground(gradientDrawable);
    }

    private void initRecyclerViewItems(String url) {
        FeedStreamer feedStreamer = new FeedStreamer(url);
        Log.d(TAG, "initRecyclerViewItems: Connecting to: " + url);
        //vars
        ArrayList<VideoContainer> videoContainers = feedStreamer.getVideoContainer();

        ArrayList<String> videoTitles = new ArrayList<>();
        ArrayList<String> videoThumbnails = new ArrayList<>();
        ArrayList<String> daysElapsed = new ArrayList<>();
        ArrayList<Date> publishedDate = new ArrayList<>();
        ArrayList<Integer> viewsList = new ArrayList<>();
        ArrayList<String> videoIDs = new ArrayList<>();
        ArrayList<String> videoSummaries = new ArrayList<>();
        for (VideoContainer video : videoContainers) {

            Log.d(TAG, "initRecyclerViewItems: Video Title: " + video.getTitle());
            videoTitles.add(video.getTitle());
            videoThumbnails.add(video.getThumbnail());
            daysElapsed.add(HelperUtilities.getTimePeriodForDaysElapsed(video.getAgeDays()));
            publishedDate.add(video.getPublished());
            viewsList.add(video.getViews());
            videoIDs.add(video.getVideoID());
            videoSummaries.add(video.getSummary());
        }
        initRecyclerView(videoTitles, videoThumbnails, daysElapsed, publishedDate, viewsList, videoIDs, videoSummaries);
    }

    private void initRecyclerView(ArrayList<String> videoTitles, ArrayList<String> videoThumbnails, ArrayList<String> daysElapsed, ArrayList<Date> publishedDate, ArrayList<Integer> viewsList, ArrayList<String> videoIDs, ArrayList<String> videoSummaries) {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView.Adapter<RecyclerViewAdapter.VideoItemViewHolder> mAdapter = new RecyclerViewAdapter(this, videoTitles, videoThumbnails, daysElapsed, publishedDate, viewsList, videoIDs, videoSummaries, tvChannelName, tvColors);

        // Add recycler view
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Add translucent layer to background
        int BG_OPACITY_ALPHA = 30;
        mRecyclerView.setBackgroundColor(ColorUtils.setAlphaComponent(tvColors[0], BG_OPACITY_ALPHA));
    }
}
