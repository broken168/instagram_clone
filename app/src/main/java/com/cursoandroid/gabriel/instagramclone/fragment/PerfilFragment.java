package com.cursoandroid.gabriel.instagramclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.activity.EditarPerfilActivity;
import com.cursoandroid.gabriel.instagramclone.activity.LoginActivity;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterGrid;
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.ImageDownloader;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.services.FileService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {

    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private ImageButton buttonEditarPerfil;
    private CircleImageView imgPerfil;
    public GridView gridViewPerfil;
    private AdapterGrid adapterGrid;

    private Retrofit retrofit;
    private UserServices userServices;
    private UserProfile currentUser;

    private ProgressBar progressBarImagePerfil;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        inicializarComponentes(view);
        configRetrofit();

        carregarInformacoesUsuario();
        carregarFotosPostagem();


        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });

        return view;
    }

    private void configRetrofit() {
        retrofit = Configurators.retrofitConfigurator(getActivity());
        userServices = retrofit.create(UserServices.class);
    }

    public void inicializarComponentes(View view){
        buttonEditarPerfil = view.findViewById(R.id.buttonAcaoPerfil);
        textPublicacoes = view.findViewById(R.id.textPublicacoes);
        textSeguidores = view.findViewById(R.id.textSeguidores);
        textSeguindo = view.findViewById(R.id.textSeguindo);
        imgPerfil = view.findViewById(R.id.imagePerfil);
        gridViewPerfil = view.findViewById(R.id.gridViewPerfil);
        progressBarImagePerfil = view.findViewById(R.id.progressBarEditarPerfil);

    }

    public void inicializarImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(Objects.requireNonNull(getActivity()))
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();

        if(!ImageLoader.getInstance().isInited()) ImageLoader.getInstance().init(config);
    }

    public void carregarFotosPostagem(){



    }
    public void carregarInformacoesUsuario(){
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
                        Toast.makeText(getActivity(), json.getString("details"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserData() {
        List<Long> followers = currentUser.getFollowers();
        List<Long> following = currentUser.getFollowing();
        Long postsNumber = currentUser.getPostsNumber();
        if(followers != null) textSeguidores.setText(String.valueOf(followers.size()));
        if(following != null) textSeguindo.setText(String.valueOf(following.size()));
        if(postsNumber != null) textSeguindo.setText(String.valueOf(postsNumber));

        String url = currentUser.getImageUrl();
        if(url != null && !url.equals("")){
            new ImageDownloader(imgPerfil, progressBarImagePerfil).execute(url);
        }else{
            progressBarImagePerfil.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inicializarImageLoader();
        Log.d(TAG, "onActivityCreated: criadaaaaa");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}