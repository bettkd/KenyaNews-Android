package com.kenyanewstv;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;

import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = "VideoPlayerActivity";
    private static final int BG_OPACITY_ALPHA = 30;

    // Vars
    private String mTVChannelName;
    private String mTVChannelURL;
    private int[] mTVColors;
    private VideoContainer mCurrentVideo;
    private ArrayList<VideoContainer> mRelatedVideos;

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
                        mCurrentVideo.getVideoID(),
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
        initRelatedVideos();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");

        if (getIntent().hasExtra("tvChannelName")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            mTVChannelName = getIntent().getStringExtra("tvChannelName");
            mTVChannelURL = getIntent().getStringExtra("tvChannelURL");
            mTVColors = getIntent().getIntArrayExtra("tvColors");
            mCurrentVideo = (VideoContainer) getIntent().getSerializableExtra("currentVideo");
            mRelatedVideos = (ArrayList<VideoContainer>) getIntent().getSerializableExtra("relatedVideos");
        }

        Log.d(TAG, "getIncomingIntent: channelURL = " + mTVChannelURL);
    }

    private void initViewHeader(String name, int[] colors) {
        // Components
        TextView headerRecyclerView = findViewById(R.id.videoplayer_header);
        headerRecyclerView.setText(name);

        LinearLayout headerLayout = findViewById(R.id.header_layout);
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                colors);
        headerLayout.setBackground(gradientDrawable);

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "onClick: clicked on back button - returning to " + mTVChannelName);

            Toast.makeText(this, mTVChannelName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, VideoListActivity.class);
            intent.putExtra("name", mTVChannelName);
            intent.putExtra("url", mTVChannelURL);
            intent.putExtra("colors", mTVColors);
            this.startActivity(intent);
        });
    }

    private void initVideoPlayerViewContent() {
        ConstraintLayout parentLayout = findViewById(R.id.videoplayer_parentlayout);
        parentLayout.setBackgroundColor(ColorUtils.setAlphaComponent(mTVColors[0], BG_OPACITY_ALPHA));

        TextView videoTitle = findViewById(R.id.videoplayer_title);
        TextView videoViewCount = findViewById(R.id.videoplayer_viewcount);
        TextView publishedDate = findViewById(R.id.videoplayer_publisheddate);
        TextView videoSummary = findViewById(R.id.videoplayer_summary);

        videoTitle.setText(mCurrentVideo.getTitle());
        videoViewCount.setText(HelperUtilities.pluralizeItem(mCurrentVideo.getViews(), "view"));
        String published = HelperUtilities.getShortDateFromLongDate(mCurrentVideo.getPublished());
        publishedDate.setText(published);
        String summary = mCurrentVideo.getSummary();
        if (summary.trim().isEmpty()) {
            summary = "No description found.";
        }
        videoSummary.setText(summary);

        ImageView shareVideo = findViewById(R.id.share_video);
        shareVideo.setOnClickListener(new View.OnClickListener() {
            final String base_url = "https://www.youtube.com/watch?v=";
            final String subject = "Sharing news from " + mTVChannelName;
            final String message = mCurrentVideo.getTitle() +
                    "\nCheck this out: "
                    + base_url + mCurrentVideo.getVideoID() +
                    "\n\n- #KenyaNewsTV app - https://t.ly/jUFO2";

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

    private void initRelatedVideos() {
        ArrayList<CircleImageView> thumbnails = new ArrayList<>();
        thumbnails.add(findViewById(R.id.thumbnail1));
        thumbnails.add(findViewById(R.id.thumbnail2));
        thumbnails.add(findViewById(R.id.thumbnail3));
        thumbnails.add(findViewById(R.id.thumbnail4));
        thumbnails.add(findViewById(R.id.thumbnail5));

        int i = 0;
        for (CircleImageView thumbnail : thumbnails) {
            thumbnail.setBackgroundColor(ColorUtils.setAlphaComponent(mTVColors[0], BG_OPACITY_ALPHA / 2));
            //Click events
            VideoContainer video = mRelatedVideos.get(i);
            Glide.with(this)
                    .asBitmap()
                    .load(video.getThumbnail())
                    .into(thumbnail);
            thumbnail.setTooltipText(video.getTitle());
            thumbnail.setOnClickListener(v -> {
                Log.d(TAG, "onClick: clicked on: " + video.getTitle());

                Toast.makeText(this, video.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra("tvChannelName", mTVChannelName);
                intent.putExtra("tvChannelURL", mTVChannelURL);
                intent.putExtra("tvColors", mTVColors);
                intent.putExtra("currentVideo", video);
                intent.putExtra("relatedVideos", mRelatedVideos);
                this.startActivity(intent);
            });
            i++;
        }
    }
}
