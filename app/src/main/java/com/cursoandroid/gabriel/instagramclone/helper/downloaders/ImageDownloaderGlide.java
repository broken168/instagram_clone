package com.cursoandroid.gabriel.instagramclone.helper.downloaders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@GlideModule
public class ImageDownloaderGlide extends AppGlideModule {

    public static final String USER_CREDENTIALS = "gabrigov.instagram_clone@gabriel.govmail.com.br:cRfENlDB=REh";
    public static final String BASIC_AUTH = "Basic " + new String(Base64.encode(USER_CREDENTIALS.getBytes(), Base64.NO_WRAP));

    public static void downloadImage(String url, Context context, ProgressBar progressBar, ImageView img){

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", BASIC_AUTH)
                .build());

        Glide.with(context)
                .load(glideUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(img);
    }

    public static void downloadImage(String url, Context context, ProgressBar progressBar, CircleImageView img){

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", BASIC_AUTH)
                .build());

        Glide.with(context)
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(img);
    }

    public static void downloadImage(String url, Context context, CircleImageView img){

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", BASIC_AUTH)
                .build());

        Glide.with(context)
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(img)
        ;
    }

    public static void downloadImage(String url, Context context,ImageView img){

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", BASIC_AUTH)
                .build());

        Glide.with(context)
                .load(glideUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(img);
    }
}
