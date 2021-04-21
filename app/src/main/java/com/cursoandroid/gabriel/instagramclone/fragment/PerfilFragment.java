package com.cursoandroid.gabriel.instagramclone.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.FragmentKt;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.cursoandroid.gabriel.instagramclone.activity.PerfilAmigoActivity;
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
import java.util.List;
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
public class PerfilFragment extends Fragment implements FeedCounter {

    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private ImageButton buttonEditarPerfil;
    private ShapeableImageView imgPerfil;
    public GridView gridViewPerfil;
    private AdapterGrid adapterGrid;
    private PostSearch postSearch;
    private final List<String> imagesUrl = new ArrayList<>();

    private UserServices userServices;
    private PostService postService;
    private UserProfile currentUser;
    private int currentPage = 0;

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

    private FragmentActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
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
    public static PerfilFragment newInstance(String param1, String param2){
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carregarInformacoesUsuario();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        inicializarComponentes(view);
        configRetrofit();

        adapterGrid = new AdapterGrid(activity, R.layout.grid_postagem, imagesUrl, PerfilFragment.this);
        gridViewPerfil.setAdapter(adapterGrid);

        buttonEditarPerfil.setOnClickListener(v ->
                FragmentKt.findNavController(PerfilFragment.this).navigate(R.id.action_profile_to_edit_profile));

        return view;
    }

    private void configRetrofit() {
        Retrofit retrofit = Configurators.retrofitConfigurator(activity);
        userServices = retrofit.create(UserServices.class);
        postService = retrofit.create(PostService.class);
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

    public void carregarInformacoesUsuario(){
        Call<UserProfile> call = userServices.getCurrentUser();
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    currentUser = response.body();
                    loadUserData();
                    loadImagesPosts(currentUser.getId());
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(activity, "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadImagesPosts(Long id) {

        Call<PostSearch> call = postService.getPostsByUserId(id, currentPage);
        call.enqueue(new Callback<PostSearch>() {
            @Override
            public void onResponse(Call<PostSearch> call, Response<PostSearch> response) {

                if (response.isSuccessful()) {
                    postSearch = response.body();
                    textPublicacoes.setText(String.valueOf(postSearch.getTotalElements()));
                    if(currentPage == 0 && imagesUrl.size() > 0)
                        return;

                    for (Post post : postSearch.getContent()) {
                        imagesUrl.add(post.getImageUrl());
                    }
                    adapterGrid.notifyDataSetChanged();

                }
            }
            @Override
            public void onFailure(Call<PostSearch> call, Throwable t) {
                Toast.makeText(activity, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
            ImageDownloaderGlide.loadImage(url, activity, progressBarImagePerfil, imgPerfil);
        }else{
            progressBarImagePerfil.setVisibility(View.GONE);
        }

    }

    @Override
    public void count(int position) {
        if(postSearch != null &&
                !postSearch.isLast() &&
                position == ( postSearch.getNumberOfElements() * (currentPage+1) - 5)
        )
        {
            Toast.makeText(activity, "atualizado", Toast.LENGTH_SHORT).show();
            currentPage++;
            carregarInformacoesUsuario();
        }
    }
}