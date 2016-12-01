package com.dominicbett.kenyanews;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dbett on 6/2/16.
 */
public class CustomFeedAdapter extends ArrayAdapter {

    private ArrayList<ChannelVideo> channelVideos;
    public CustomFeedAdapter(Context context, ArrayList<ChannelVideo> channelVideos) {
        super(context, R.layout.custom_feed, channelVideos);
        this.channelVideos = channelVideos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater feedInflater = LayoutInflater.from(getContext());
        View customFeedView = feedInflater.inflate(R.layout.custom_feed, parent, false);

        ChannelVideo thisVideo = channelVideos.get(position);
        String currentTitle = thisVideo.getTitle();

        String currentPublished = thisVideo.getPublished();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ");
        try {
            //Date today = new Date();
            Date myDate = inputFormat.parse(currentPublished);
            int days = (int) (myDate.getTime() - new Date().getTime())/(1000*60*60*24);
            switch (days) {
                case 0:
                    currentPublished = "Today";
                    break;
                case 1:
                    currentPublished = "Yesterday";
                    break;
                default:
                    currentPublished = days + " days ago";
                    break;
            }
            //DateFormat mdy = new SimpleDateFormat("MM-dd-yyyy");
            //currentPublished = mdy.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String currentViews = thisVideo.getViews() + "";
        Bitmap currentThumb = thisVideo.getThumbnail();

        TextView txtTitle = (TextView) customFeedView.findViewById(R.id.txtTitle);
        TextView txtPublished = (TextView) customFeedView.findViewById(R.id.txtPublished);
        TextView txtViews = (TextView) customFeedView.findViewById(R.id.txtViews);
        ImageView imgThumb = (ImageView) customFeedView.findViewById(R.id.imgThumbnail);

        txtTitle.setText(currentTitle);
        txtPublished.setText(currentPublished);
        txtViews.setText(currentViews);
        //imgThumb.setImageResource(R.drawable.chunkk);
        imgThumb.setImageBitmap(currentThumb);
        return customFeedView;
    }
}
