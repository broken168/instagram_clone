package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.dto.NewUserDTO;
import com.cursoandroid.gabriel.instagramclone.model.AccountCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST(value = "/users/new_user")
    Call<Void> registrarUsuario(@Body NewUserDTO user);

    @POST(value = "/login")
    Call<Void> loginUsuario(@Body AccountCredentials accountCredentials);

}
