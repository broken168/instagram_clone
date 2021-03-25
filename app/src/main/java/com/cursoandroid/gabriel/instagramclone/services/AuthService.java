package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.model.AccountCredentials;
import com.cursoandroid.gabriel.instagramclone.model.ModelToken;
import com.cursoandroid.gabriel.instagramclone.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {

    @GET(value = "/api/greeting")
    Call<String> greeting();

    @POST(value = "auth/register")
    Call<Void> registrarUsuario(@Body Usuario usuario);

    @POST(value = "/auth/signin")
    Call<ModelToken> loginUsuario(@Body AccountCredentials accountCredentials);


}
