package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.model.Post;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private TextView textPerfilPostagem, textQtdCurtidasPostagem, textDescricao;
    private ImageView imagePostagemSelecionada;
    private CircleImageView imagemPerfilPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        inicializarComponentes();

        Toolbar toolbar = findViewById(R.id.toolbarAlternativa);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visualizar Postagem");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Post postagem = (Post) bundle.getSerializable("postagem");


            /*
            Uri uriPostagem = Uri.parse(postagem.getCaminhoFoto());
            Glide.with(VisualizarPostagemActivity.this).load(uriPostagem).into(imagePostagemSelecionada);

            textDescricao.setText(postagem.getDescricao());
            textPerfilPostagem.setText(usuario.getFullName());

             */

        }
    }
    public void inicializarComponentes(){

        imagemPerfilPostagem = findViewById(R.id.imagePerfilPostagem);
        textDescricao = findViewById(R.id.textDescricaoPostagem);
        textPerfilPostagem = findViewById(R.id.textPerfilPostagem);
        textQtdCurtidasPostagem = findViewById(R.id.textQtdCurtidasPostagem);
        imagePostagemSelecionada = findViewById(R.id.imagePostagemSelecionada);

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}