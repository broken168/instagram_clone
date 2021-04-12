package com.cursoandroid.gabriel.instagramclone.helper.downloaders;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.cursoandroid.gabriel.instagramclone.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ImageDownloaderPicasso extends Application {

    public static final String USER_CREDENTIALS = "gabrigov.instagram_clone@gabriel.govmail.com.br:cRfENlDB=REh";
    public static final String BASIC_AUTH = "Basic " + new String(Base64.encode(USER_CREDENTIALS.getBytes(), Base64.NO_WRAP));

    public static Context context;
    private static final OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", BASIC_AUTH)
                        .build();
                return chain.proceed(newRequest);
            })
            .build();


    public static void loadImage(String url, ProgressBar progressBar, ImageView imageView){
        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        picasso.load(url)
                .placeholder(R.drawable.perfil)
                .error(R.drawable.perfil)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
