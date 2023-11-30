package com.kenyanewstv;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FeedStreamer {
    // Instance Variables
    private ArrayList<VideoContainer> videoContainer;
    // Class Variables
    private String urlString;

    // Constructors
    public FeedStreamer(String url) {
        this.urlString = url;
        this.videoContainer = new ArrayList<>();
        this.fetchVideos();
    }

    public ArrayList<VideoContainer> getVideoContainer() {
        return this.videoContainer;
    }

    public void setChannelVideo(VideoContainer videoContainer) {
        this.videoContainer.add(videoContainer);
    }

    // Parse/read XML document
    public void parseXML(XmlPullParser myParser) {
        int event;
        String text = null;
        try {
            event = myParser.getEventType();

            VideoContainer video_container = new VideoContainer();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("media:title")) {
                            video_container.setTitle(text);
                            //System.out.println(title);
                        } else if (name.equals("media:description")) {
                            video_container.setSummary(text);
                        } else if (name.equals("media:thumbnail")) {
                            String imgURL = myParser.getAttributeValue(null, "url");
                            video_container.setThumbnail(imgURL);
                        } else if (name.equals("published")) {

                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.US);
                            Date published = inputFormat.parse(text);
                            video_container.setPublished(published);
                            int age = (int) (new Date().getTime() - published.getTime()) / (1000 * 60 * 60 * 24);
                            video_container.setAgeDays(age);
                        } else if (name.equals("yt:videoId")) {
                            video_container.setVideoID(text);
                        } else if (name.equals("media:statistics")) {
                            video_container.setViews(Integer.parseInt(myParser.getAttributeValue(null, "views")));
                        }
                        break;
                    default:
                        break;
                }
                if (video_container.getViews() != -1) {
                    this.setChannelVideo(video_container);
                    video_container = new VideoContainer();
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetch/load XML from the web
    protected void fetchVideos() {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("GET");
            connect.setDoInput(true);
            connect.connect();

            InputStream stream = connect.getInputStream();
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myParser = xmlFactoryObject.newPullParser();
            myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myParser.setInput(stream, null);
            parseXML(myParser);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}