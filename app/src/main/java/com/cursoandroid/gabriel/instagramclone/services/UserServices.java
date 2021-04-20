package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.search.PostSearch;
import com.cursoandroid.gabriel.instagramclone.search.UserSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserServices {

    @GET(value = "/users/{id}")
    Call<UserProfile> getUserById(@Path("id") Long id);

    @PUT(value = "/users/update")
    Call<Void> updateInfosUser(@Body UserProfile user);

    @GET(value = "/users/username/{username}")
    Call<UserSearch> getUserProfileByUsername(@Path("username")String username);

    @GET(value = "/users/current_user")
    Call<UserProfile> getCurrentUser();

    @POST(value = "/users/new_follow/{id}")
    Call<Void> newFollow(@Path("id") Long id);

    @POST(value = "/users/remove_follow/{id}")
    Call<Void> removeFollow(@Path("id") Long id);

}
