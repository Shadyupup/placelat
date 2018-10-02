package com.example.acer_pc.placelat;

public class oneReviews {
    private String name;
    private String rating;
    private String time;
    private String text;
    private String photo_url;
    private String url;
    public oneReviews(String name, String rating, String time, String text, String photo_url, String url) {
        this.name = name;
        this.rating = rating;
        this.time = time;
        this.text = text;
        this.photo_url = photo_url;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public String getUrl() {
        return url;
    }
}
