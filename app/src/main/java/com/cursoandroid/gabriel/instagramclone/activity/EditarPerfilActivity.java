package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.Converters;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.helper.Permissao;
import com.cursoandroid.gabriel.instagramclone.model.PathModel;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.services.FileService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.textfield.TextInputEditText;
import com.nostra13.universalimageloader.core.assist.FlushedInputStream;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

        Call<UserProfile> call = userServices.getUserProfileById(MySharedPreferences.getCurrentUserID(getApplicationContext()));

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null){
                    currentUser = response.body();
                    loadUserData();

                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EditarPerfilActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "erro ao recuperar user: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserData() {
        editEmailPerfil.setText(currentUser.getEmail());
        editNomePerfil.setText(currentUser.getUsername());


        String url = currentUser.getProfileImage_path_name();
        if(url != null) {
            DownloadImage downloadImage = new DownloadImage();
            downloadImage.execute(url);
        }else {
            progressBarImagePerfil.setVisibility(View.GONE);
        }

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL myURL = new URL(strings[0]);
                //URL myURL = new URL("http://i.stack.imgur.com/WxVXe.jpg");
                HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();

                String userCredentials = "gabrielm@gabriel.municipiodigital.com.br:,z[V;Dku*3Jv";
                String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), 0));

                connection.setRequestProperty("Authorization", basicAuth);
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.connect();

                Log.d("result", "doInBackground: " + connection.getResponseCode());
                InputStream is = connection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(new FlushedInputStream(is));
                connection.disconnect();
                return bmp;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Bitmap bmp) {

            if(bmp != null) {
                imageEditarPerfil.setImageBitmap(bmp);
            }else{
                Toast.makeText(EditarPerfilActivity.this, "Não foi possível carregar a foto de perfil. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
            progressBarImagePerfil.setVisibility(View.GONE);
        }
    }


    private void configRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + MySharedPreferences.getToken(getApplicationContext()))
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

                switch (requestCode){
                    case SELECAO_GALERIA:
                        localImagemSelecionada = data.getData();
                        imageRecuperadaGaleria = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
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

            Call<PathModel> call = fileService.uploadFile(Converters.converterBitmapToMultipartBody(imageRecuperadaGaleria),  "profile_image");
            call.enqueue(new Callback<PathModel>() {
                @Override
                public void onResponse(Call<PathModel> call, Response<PathModel> response) {
                    if (response.isSuccessful()){
                        response.body();
                        currentUser.setProfileImage_path_name(response.body().getPath());
                        updateUserInfos();
                    }else{
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(EditarPerfilActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(EditarPerfilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PathModel> call, Throwable t) {
                    Toast.makeText(EditarPerfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EditarPerfilActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EditarPerfilActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                if(openByDialog){
                    finish();
                    startActivity(new Intent(EditarPerfilActivity.this, MainActivity.class));
                }else {
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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