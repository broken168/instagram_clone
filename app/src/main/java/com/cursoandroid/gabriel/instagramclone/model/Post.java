package com.cursoandroid.gabriel.instagramclone.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Post implements Serializable {

    private Bitmap userImageBitmap;
    private String description, imageUrl, username;
    private Long id;
    private UserProfile userProfile;

    public Post() {
    }

    public Post(String description, String imageUrl, UserProfile userProfile) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.userProfile = userProfile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
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

    public Bitmap getUserImageBitmap() {
        return userImageBitmap;
    }

    public void setUserImageBitmap(Bitmap userImageBitmap) {
        this.userImageBitmap = userImageBitmap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
