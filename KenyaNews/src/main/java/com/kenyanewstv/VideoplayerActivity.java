package com.kenyanewstv;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

public class VideoplayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final String TAG = "VideoplayerActivity";
    private static final String API_KEY = "AIzaSyBkIYCYIUozVohExwejbRadP4Mq-LA5ovc";
    private static final int BG_OPACITY_ALPHA = 30;

    // Components
    private TextView headerRecyclerView;
    private ConstraintLayout parentLayout;
    private TextView videoTitle;
    private TextView videoViewCount;
    private TextView videoSummary;
    private ImageView shareVideo;
    private TextView publishedDate;

    //private AdView mAdView;

    // Vars
    private String mTVChannelName;
    private int[] mTVColors;
    private String mVideoID;
    private String mVideoTitle;
    private int mViews;
    private String mPublishedDate;
    private String mVideoSummary;

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        Log.d(TAG, "onCreate: started.");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player);
        youTubePlayerView.initialize(API_KEY, this);

        getIncomingIntent();
        initViewHeader(mTVChannelName, mTVColors);
        initVideoPlayerViewContent();

        // Implement Ad banner
        //MobileAds.initialize(this, "ca-app-pub-2441030782933536~4703195751");
        //mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if (getIntent().hasExtra("tvChannelName")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            mTVChannelName = getIntent().getStringExtra("tvChannelName");
            mTVColors = getIntent().getIntArrayExtra("tvColors");
            mVideoTitle = getIntent().getStringExtra("title");
            mViews = getIntent().getIntExtra("views", 0);
            long publishedLongDate = getIntent().getLongExtra("publishedDate", -1);
            mPublishedDate = HelperUtilities.getShortDateFromLongDate(publishedLongDate);
            mVideoID = getIntent().getStringExtra("videoID");
            mVideoSummary = getIntent().getStringExtra("videoSummary");
            if (mVideoSummary.trim().isEmpty()) {
                mVideoSummary = "No description found.";
            }
        }
    }

    private void initViewHeader(String name, int[] colors) {
        headerRecyclerView = findViewById(R.id.videoplayer_header);
        headerRecyclerView.setText(name);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        headerRecyclerView.setBackground(gradientDrawable);
    }

    private void initVideoPlayerViewContent() {
        parentLayout = findViewById(R.id.videoplayer_parentlayout);
        parentLayout.setBackgroundColor(ColorUtils.setAlphaComponent(mTVColors[0], BG_OPACITY_ALPHA));

        videoTitle = findViewById(R.id.videoplayer_title);
        videoViewCount = findViewById(R.id.videoplayer_viewcount);
        publishedDate = findViewById(R.id.videoplayer_publisheddate);
        videoSummary = findViewById(R.id.videoplayer_summary);

        videoTitle.setText(mVideoTitle);
        videoViewCount.setText(HelperUtilities.pluralizeItem(mViews, "view"));
        publishedDate.setText(mPublishedDate);
        videoSummary.setText(mVideoSummary);

        shareVideo = findViewById(R.id.share_video);
        shareVideo.setOnClickListener(new View.OnClickListener() {
            String base_url = "https://www.youtube.com/watch?v=";
            String subject = "Sharing news from " + mTVChannelName;
            String message = mVideoTitle +
                    "\nLink: "
                    + base_url + mVideoID +
                    "\n\n- #KenyaNewsTV android app.";

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Sharing via"));
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        // Add listeners
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        // Start buffering
        if (!wasRestored) {
            Log.d(TAG, "onInitializationSuccess: Starting video ID: " + mVideoID);
            youTubePlayer.loadVideo(mVideoID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failed to initialize video", Toast.LENGTH_SHORT).show();
    }
}
