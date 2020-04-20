package com.kenyanewstv;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.core.graphics.ColorUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class VideolistActivity extends AppCompatActivity {

    private static final String TAG = "VideolistActivity";
    private int BG_OPACITY_ALPHA = 30;

    //vars
    private ArrayList<VideoContainer> videoContainers;
    private String tvChannelURL;
    private String tvChannelName;
    private int[] tvColors;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mHeaderRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        Log.d(TAG, "onCreate: started.");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getIncomingIntent();
        initViewHeader(tvChannelName, tvColors);
        initRecyclerViewItems(tvChannelURL);
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
        mHeaderRecyclerView = findViewById(R.id.videolist_header);
        mHeaderRecyclerView.setText(name);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        mHeaderRecyclerView.setBackground(gradientDrawable);
    }

    private void initRecyclerViewItems(String url) {
        FeedStreamer feedStreamer = new FeedStreamer(url);
        Log.d(TAG, "initRecyclerViewItems: Connecting to: " + url);
        videoContainers = feedStreamer.getVideoContainer();

        ArrayList<String> videoTitles = new ArrayList<>();
        ArrayList<String> videoTumbnails = new ArrayList<>();
        ArrayList<String> daysElapsed = new ArrayList<>();
        ArrayList<Date> publishedDate = new ArrayList<>();
        ArrayList<Integer> viewsList = new ArrayList<>();
        ArrayList<String> videoIDs = new ArrayList<>();
        ArrayList<String> videoSummaries = new ArrayList<>();
        for (VideoContainer video : videoContainers) {

            Log.d(TAG, "initRecyclerViewItems: Video Title: " + video.getTitle());
            videoTitles.add(video.getTitle());
            videoTumbnails.add(video.getThumbnail());
            daysElapsed.add(HelperUtilities.getTimePeriodForDaysElapsed(video.getAgeDays()));
            publishedDate.add(video.getPublished());
            viewsList.add(video.getViews());
            videoIDs.add(video.getVideoID());
            videoSummaries.add(video.getSummary());
        }
        initRecyclerView(videoTitles, videoTumbnails, daysElapsed, publishedDate, viewsList, videoIDs, videoSummaries);
    }

    private void initRecyclerView(ArrayList<String> videoTitles, ArrayList<String> videoTumbnails, ArrayList<String> daysElapsed, ArrayList<Date> publishedDate, ArrayList<Integer> viewsList, ArrayList<String> videoIDs, ArrayList<String> videoSummaries) {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        mAdapter = new RecyclerViewAdapter(this, videoTitles, videoTumbnails, daysElapsed, publishedDate, viewsList, videoIDs, videoSummaries, tvChannelName, tvColors);

        // Add recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Add translucent layer to background
        mRecyclerView.setBackgroundColor(ColorUtils.setAlphaComponent(tvColors[0], BG_OPACITY_ALPHA));
    }
}
