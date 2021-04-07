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
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.model.AccountCredentials;

import com.cursoandroid.gabriel.instagramclone.services.AuthService;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail, editSenha;
    private Button buttonLogin;
    private static final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").addConverterFactory(GsonConverterFactory.create()).build();
    private static final AuthService authService = retrofit.create(AuthService.class);
    private AlertDialog dialog;
    private MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inicializarComponentes();
        verificarUsuarioLogado();

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Logando...")
                .setCancelable(false)
                .build();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario();
            }
        });

    }

    public void verificarUsuarioLogado(){
        String tokenRecuperado = mySharedPreferences.getToken();
        if(!tokenRecuperado.equals("")){
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    public void inicializarComponentes(){
        buttonLogin = findViewById(R.id.buttonLogin);
        editEmail = findViewById(R.id.editEmailLogin);
        editSenha = findViewById(R.id.editSenhaLogin);
        editEmail.requestFocus();
        mySharedPreferences = new MySharedPreferences(this);
    }

    public void abrirCadastro(View view){
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }
    public void validarUsuario(){

        String campoEmail = editEmail.getText().toString();
        String campoSenha = editSenha.getText().toString();

        if(!campoEmail.isEmpty() && !campoSenha.isEmpty()){

            AccountCredentials accountCredentials = new AccountCredentials(campoEmail, campoSenha);
            logarUsuario(accountCredentials);

        }else{
            Toast.makeText(LoginActivity.this, "Preencha todos os dados", Toast.LENGTH_SHORT).show();
        }
    }

    private void logarUsuario(AccountCredentials accountCredentials) {
        dialog.show();

        Call<Void> call = authService.loginUsuario(accountCredentials);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful() && response.headers().get("Authorization") != null) {
                    // Do your success stuff...
                    mySharedPreferences.saveToken(response.headers().get("Authorization"));
                    if(!mySharedPreferences.getToken().equals("")) {
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                } else {
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, json.getString("details"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }


}