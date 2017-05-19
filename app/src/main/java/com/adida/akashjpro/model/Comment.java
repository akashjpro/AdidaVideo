package com.adida.akashjpro.model;

/**
 * Created by Aka on 1/5/2017.
 */

public class Comment {
    private String id;
    private String name;
    private String content;
    private String time;
    private String totalLike;
    private String idReply;
    private boolean isLike = false;

    public Comment() {
    }

    public Comment(String id, String idReply, String name, String content, String time, String totalLike) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.time = time;
        this.totalLike = totalLike;
        this.idReply = idReply;
    }


    public String getIdReply() {
        return idReply;
    }

    public void setIdReply(String idReply) {
        this.idReply = idReply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
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

}
