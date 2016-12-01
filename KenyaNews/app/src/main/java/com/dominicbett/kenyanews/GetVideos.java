package com.dominicbett.kenyanews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dbett on 1/26/16.
 */
public class GetVideos extends AsyncTask<String, Integer, ArrayList<ChannelVideo>> {

    // Instance Variables
    private ArrayList<ChannelVideo> channelVideos;

    // Class Variables
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;

    // Constructor
    public GetVideos(String url) {
        this.urlString = url;
        this.channelVideos = new ArrayList<ChannelVideo>();
    }

    public ArrayList<ChannelVideo> getChannelVideos() {
        return this.channelVideos;
    }

    public void setChannelVideos(ArrayList<ChannelVideo> channelVideos) {
        this.channelVideos = channelVideos;
    }

    // Parse/read XML document
    public void parseXML (XmlPullParser myParser) {
        int event;
        String text = null;
        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT){

                ChannelVideo _channelVideo = new ChannelVideo();

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
                        } else if (name.equals("media:statistics")){
                            _channelVideo.setViews(Integer.parseInt(myParser.getAttributeValue(null, "views")));
                        }
                        break;
                    default:
                        channelVideos.add(_channelVideo);
                        break;
                }

                System.out.println(_channelVideo.getTitle());

                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch/load XML from the web
    public void fetchXML() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                } catch (
                        Exception e
                        )

                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected ArrayList<ChannelVideo> doInBackground(String... urls) {
        ArrayList<ChannelVideo> cv = new ArrayList<ChannelVideo>();
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
            cv = getChannelVideos();
        } catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
        return cv;
    }

    @Override
    protected void onPostExecute(ArrayList<ChannelVideo> channelVideos) {
    }
}
