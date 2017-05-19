package com.adida.akashjpro.model;

/**
 * Created by Akashjpro on 11/20/2016.
 */

public class Video {
    private String name;
    private String image;
    private String videoID;

    public Video() {
    }

    public Video(String name, String image, String videoID) {
        this.name = name;
        this.image = image;
        this.videoID = videoID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }
}
