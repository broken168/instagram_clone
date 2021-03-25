package com.cursoandroid.gabriel.instagramclone.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.activity.PerfilAmigoActivity;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterPesquisa;
import com.cursoandroid.gabriel.instagramclone.helper.MySharedPreferences;
import com.cursoandroid.gabriel.instagramclone.helper.RecyclerItemClickListener;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.model.Usuario;
import com.cursoandroid.gabriel.instagramclone.services.FileService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PesquisaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PesquisaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SearchView searchViewPesquisa;
    private RecyclerView recyclerViewPesquisar;

    private Retrofit retrofit;
    private UserServices userServices;


    private List<UserProfile> listaUsuarios = new ArrayList<>();
    private AdapterPesquisa adapterPesquisa;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PesquisaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PesquisaFragment newInstance(String param1, String param2) {
        PesquisaFragment fragment = new PesquisaFragment();
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
        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchViewPesquisa = view.findViewById(R.id.searchViewPesquisa);
        recyclerViewPesquisar = view.findViewById(R.id.recyclerViewPesquisa);
        recyclerViewPesquisar.setHasFixedSize(true);
        recyclerViewPesquisar.setLayoutManager(new LinearLayoutManager(getActivity()));

        configRetrofit();

        adapterPesquisa = new AdapterPesquisa(listaUsuarios, getActivity());

        recyclerViewPesquisar.setAdapter(adapterPesquisa);

        recyclerViewPesquisar.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerViewPesquisar, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Usuario usuario = listaUsuarios.get(position);
                Intent i = new Intent(getActivity(), PerfilAmigoActivity.class);
                i.putExtra("usuarioSelecionado", "usuario");
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        searchViewPesquisa.setQueryHint("Buscar usuários");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                pesquisarUsuarios(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return view;
    }

    private void configRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + MySharedPreferences.getToken(Objects.requireNonNull(getActivity())))
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
        retrofit = new Retrofit.Builder().baseUrl("http://189.84.65.150:8080").client(client).addConverterFactory(GsonConverterFactory.create()).build();
        userServices = retrofit.create(UserServices.class);
    }
    private void pesquisarUsuarios(String username){

        listaUsuarios.clear();

        Call<List<UserProfile>> call = userServices.getUserProfileByUsername(username);
        call.enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<UserProfile> list = response.body();
                    for(UserProfile userProfile : response.body()){
                        if(userProfile.getIdUser() != MySharedPreferences.getCurrentUserID(Objects.requireNonNull(getActivity()))) {
                            listaUsuarios.add(userProfile);
                        }
                    }
                    listaUsuarios = response.body();
                    adapterPesquisa.notifyDataSetChanged();
                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(getActivity(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                Toast.makeText(getActivity(), "Erro ao fazer pesquisa. " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}