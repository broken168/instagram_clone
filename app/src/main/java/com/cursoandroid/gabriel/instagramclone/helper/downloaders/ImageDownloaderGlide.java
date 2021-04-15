package com.cursoandroid.gabriel.instagramclone.helper.downloaders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageDownloaderGlide {

    private static final String userCredentials = "gabrigov.instagram_clone@gabriel.govmail.com.br:cRfENlDB=REh";
    private static final String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));

    public static void loadImage(String url, Context context, ProgressBar progressBar, ImageView img){

        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", basicAuth)
                .build());

        Glide.with(context).load(glideUrl).transition(DrawableTransitionOptions.withCrossFade(100)).centerCrop().listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(img);
    }
}
