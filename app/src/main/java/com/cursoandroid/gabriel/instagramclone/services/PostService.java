package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostService {

    @POST(value = "/api/createPost")
    Call<Post> createPost(@Body Post post);

    @GET(value = "/api/getAllPosts")
    Call<List<Post>> getAllPosts();
}
