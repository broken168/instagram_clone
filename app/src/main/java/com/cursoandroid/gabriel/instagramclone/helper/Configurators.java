package com.cursoandroid.gabriel.instagramclone.helper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Configurators {

    public static Retrofit retrofitConfigurator(Context context){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", new MySharedPreferences(context).getToken())
                        .build();
                Response response = chain.proceed(newRequest);

                if(!response.isSuccessful())
                    backgroundThreadShortToast(context, response.body());
                return response;
            }
        }).build();

        return new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").client(client).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static void backgroundThreadShortToast(final Context context, final ResponseBody responseBody)
    {
        new Handler(Looper.getMainLooper()).post(
                    () ->
                    {
                        String message = "";
                        String details = "";
                        try {
                            BufferedSource source = responseBody.source();
                            source.request(Long.MAX_VALUE); // Buffer the entire body.
                            Buffer buffer   = source.buffer();
                            Charset charset = responseBody.contentType().charset(StandardCharsets.UTF_8);
                            // Clone the existing buffer is they can only read once so we still want to pass the original one to the chain.
                            String json     = buffer.clone().readString(charset);
                            JsonElement obj = new JsonParser().parse(json);
                            // Capture error code an message.
                            if (obj instanceof JsonObject && ((JsonObject) obj).has("message")) {
                                message = ((JsonObject) obj).get("message").getAsString();
                            }
                            if (obj instanceof JsonObject && ((JsonObject) obj).has("details")) {
                                details = ((JsonObject) obj).get("details").getAsString();
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }finally {
                            Dialog.dialogError(context, message, details);
                        }
                    });
    }
}
