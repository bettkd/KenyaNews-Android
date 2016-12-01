package com.dominicbett.kenyanews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsFeedActivity extends AppCompatActivity {


    public static final String ID_EXTRA = "com.dominicbett.kenyanews.VideoView._ID";

    public static ArrayList<ChannelVideo> channelVideos;

    // Class Variables
    private String urlRoot = "https://www.youtube.com/feeds/videos.xml?channel_id=";
    private String channelID = "UCKVsdeoHExltrWMuK0hOWmg"; // KTN TV
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        System.out.println("Started Successfully");


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String url = urlRoot + channelID;

        VideoDeets videoDeets = new VideoDeets(url);
        channelVideos = videoDeets.getChannelVideos();

        int max_feed = channelVideos.size();
        String[] _title = new String[max_feed];
        Bitmap[] _thumbURL = new Bitmap[max_feed];
        String[] _videoID = new String[max_feed];
        String[] _published = new String[max_feed];

        int i = 0;
        while(i < max_feed) {
            _title[i] = channelVideos.get(i).getTitle();
            _published[i] =  channelVideos.get(i).getPublished();
           _thumbURL[i] =  channelVideos.get(i).getThumbnail();
            _videoID[i] =  channelVideos.get(i).getVideoID();

            System.out.println(_title[i].isEmpty());
            System.out.println(channelVideos.size() + "  " + i + " --> " + _published[i]);
            i++;
        }
        String[] foods = {"Bacon", "Eggs", "Peanuts", "Toast"};
        //ListAdapter titles = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, _videoID);
        ListAdapter customFeedAdapter = new CustomFeedAdapter(this, channelVideos);
        ListView lstVideos = (ListView) findViewById(R.id.lstVideos);
        lstVideos.setAdapter(customFeedAdapter);
        lstVideos.setOnItemClickListener(onListClick);

    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(NewsFeedActivity.this, VideoView.class);
            intent.putExtra(ID_EXTRA, String.valueOf(id));
            startActivity(intent);
        }
    };
}
