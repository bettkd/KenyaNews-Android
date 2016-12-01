package com.dominicbett.kenyanews;

import android.graphics.Bitmap;

/**
 * Created by dbett on 1/27/16.
 */
public class ChannelVideo {
    private String title;
    private String summary;
    private Bitmap thumbnail;
    private String published;
    private String videoID;
    private int views;

    public ChannelVideo() {
        this.title = "";
        this.summary = "";
        this.thumbnail = null;
        this.published = "";
        this.videoID = "";
        this.views = -1;
    }

    public ChannelVideo(String title, String summary, Bitmap thumbnail, String published, String videoID, int views) {
        this.title = title;
        this.summary = summary;
        this.thumbnail = thumbnail;
        this.published = published;
        this.videoID = videoID;
        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public String getPublished() {
        return published;
    }

    public String getVideoID() {
        return videoID;
    }

    public int getViews() {
        return views;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
