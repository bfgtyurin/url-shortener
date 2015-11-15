package com.vtyurin.app.model;

public class Link {

    private long id;
    private String fullUrl;
    private String shortUrl;
    private int clicks;

    public Link(String fullUrl, String shortUrl, int clicks) {
        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        return "Link{" +
                "fullUrl='" + fullUrl + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", clicks=" + clicks +
                '}';
    }
}
