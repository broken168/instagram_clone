package com.cursoandroid.gabriel.instagramclone.search;

import com.cursoandroid.gabriel.instagramclone.model.Comment;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;

import java.io.Serializable;
import java.util.List;

public class CommentSearch implements Serializable {

    private List<Comment> content;
    private int totalPages;
    private int numberOfElements;
    private int number;

    public CommentSearch() {
    }

    public List<Comment> getContent() {
        return content;
    }

    public void setContent(List<Comment> content) {
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
