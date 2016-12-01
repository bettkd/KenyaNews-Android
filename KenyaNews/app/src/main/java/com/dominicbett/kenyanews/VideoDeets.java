package com.dominicbett.kenyanews;

import android.graphics.BitmapFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dbett on 6/1/16.
 */
public class VideoDeets {

    // Instance Variables
    private ArrayList<ChannelVideo> channelVideos;

    // Class Variables
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;

    // Constructor
    public VideoDeets(String url) {
        this.urlString = url;
        this.channelVideos = new ArrayList<>();
        this.fetchVideos();
        //System.out.println(channelVideos.get(1).getTitle() + "555555");
    }

    public ArrayList<ChannelVideo> getChannelVideos() {
        return this.channelVideos;
    }

    public void setChannelVideo(ChannelVideo channelVideo) {
        this.channelVideos.add(channelVideo);
    }

    // Parse/read XML document
    public void parseXML (XmlPullParser myParser) {
        int event;
        String text = null;
        try {
            event = myParser.getEventType();


            ChannelVideo _channelVideo = new ChannelVideo();

            while (event != XmlPullParser.END_DOCUMENT){


                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("media:title")) {
                            _channelVideo.setTitle(text);
                            //System.out.println(title);
                        } else if (name.equals("media:description")) {
                            _channelVideo.setSummary(text);
                        } else if (name.equals("media:thumbnail")){
                            URL imgURL = new URL(myParser.getAttributeValue(null, "url"));
                            _channelVideo.setThumbnail(BitmapFactory.decodeStream(imgURL.openConnection().getInputStream()));
                        } else if (name.equals("published")){
                            _channelVideo.setPublished(text);
                        } else if (name.equals("yt:videoId")){
                            _channelVideo.setVideoID(text);
                        } else if (name.equals("media:statistics")) {
                            _channelVideo.setViews(Integer.parseInt(myParser.getAttributeValue(null, "views")));
                        } else if (name.equals("entry")) {
                        }
                        break;
                    default:
                        break;
                }

                if (_channelVideo.getViews() != -1) {
                    this.setChannelVideo(_channelVideo);

                    //System.out.println(_channelVideo.getTitle());
                    //System.out.println(_channelVideo.getViews());

                    _channelVideo = new ChannelVideo();
                }

                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch/load XML from the web
    protected void fetchVideos() {
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("GET");
            connect.setDoInput(true);
            connect.connect();

            InputStream stream = connect.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);
            parseXML(myParser);
            stream.close();
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

}
