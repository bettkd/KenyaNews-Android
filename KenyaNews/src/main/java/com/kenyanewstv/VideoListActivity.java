package com.kenyanewstv;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class VideoListActivity extends AppCompatActivity {
    private static final String TAG = "VideoListActivity";

    private RecyclerView mRecyclerView;
    private Context mActivityContext;

    private String tvChannelURL;
    private String tvChannelName;
    private int[] tvColors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        mActivityContext = this;
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
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                int pad_bottom = HelperUtilities.convertDimensionFromDP(mActivityContext, 180);
                mRecyclerView.setPadding(0, 0, 0, pad_bottom);
            }
        });
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if (getIntent().hasExtra("name")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            tvChannelName = getIntent().getStringExtra("name");
            tvChannelURL = getIntent().getStringExtra("url");
            tvColors = getIntent().getIntArrayExtra("colors");
        }
        Log.d(TAG, "name: " + tvChannelName);
        Log.d(TAG, "URL: " + tvChannelURL);
    }

    private void initViewHeader(String name, int[] colors) {
        TextView mHeaderRecyclerView = findViewById(R.id.videolist_header);
        mHeaderRecyclerView.setText(name);

        LinearLayout headerLayout = findViewById(R.id.header_layout);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        headerLayout.setBackground(gradientDrawable);

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on back button - returning to " + tvChannelName);

            Toast.makeText(this, tvChannelName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        });
    }

    private void initRecyclerViewItems(String url) {
        FeedStreamer feedStreamer = new FeedStreamer(url);
        Log.d(TAG, "initRecyclerViewItems: Connecting to: " + url);
        ArrayList<VideoContainer> videos = feedStreamer.getVideoContainer();
        initRecyclerView(videos);
    }

    private void initRecyclerView(ArrayList<VideoContainer> videos) {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView.Adapter<RecyclerViewAdapter.VideoItemViewHolder> mAdapter = new RecyclerViewAdapter(this, videos, tvChannelName, tvChannelURL, tvColors);

        // Add recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Add translucent layer to background
        final int BG_OPACITY_ALPHA = 30;
        mRecyclerView.setBackgroundColor(ColorUtils.setAlphaComponent(tvColors[0], BG_OPACITY_ALPHA));
    }
}
