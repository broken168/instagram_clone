package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterMiniaturas;
import com.cursoandroid.gabriel.instagramclone.helper.Converters;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.helper.RecyclerItemClickListener;
import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.services.FileService;
import com.cursoandroid.gabriel.instagramclone.services.PostService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.textfield.TextInputEditText;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FiltroActivity extends AppCompatActivity {

    static
    { System.loadLibrary("NativeImageProcessor"); }

    private ImageView imagefotoEscolhida;
    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private List<ThumbnailItem> listaFiltros = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterMiniaturas adapterMiniaturas;
    private TextInputEditText textDescricao;

    private UserProfile currentUser;

    private Retrofit retrofit;
    private UserServices userServices;
    private FileService fileService;
    private PostService postService;

    private AlertDialog dialog;

    private AlertDialog dialogCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
        inicializarComponentes();

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Publicando...")
                .setCancelable(false)
                .build();

        dialogCurrentUser = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Carregando dados do usuário...")
                .setCancelable(false)
                .build();

        Toolbar toolbar = findViewById(R.id.toolbarAlternativa);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        configRetrofit();
        getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imagefotoEscolhida.setImageBitmap(imagem);
            imagemFiltro = imagem.copy(imagem.getConfig(), true);

            adapterMiniaturas = new AdapterMiniaturas(listaFiltros, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapterMiniaturas);

            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ThumbnailItem item = listaFiltros.get(position);

                    imagemFiltro = imagem.copy(imagem.getConfig(), true);
                    Filter filtro = item.filter;
                    imagefotoEscolhida.setImageBitmap(filtro.processFilter(imagemFiltro));
                }
                @Override
                public void onLongItemClick(View view, int position) {
                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            }
            ));
            recuperarFiltros();
        }

    }

    private void getCurrentUser() {
    }

    private void configRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", new MySharedPreferences(FiltroActivity.this).getToken())
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        retrofit = new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").client(client).addConverterFactory(GsonConverterFactory.create()).build();
        fileService = retrofit.create(FileService.class);
        postService = retrofit.create(PostService.class);
        userServices = retrofit.create(UserServices.class);
    }



    public void recuperarFiltros(){
         ThumbnailsManager.clearThumbs();

        listaFiltros.clear();
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb(item);

        List<Filter> filtros = FilterPack.getFilterPack(getApplicationContext());

        for( Filter filtro: filtros){
            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filtro;
            itemFiltro.filterName = filtro.getName();

            ThumbnailsManager.addThumb(itemFiltro);
        }

        listaFiltros.addAll(ThumbnailsManager.processThumbs(getApplicationContext()));
        adapterMiniaturas.notifyDataSetChanged();

    }

    private void publishPostPhoto() {
        dialog.show();

        if(textDescricao.getText() != null) {

            Bitmap bmImage = ((BitmapDrawable)imagefotoEscolhida.getDrawable()).getBitmap();

            /*
            Call<PathModel> call = fileService.uploadFile(Converters.converterBitmapToMultipartBody(bmImage), "post_image");

            call.enqueue(new Callback<PathModel>() {
                @Override
                public void onResponse(Call<PathModel> call, Response<PathModel> response) {
                    if(response.isSuccessful() && response.body() != null){
                        createPost(response.body());
                    }else{
                        Toast.makeText(FiltroActivity.this, Converters.converterErrorBodyToString(response), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<PathModel> call, Throwable t) {

                }
            });

             */

        }else{
            Toast.makeText(this, "A postagem precisa de uma legenda", Toast.LENGTH_SHORT).show();
        }


    }

    private void createPost() {
        Post post = new Post();
        post.setDescription(textDescricao.getText().toString());
        //post.setImageUrl(body.getPath());

        Call<Post> call = postService.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(FiltroActivity.this, "Post publicado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(FiltroActivity.this, Converters.converterErrorBodyToString(response), Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(FiltroActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }




    public void inicializarComponentes(){
        imagefotoEscolhida = findViewById(R.id.imageFotoEscolhida);
        recyclerView = findViewById(R.id.recyclerFiltros);
        textDescricao = findViewById(R.id.textDescricaoFiltro);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.ic_salvar_postagem:
                publishPostPhoto();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}