package com.cursoandroid.gabriel.instagramclone.model;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable, Comparable<Comment> {

    private Long id;
    private String userImageUrl;
    private String msg;
    private Date date;
    private String username;

    public Comment() {
    }

    public Comment(Long id, String userImageUrl, String msg, Date date) {
        this.id = id;
        this.userImageUrl = userImageUrl;
        this.msg = msg;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(Comment o) {
        if(this.id < o.getId()) return 1;
        else if (this.id > o.getId()) return -1;
        else return 0;
    }
}
