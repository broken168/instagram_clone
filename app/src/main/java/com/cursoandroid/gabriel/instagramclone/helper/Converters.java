package com.cursoandroid.gabriel.instagramclone.helper;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class Converters {

    public static MultipartBody.Part converterBitmapToMultipartBody(Bitmap bmImage){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] bytesImage = baos.toByteArray();

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), bytesImage);

        return MultipartBody.Part.createFormData("file",
                java.util.UUID.nameUUIDFromBytes(bytesImage) + ".jpeg",
                requestFile);

    }

    public static String converterErrorBodyToString(Response response){
        try {
            JSONObject jObjError = new JSONObject(response.errorBody().string());
            return jObjError.getString("message");

        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
