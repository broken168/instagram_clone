package com.cursoandroid.gabriel.instagramclone.search;

import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;

import java.io.Serializable;
import java.util.List;

public class PostSearch implements Serializable {

    private List<Post> content;
    private int totalPages;
    private int numberOfElements;
    private int number;
    private int totalElements;
    private boolean last;

    public PostSearch() {
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<Post> getContent() {
        return content;
    }

    public void setContent(List<Post> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
