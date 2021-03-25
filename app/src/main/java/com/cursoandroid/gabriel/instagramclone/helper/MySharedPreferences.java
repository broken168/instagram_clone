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
    private static final String CHAVE_ID = "id";


    public MySharedPreferences(Context c) {
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public static void removeToken(Context c){
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.remove(CHAVE_TOKEN);
        editor.apply();
    }

    public static void saveToken(Context c, String token){
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putString(CHAVE_TOKEN, token);
        editor.apply();
    }

    public static void saveCurrentUserID(Context c, Long id){
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.putLong(CHAVE_ID, id);
        editor.apply();
    }

    public static Long getCurrentUserID(Context c){
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);

        return sharedPreferences.getLong(CHAVE_ID, 0);
    }

    public static String getToken(Context c){
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CHAVE_TOKEN, "");
    }
    public static void removeCurrentUserID(Context c){
        sharedPreferences = c.getSharedPreferences(ARQUIVO_PREFERENCIA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        editor.remove(CHAVE_ID);
        editor.apply();
    }






}
