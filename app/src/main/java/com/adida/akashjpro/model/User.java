package com.adida.akashjpro.model;

/**
 * Created by Aka on 1/3/2017.
 */

public class User {
    private String name;
    private String profilePic;
    private String email;
    private String coverPhoto;
    private String birthday;
    private String location;
    private String gender;
    private String phone;

    public User() {
    }

    public User(String name, String profilePic, String email, String coverPhoto, String birthday, String location, String gender, String phone) {
        this.name = name;
        this.profilePic = profilePic;
        this.email = email;
        this.coverPhoto = coverPhoto;
        this.birthday = birthday;
        this.location = location;
        this.gender = gender;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
