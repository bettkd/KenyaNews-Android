package com.kenyanewstv;

import java.util.Date;
import java.util.Objects;

public class VideoContainer {
    private String title;
    private String summary;
    private String thumbnail;
    private Date published;
    private int ageDays;
    private String videoID;
    private int views;

    public VideoContainer() {
        this.title = "";
        this.summary = "";
        this.thumbnail = "";
        this.published = null;
        this.ageDays = -1;
        this.videoID = "";
        this.views = -1;
    }

    public VideoContainer(String title, String summary, String thumbnail, Date published, int ageDays, String videoID, int views) {
        this.title = title;
        this.summary = summary;
        this.thumbnail = thumbnail;
        this.published = published;
        this.ageDays = ageDays;
        this.videoID = videoID;
        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public int getAgeDays() {
        return ageDays;
    }

    public void setAgeDays(int ageDays) {
        this.ageDays = ageDays;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "VideoContainer{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", published='" + published.toString() + '\'' +
                ", ageDays=" + ageDays +
                ", videoID='" + videoID + '\'' +
                ", views=" + views +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoContainer)) return false;
        VideoContainer that = (VideoContainer) o;
        return getAgeDays() == that.getAgeDays() &&
                getViews() == that.getViews() &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getSummary(), that.getSummary()) &&
                Objects.equals(getThumbnail(), that.getThumbnail()) &&
                Objects.equals(getPublished(), that.getPublished()) &&
                Objects.equals(getVideoID(), that.getVideoID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSummary(), getThumbnail(), getPublished(), getAgeDays(), getVideoID(), getViews());
    }
}
