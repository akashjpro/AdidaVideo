package com.adida.akashjpro.model;

/**
 * Created by Aka on 1/10/2017.
 */

public class CommentReply {
    private String id;
    private String name;
    private String content;
    private String time;
    private String totalLike;
    private String position;
    private boolean isLike = false;

    public CommentReply() {
    }

    public CommentReply(String id, String name, String content, String time, String totalLike, String position) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.time = time;
        this.totalLike = totalLike;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(String totalLike) {
        this.totalLike = totalLike;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
