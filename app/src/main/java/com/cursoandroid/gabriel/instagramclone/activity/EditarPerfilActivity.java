package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.Converters;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.helper.Permissao;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.services.FileService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.IOException;


import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView imageEditarPerfil;
    private TextView textAlterarFoto;
    private TextInputEditText editNomePerfil, editEmailPerfil;
    private Button buttonSalvar;
    private static final int SELECAO_GALERIA = 200;
    private Bitmap imageRecuperadaGaleria = null;
    private Retrofit retrofit;
    private UserServices userServices;
    private FileService fileService;

    private Uri localImagemSelecionada;
    private UserProfile currentUser;
    private ProgressBar progressBarImagePerfil;
    private AlertDialog dialog;

    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private boolean openByDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        inicializarComponentes();

        Permissao.validarPermissoes(permissoesNecessarias, EditarPerfilActivity.this, 2);

        configRetrofit();
        recuperarDadosUsuario();

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Atualizando dados...")
                .setCancelable(false)
                .build();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            openByDialog = bundle.getBoolean("openByDialog");
        }

        Toolbar toolbar = findViewById(R.id.toolbarAlternativa);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Editar perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);


        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editNomePerfil.getText().toString().isEmpty()) {
                    currentUser.setUsername(editNomePerfil.getText().toString());
                    updateUserImage();
                }else{
                    Toast.makeText(EditarPerfilActivity.this, "Preencha o usuário", Toast.LENGTH_SHORT).show();
                }

            }
        });

        textAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if( i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA);
                }
            }
        });
    }

    private void recuperarDadosUsuario() {
        Call<UserProfile> call = userServices.getCurrentUser();
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    currentUser = response.body();
                    loadUserData();
                }else{
                    progressBarImagePerfil.setVisibility(View.GONE);
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(EditarPerfilActivity.this, json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadUserData() {
        editEmailPerfil.setText(currentUser.getEmail());
        editNomePerfil.setText(currentUser.getUsername());

        String url = currentUser.getImageUrl();
        if(url != null && !url.equals("")) {
            ImageDownloaderGlide.downloadImage(url, this, progressBarImagePerfil, imageEditarPerfil);
        }else {
            progressBarImagePerfil.setVisibility(View.GONE);
        }

    }


    private void configRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", new MySharedPreferences(getApplicationContext()).getToken())
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        retrofit = new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").client(client).addConverterFactory(GsonConverterFactory.create()).build();
        fileService = retrofit.create(FileService.class);
        userServices = retrofit.create(UserServices.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            try{

                if (requestCode == SELECAO_GALERIA) {
                    localImagemSelecionada = data.getData();
                    imageRecuperadaGaleria = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                }

                if(imageRecuperadaGaleria != null){
                    imageEditarPerfil.setImageBitmap(imageRecuperadaGaleria);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void updateUserImage() {
        dialog.show();
        if(imageRecuperadaGaleria != null) {
            Call<Void> call = fileService.uploadFile(Converters.converterBitmapToMultipartBody(imageRecuperadaGaleria), "profile_image");
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        currentUser.setImageUrl(response.headers().get("Location"));
                        updateUserInfos();
                    }else{
                        try {
                            JSONObject json = new JSONObject(response.errorBody().string());
                            Dialog.dialogError(getApplicationContext(), json.getString("message"), json.getString("details"));
                        }catch (Exception e){
                            Toast.makeText(EditarPerfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(EditarPerfilActivity.this, "Falha ao atualizar imagem: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    updateUserInfos();
                }
            });
        }else{
            updateUserInfos();
        }

    }

    private void updateUserInfos(){
        Call<Void> call = userServices.updateInfosUser(currentUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EditarPerfilActivity.this, "Infos atualizadas com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(EditarPerfilActivity.this, json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                close();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                close();

            }
            private void close() {
                if(openByDialog){
                    finish();
                    startActivity(new Intent(EditarPerfilActivity.this, MainActivity.class));
                }else {
                    finish();
                }
                dialog.dismiss();
            }
        });
    }

    public void inicializarComponentes(){
        imageEditarPerfil = findViewById(R.id.imageEditarPerfil);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        editEmailPerfil = findViewById(R.id.editEmailPerfil);
        editNomePerfil = findViewById(R.id.editNomePerfil);
        progressBarImagePerfil = findViewById(R.id.progressBarEditarPerfil);
        buttonSalvar = findViewById(R.id.buttonSalvarAlteracoesPerfil);
        editEmailPerfil.setFocusable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}