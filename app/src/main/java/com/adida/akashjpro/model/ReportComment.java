package com.adida.akashjpro.model;

/**
 * Created by Aka on 3/10/2017.
 */

public class ReportComment {
    String  idVideo;
    String  content;
    String  position;

    public ReportComment() {
    }

    public ReportComment(String idVideo, String content, String position) {
        this.idVideo = idVideo;
        this.content = content;
        this.position = position;
    }

    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
