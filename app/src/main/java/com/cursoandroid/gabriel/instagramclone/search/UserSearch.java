package com.cursoandroid.gabriel.instagramclone.search;

import com.cursoandroid.gabriel.instagramclone.model.UserProfile;

import java.io.Serializable;
import java.util.List;

public class UserSearch implements Serializable {

    private List<UserProfile> content;
    private int totalPages;
    private int numberOfElements;
    private int number;

    public UserSearch() {
    }

    public List<UserProfile> getContent() {
        return content;
    }

    public void setContent(List<UserProfile> content) {
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
}
