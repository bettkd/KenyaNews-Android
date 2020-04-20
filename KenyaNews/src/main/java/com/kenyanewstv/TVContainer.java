package com.kenyanewstv;

import java.util.Arrays;
import java.util.Objects;

public class TVContainer {
    private String name;
    private String url;
    private int[] colors;
    private String thumbnail;

    public TVContainer() {
    }

    public TVContainer(String name, String url, int[] colors, String thumbnail) {
        this.name = name;
        this.url = url;
        this.colors = colors;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "TVContainer{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", colors=" + Arrays.toString(colors) +
                ", thumbnail='" + thumbnail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TVContainer)) return false;
        TVContainer that = (TVContainer) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getUrl(), that.getUrl()) &&
                Arrays.equals(getColors(), that.getColors()) &&
                Objects.equals(getThumbnail(), that.getThumbnail());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getName(), getUrl(), getThumbnail());
        result = 31 * result + Arrays.hashCode(getColors());
        return result;
    }
}
