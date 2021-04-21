package com.cursoandroid.gabriel.instagramclone.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.Converters;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.helper.Permissao;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.services.FileService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    private ShapeableImageView imageEditarPerfil;
    private TextView textAlterarFoto;
    private TextInputEditText editNomePerfil, editEmailPerfil;
    private Button buttonSalvar;
    private static final int SELECAO_GALERIA = 200;
    private Bitmap imageRecuperadaGaleria = null;
    private UserServices userServices;
    private FileService fileService;

    private UserProfile currentUser;
    private ProgressBar progressBarImagePerfil;
    private AlertDialog dialog;

    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };



    public EditProfileFragment() {
        // Required empty public constructor
    }


    private FragmentActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_editar_perfil, container, false);
        inicializarComponentes(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Permissao.validarPermissoes(permissoesNecessarias, activity, 2);

        configRetrofit();
        recuperarDadosUsuario();

        dialog = new SpotsDialog.Builder()
                .setContext(activity)
                .setMessage("Carregando dados...")
                .setCancelable(false)
                .build();

        buttonSalvar.setOnClickListener(view -> {
            if(!editNomePerfil.getText().toString().isEmpty()) {
                currentUser.setUsername(editNomePerfil.getText().toString());
                updateUserImage();
            }else{
                Toast.makeText(activity, "Preencha o usuário", Toast.LENGTH_SHORT).show();
            }
        });
        textAlterarFoto.setOnClickListener(v ->
        {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SELECAO_GALERIA);
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
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(activity, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadUserData() {
        editEmailPerfil.setText(currentUser.getEmail());
        editNomePerfil.setText(currentUser.getUsername());

        String url = currentUser.getImageUrl();
        if(url != null && !url.equals("")) {
            ImageDownloaderGlide.loadImage(url, activity, progressBarImagePerfil, imageEditarPerfil);
        }else {
            progressBarImagePerfil.setVisibility(View.GONE);
        }

    }


    private void configRetrofit() {
        Retrofit retrofit = Configurators.retrofitConfigurator(activity);
        fileService = retrofit.create(FileService.class);
        userServices = retrofit.create(UserServices.class);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            try{

                if (requestCode == SELECAO_GALERIA) {
                    Uri localImagemSelecionada = data.getData();
                    imageRecuperadaGaleria = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), localImagemSelecionada);
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
            Call<Void> call = fileService.uploadFile(Converters.convertBitmapToMultipartBody(imageRecuperadaGaleria), "profile_image");
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        currentUser.setImageUrl(response.headers().get("Location"));
                        updateUserInfos();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(activity, "Falha ao atualizar imagem: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "Infos atualizadas com sucesso!", Toast.LENGTH_SHORT).show();
                }
                close();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                close();

            }
            private void close() {
                dialog.dismiss();
                activity.getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void inicializarComponentes(View view){
        imageEditarPerfil = view.findViewById(R.id.imageEditarPerfil);
        textAlterarFoto = view.findViewById(R.id.textAlterarFoto);
        editEmailPerfil = view.findViewById(R.id.editEmailPerfil);
        editNomePerfil = view.findViewById(R.id.editNomePerfil);
        progressBarImagePerfil = view.findViewById(R.id.progressBarEditarPerfil);
        buttonSalvar = view.findViewById(R.id.buttonSalvarAlteracoesPerfil);
        editEmailPerfil.setFocusable(false);

    }



}