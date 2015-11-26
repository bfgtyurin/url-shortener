package com.vtyurin.app.model;

public class Link {

    private Long id = null;
    private String fullUrl;
    private String shortUrl;
    private String title;
    private long clicks;

    public Link() {
    }

    public Link(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public Link(String fullUrl, String shortUrl, long clicks) {

        if (clicks < 0) {
            throw new IllegalArgumentException("illegal number of clicks: [" + clicks + "]");
        }

        this.fullUrl = fullUrl;
        this.shortUrl = shortUrl;
        this.clicks = clicks;
    }

    public Long getId() {
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    @Override
    public String toString() {
        return "Link {id: " + id +
                ", fullUrl:'" + fullUrl + '\'' +
                ", shortUrl:'" + shortUrl + '\'' +
                ", title:'" + title + '\'' +
                ", clicks:" + clicks +
                "\'}";
    }
}
