package com.cursoandroid.gabriel.instagramclone.helper;

import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderPicasso;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configurators {

    public static Retrofit retrofitConfigurator(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", new MySharedPreferences(ImageDownloaderPicasso.context).getToken())
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        return new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").client(client).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
