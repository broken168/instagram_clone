package com.cursoandroid.gabriel.instagramclone.model;

import java.io.Serializable;
import java.util.Objects;

public class Post implements Serializable, Comparable<Post>{

    private String description, imageUrl, username, userImageUrl;
    private Long id;
    private UserProfile userProfile;
    private Boolean liked;
    private Long likes;

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

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }


    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Override
    public int compareTo(Post otherPost) {
        if(this.id < otherPost.getId()) return 1;
        else if (this.id > otherPost.getId()) return -1;
        else return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

