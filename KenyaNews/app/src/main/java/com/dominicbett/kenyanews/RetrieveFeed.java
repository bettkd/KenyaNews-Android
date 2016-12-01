package com.dominicbett.kenyanews;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dbett on 2/2/16.
 */
public class RetrieveFeed extends AsyncTask {

    private URL url;

    private ArrayList<ChannelVideo> channelVideos = new ArrayList<ChannelVideo>();;

    public void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        // Initializing instance variables

        ChannelVideo channelVideo = new ChannelVideo();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
            xpp.setInput(getInputStream(url), "UTF-8");

            String text = null;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String name = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("media:title")) {
                            channelVideo.setTitle(text);
                            System.out.println(text);
                        } else if (name.equals("media:description")) {
                            channelVideo.setSummary(text);
                        } else if (name.equals("media:thumbnail")){
                            URL imgURL = new URL(xpp.getAttributeValue(null, "url"));
                            channelVideo.setThumbnail(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                        } else if (name.equals("published")){
                            channelVideo.setPublished(text);
                        } else if (name.equals("yt:videoId")){
                            channelVideo.setVideoID(text);
                        } else if (name.equals("media:statistics")){
                            channelVideo.setViews(Integer.parseInt(xpp.getAttributeValue(null, "views")));
                        }
                        break;
                    default:
                        channelVideos.add(channelVideo);
                        break;
                }

                //System.out.println(channelVideo.getTitle());

                eventType = xpp.next(); //move to next element
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return channelVideos;
    }


    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<ChannelVideo> getChannelVideos() {
        return channelVideos;
    }
}