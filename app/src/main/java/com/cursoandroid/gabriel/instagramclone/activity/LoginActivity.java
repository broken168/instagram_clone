package com.cursoandroid.gabriel.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.model.AccountCredentials;
import com.cursoandroid.gabriel.instagramclone.model.ModelToken;
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
        String tokenRecuperado = MySharedPreferences.getToken(getApplicationContext());
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

        Call<ModelToken> call = authService.loginUsuario(accountCredentials);

        call.enqueue(new Callback<ModelToken>() {
            @Override
            public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {

                if (response.isSuccessful()) {
                    // Do your success stuff...
                    MySharedPreferences.saveToken(getApplicationContext(), response.body().getToken());
                    MySharedPreferences.saveCurrentUserID(getApplicationContext(), response.body().getId());
                    if(!MySharedPreferences.getToken(getApplicationContext()).equals("")) {
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ModelToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        });
    }


}