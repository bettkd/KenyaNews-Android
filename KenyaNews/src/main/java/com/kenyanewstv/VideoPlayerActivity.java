package com.kenyanewstv;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";
    private static final int BG_OPACITY_ALPHA = 30;

    // Vars
    private String mTVChannelName;
    private int[] mTVColors;
    private String mVideoID;
    private String mVideoTitle;
    private int mViews;
    private String mPublishedDate;
    private String mVideoSummary;

    /*
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
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        Log.d(TAG, "onCreate: started.");

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        YouTubePlayerListener listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer,
                        getLifecycle(),
                        mVideoID,
                        0f
                );
            }
        };

        // disable web ui
        IFramePlayerOptions options = new IFramePlayerOptions.Builder().controls(0).build();

        youTubePlayerView.initialize(listener, options);

        getIncomingIntent();
        initViewHeader(mTVChannelName, mTVColors);
        initVideoPlayerViewContent();
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
        // Components
        TextView headerRecyclerView = findViewById(R.id.videoplayer_header);
        headerRecyclerView.setText(name);
        headerRecyclerView.setTextColor(Color.WHITE);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        headerRecyclerView.setBackground(gradientDrawable);
    }
    private void initVideoPlayerViewContent() {
        ConstraintLayout parentLayout = findViewById(R.id.videoplayer_parentlayout);
        parentLayout.setBackgroundColor(ColorUtils.setAlphaComponent(mTVColors[0], BG_OPACITY_ALPHA));

        TextView videoTitle = findViewById(R.id.videoplayer_title);
        TextView videoViewCount = findViewById(R.id.videoplayer_viewcount);
        TextView publishedDate = findViewById(R.id.videoplayer_publisheddate);
        TextView videoSummary = findViewById(R.id.videoplayer_summary);

        videoTitle.setText(mVideoTitle);
        videoViewCount.setText(HelperUtilities.pluralizeItem(mViews, "view"));
        publishedDate.setText(mPublishedDate);
        videoSummary.setText(mVideoSummary);

        ImageView shareVideo = findViewById(R.id.share_video);
        shareVideo.setOnClickListener(new View.OnClickListener() {
            final String base_url = "https://www.youtube.com/watch?v=";
            final String subject = "Sharing news from " + mTVChannelName;
            final String message = mVideoTitle +
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
}
