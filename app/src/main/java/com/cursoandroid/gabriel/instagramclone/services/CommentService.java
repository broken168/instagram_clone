package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.model.Comment;
import com.cursoandroid.gabriel.instagramclone.search.CommentSearch;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CommentService {

    @GET(value = "/comment/post/{id}")
    Call<CommentSearch> getCommentsByPost(@Path("id") Long postId);

    @POST(value = "/comment/{id}")
    Call<Void> addComment(@Body Comment comment, @Path("id") Long postId);

}
