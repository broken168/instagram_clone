package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterComentarios;
import com.cursoandroid.gabriel.instagramclone.model.Comentario;
import com.cursoandroid.gabriel.instagramclone.model.Feed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VisualizarComentariosActivity extends AppCompatActivity {

    private EditText editTextComentarioDigitado;
    private FloatingActionButton fabSalvarComentario;
    private RecyclerView recyclerView;

    private Feed feed;
    private List<Comentario> listaComentarios = new ArrayList<>();
    private AdapterComentarios adapterComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_comentarios);

        Toolbar toolbar = findViewById(R.id.toolbarAlternativa);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visualizar comentários");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        inicializarComponentes();

        fabSalvarComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarComentario(editTextComentarioDigitado.getText().toString());
                editTextComentarioDigitado.setText("");
            }
        });

        adapterComentarios = new AdapterComentarios(listaComentarios, getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapterComentarios);

    }

    public void salvarComentario(String textoDigitado){



    }

    public void carregarComentarios(){

        listaComentarios.clear();



    }

    public void inicializarComponentes(){

        editTextComentarioDigitado = findViewById(R.id.editTextComentario);
        fabSalvarComentario = findViewById(R.id.fabAdicionarComentario);
        recyclerView = findViewById(R.id.recyclerComentarios);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            feed = (Feed) bundle.getSerializable("postagem");
            Log.i("result", "recuperou");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}