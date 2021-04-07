package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserServices {

    @PUT(value = "/users/update")
    Call<Void> updateInfosUser(@Body UserProfile user);

    //@Headers({"Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGF2ZXMxMjMuZ3NAZ21haWwuY29tIiwicm9sZXMiOlsiQ09NTU9OX1VTRVIiXSwiaWF0IjoxNjE1NDE2MzE5LCJleHAiOjE2MTU0MTk5MTl9.dlDJFgz0Jf4dupd_58g4LUgAaBhlpKyV4hoXz8hnpnY"})
    @GET(value = "/api/user/id={id}")
    Call<UserProfile> getUserProfileById(@Path("id") Long id);

    @GET(value = "/api/findByUsername/{username}")
    Call<List<UserProfile>> getUserProfileByUsername(@Path("username")String username);

    @GET(value = "/users/current_user")
    Call<UserProfile> getCurrentUser();
}
