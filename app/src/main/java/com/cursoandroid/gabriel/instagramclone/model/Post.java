package com.cursoandroid.gabriel.instagramclone.model;

import java.io.Serializable;

public class Post implements Serializable {

    private String description, imageUrl;
    private Long id, userProfileId;

    public Post() {
    }

    public Post(String description, String imageUrl, Long userProfileId) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.userProfileId = userProfileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
