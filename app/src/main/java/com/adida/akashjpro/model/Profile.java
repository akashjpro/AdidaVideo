package com.adida.akashjpro.model;

/**
 * Created by Aka on 12/26/2016.
 */

public class Profile {
    private String id;
    private String link;

    public Profile() {
    }

    public Profile(String id, String link) {
        this.id = id;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
