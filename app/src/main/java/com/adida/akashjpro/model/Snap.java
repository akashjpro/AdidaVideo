package com.adida.akashjpro.model;

import java.util.List;

/**
 * Created by Akashjpro on 11/20/2016.
 */

public class Snap {
    private int mGravity;
    private String mText;
    private List<Video> videos;

    public Snap(int mGravity, String mText, List<Video> videos) {
        this.mGravity = mGravity;
        this.mText = mText;
        this.videos = videos;
    }

    public int getmGravity() {
        return mGravity;
    }

    public void setmGravity(int mGravity) {
        this.mGravity = mGravity;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
