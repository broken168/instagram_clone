package com.cursoandroid.gabriel.instagramclone.model;

import java.util.List;

public class UserProfile {

    private Long id;
    private String email;
    private String username;
    private String imageUrl;
    private List<Long> followers;
    private List<Long> following;
    private Long postsNumber;

    public UserProfile() {

    }

    public UserProfile(Long id, String email, String username, String imageUrl, List<Long> followers, List<Long> following, Long postsNumber) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
        this.followers = followers;
        this.following = following;
        this.postsNumber = postsNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Long> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Long> followers) {
        this.followers = followers;
    }

    public List<Long> getFollowing() {
        return following;
    }

    public void setFollowing(List<Long> following) {
        this.following = following;
    }

    public Long getPostsNumber() {
        return postsNumber;
    }

    public void setPostsNumber(Long postsNumber) {
        this.postsNumber = postsNumber;
    }
}
