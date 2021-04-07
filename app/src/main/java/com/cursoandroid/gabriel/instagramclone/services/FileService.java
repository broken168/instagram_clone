package com.cursoandroid.gabriel.instagramclone.services;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileService {

    @Multipart
    @POST(value = "/file/upload_file/{path}")
    Call<Void> uploadFile(@Part MultipartBody.Part file, @Path("path") String path);

}
