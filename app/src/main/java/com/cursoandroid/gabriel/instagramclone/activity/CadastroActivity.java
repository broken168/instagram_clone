package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.dto.NewUserDTO;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.model.AccountCredentials;
import com.cursoandroid.gabriel.instagramclone.services.AuthService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText email, nome, senha;
    private Button buttonCadastrar;
    private AlertDialog dialog;
    private Retrofit retrofit;
    private AuthService authService;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Cadastrando...")
                .setCancelable(false)
                .build();



        retrofit = new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").addConverterFactory(GsonConverterFactory.create()).build();
        authService = retrofit.create(AuthService.class);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario();
            }
        });

    }

    public void inicializarComponentes(){
        email = findViewById(R.id.editEmailCadastro);
        nome = findViewById(R.id.editNomeCadastro);
        senha = findViewById(R.id.editSenhaCadastro);
        buttonCadastrar = findViewById(R.id.buttonCadastrarConta);
        mySharedPreferences = new MySharedPreferences(getApplicationContext());
        email.requestFocus();
    }

    //validar todos os campos do usuario e configura objeto usuario
    public void validarUsuario(){
        Log.i("result", "clicado");

        String nomeCadastro = nome.getText().toString();
        String senhaCadastro = senha.getText().toString();
        String emailCadastro = email.getText().toString();

        if(!nomeCadastro.isEmpty() && !senhaCadastro.isEmpty() && !emailCadastro.isEmpty()){

            NewUserDTO user = new NewUserDTO(emailCadastro, nomeCadastro, senhaCadastro);
            cadastrarUsuario(user);

        }else{
            Toast.makeText(CadastroActivity.this, "Preencha todos os dados", Toast.LENGTH_SHORT).show();
        }
    }

    //cadastrar o usuario no firebase
    public void cadastrarUsuario(NewUserDTO user){
        dialog.show();

        Call<Void> call = authService.registrarUsuario(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response){

                if (response.isSuccessful()) {
                    // Do your success stuff...
                    Toast.makeText(CadastroActivity.this, "Cadastro feito com sucesso", Toast.LENGTH_SHORT).show();
                    autoLogin(user);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JSONArray jsonArray = jObjError.getJSONArray("errors");
                        Dialog.dialogError(CadastroActivity.this, jsonArray.getJSONObject(0).getString("message"), null);
                    } catch (Exception e) {
                        Toast.makeText(CadastroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
            }

            private void autoLogin(NewUserDTO user) {
                Call<Void> call = authService.loginUsuario(new AccountCredentials(user.getEmail(), user.getPassword()));
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful() && response.headers().get("Authorization") != null) {
                            // Do your success stuff...
                            mySharedPreferences.saveToken(response.headers().get("Authorization"));
                            if(!mySharedPreferences.getToken().equals("")) {
                                finish();
                                startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                            }
                        } else {
                            try {
                                JSONObject json = new JSONObject(response.errorBody().string());
                                Dialog.dialogError(CadastroActivity.this, json.getString("message"), json.getString("details"));
                            } catch (Exception e) {
                                Toast.makeText(CadastroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CadastroActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("kkkk", "onResponse: error");
                Toast.makeText(CadastroActivity.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

}