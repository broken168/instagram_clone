package com.cursoandroid.gabriel.instagramclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MySharedPreferences {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String ARQUIVO_PREFERENCIA = "MySharedPreferences";
    private static final String CHAVE_TOKEN = "token";


    public MySharedPreferences(Context c) {
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void removeToken(){
        editor.remove(CHAVE_TOKEN);
        editor.apply();
    }

    public void saveToken(String token){
        editor.putString(CHAVE_TOKEN, token);
        editor.apply();
    }

    public  String getToken(){
        return sharedPreferences.getString(CHAVE_TOKEN, "");
    }







}
