package com.cursoandroid.gabriel.instagramclone.services;

import com.cursoandroid.gabriel.instagramclone.model.PathModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileService {

    @Multipart
    @POST(value = "/api/file/uploadFile/{path}")
    Call<PathModel> uploadFile(@Part MultipartBody.Part image, @Path("path") String path);


}
