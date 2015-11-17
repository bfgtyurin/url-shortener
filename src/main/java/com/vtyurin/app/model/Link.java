package com.vtyurin.app.model;

public class Link {

    private long id;
    private String fullURL;
    private String shortURL;
    private long clicks;

    public Link() {
    }

    public Link(String fullURL, String shortURL, int clicks) {
        this.fullURL = fullURL;
        this.shortURL = shortURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullURL() {
        return fullURL;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        return "Link{" +
                "fullURL='" + fullURL + '\'' +
                ", shortURL='" + shortURL + '\'' +
                ", clicks=" + clicks +
                '}';
    }
}
