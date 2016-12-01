package com.dominicbett.kenyanews;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoView extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        TextView txt3 = (TextView) findViewById(R.id.txtTitle);
        final int feedID = Integer.parseInt(getIntent().getStringExtra(NewsFeedActivity.ID_EXTRA));
        txt3.setText(NewsFeedActivity.channelVideos.get(feedID).getTitle());

        //YOUTUBE ACTIONS
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);

        onInitializedListener = new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                String videoID = NewsFeedActivity.channelVideos.get(feedID).getVideoID();
                youTubePlayer.loadVideo(videoID);
                System.out.println(videoID);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //Handle failure
                System.out.println("Failed to load video");
                System.err.println(youTubeInitializationResult);
            }
        };

        youTubePlayerView.initialize("AIzaSyAE9QNLTiysnzY77mtan_TAkiC4h8YMJzs", onInitializedListener);
    }
}
