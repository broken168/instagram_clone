package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterGrid;
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.helper.FeedCounter;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.search.PostSearch;
import com.cursoandroid.gabriel.instagramclone.services.PostService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilAmigoActivity extends AppCompatActivity implements FeedCounter {

    private TextView textButtonAcaoPerfil;
    private ShapeableImageView imagemPerfil;
    private ImageButton buttonAcaoPerfil;
    private GridView gridViewPerfil;
    private ProgressBar progressBar;

    private UserProfile friendUser;
    private UserProfile currentUser;

    private UserServices userServices;
    private PostService postService;
    private PostSearch postSearch;
    private int currentPage = 0;
    private List<String> imagesUrl = new ArrayList<>();

    private AdapterGrid adapterGrid;

    private TextView textPublicacoes, textSeguidores, textSeguindo;

    private AlertDialog dialog;

    private boolean following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configurações iniciais
        inicializarComponentes();
        configRetrofit();

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperado dados. Aguarde...")
                .setCancelable(false)
                .build();
        dialog.show();

        Toolbar toolbar = findViewById(R.id.toolbarAlternativa);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        adapterGrid = new AdapterGrid(getApplicationContext(), R.layout.grid_postagem, imagesUrl, PerfilAmigoActivity.this);
        gridViewPerfil.setAdapter(adapterGrid);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            retrieveUser(bundle.getLong("user"));
        }

        buttonAcaoPerfil.setOnClickListener(v -> saveFollow());

    }


    private void configRetrofit() {
        Retrofit retrofit = Configurators.retrofitConfigurator(PerfilAmigoActivity.this);
        userServices = retrofit.create(UserServices.class);
        postService = retrofit.create(PostService.class);
    }

    public void inicializarComponentes(){
        textButtonAcaoPerfil = findViewById(R.id.textBotaoAcaoPerfil);
        imagemPerfil = findViewById(R.id.imagePerfil);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);
        textButtonAcaoPerfil.setText("Carregando...");
        gridViewPerfil = findViewById(R.id.gridViewPerfil);
        progressBar = findViewById(R.id.progressBarEditarPerfil);
    }

    public void carregarFotosPostagem(){

        int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
        int tamanhoImagem = tamanhoGrid / 3;
        gridViewPerfil.setColumnWidth( tamanhoImagem );

        Call<PostSearch> call = postService.getPostsByUserId(friendUser.getId(), currentPage);
        call.enqueue(new Callback<PostSearch>() {
            @Override
            public void onResponse(Call<PostSearch> call, Response<PostSearch> response) {

                if (response.isSuccessful()) {
                    postSearch = response.body();
                    for (Post post : postSearch.getContent()) {
                        imagesUrl.add(post.getImageUrl());
                    }
                    adapterGrid.notifyDataSetChanged();
                    textPublicacoes.setText(String.valueOf(postSearch.getTotalElements()));
                }
            }
            @Override
            public void onFailure(Call<PostSearch> call, Throwable t) {
                Toast.makeText(PerfilAmigoActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void recuperarDadosUsuariosLogado(){
        Call<UserProfile> call = userServices.getCurrentUser();
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    currentUser = response.body();
                    if(followVerification()) textButtonAcaoPerfil.setText("Seguindo");
                    else textButtonAcaoPerfil.setText("Seguir");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(PerfilAmigoActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean followVerification(){
        following = currentUser.getFollowing().contains(friendUser.getId());
        return following;
    }

    private void saveFollow(){
        if(following){
            Call<Void> call = userServices.removeFollow(friendUser.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()) {
                        following = false;
                        textButtonAcaoPerfil.setText("Seguir");
                        Long number = (Long.parseLong( textSeguidores.getText().toString() ) - 1L);
                        textSeguidores.setText(String.valueOf(number));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PerfilAmigoActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Call<Void> call = userServices.newFollow(friendUser.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        following = true;
                        textButtonAcaoPerfil.setText("Seguindo");
                        Long number = (Long.parseLong( textSeguidores.getText().toString() ) + 1L);
                        textSeguidores.setText(String.valueOf(number));
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PerfilAmigoActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void retrieveUser(Long id){
        Call<UserProfile> call = userServices.getUserById(id);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()) {
                    friendUser = response.body();
                    loadUserData();
                    recuperarDadosUsuariosLogado();
                    carregarFotosPostagem();
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(PerfilAmigoActivity.this, "Failure:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            private void loadUserData() {
                getSupportActionBar().setTitle(friendUser.getUsername());
                List<Long> followers = friendUser.getFollowers();
                List<Long> following = friendUser.getFollowing();
                Long postsNumber = friendUser.getPostsNumber();
                if(followers != null) textSeguidores.setText(String.valueOf(followers.size()));
                if(following != null) textSeguindo.setText(String.valueOf(following.size()));
                if(postsNumber != null) textSeguindo.setText(String.valueOf(postsNumber));

                String url = friendUser.getImageUrl();
                if(url != null && !url.equals("")){
                    ImageDownloaderGlide.loadImage(url, getApplicationContext(), progressBar, imagemPerfil);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void count(int position) {
        if(postSearch != null &&
                !postSearch.isLast() &&
                position == ( postSearch.getNumberOfElements() * (currentPage+1) - 5)
        )
        {
            Toast.makeText(PerfilAmigoActivity.this, "atualizado", Toast.LENGTH_SHORT).show();
            currentPage++;
            carregarFotosPostagem();
        }
    }
}