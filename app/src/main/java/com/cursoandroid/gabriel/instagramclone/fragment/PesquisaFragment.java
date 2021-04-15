package com.cursoandroid.gabriel.instagramclone.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.helper.RecyclerItemClickListener;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.search.UserSearch;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    private UserProfile currentUser;

    private List<UserProfile> listaUsuarios = new ArrayList<>();
    private AdapterPesquisa adapterPesquisa;
    private UserSearch userSearch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PesquisaFragment() {
        // Required empty public constructor
    }

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof Activity){
            activity = (Activity) context;
        }
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
        recyclerViewPesquisar.setLayoutManager(new LinearLayoutManager(activity));

        configRetrofit();
        getCurrentUser();

        adapterPesquisa = new AdapterPesquisa(listaUsuarios, activity);

        recyclerViewPesquisar.setAdapter(adapterPesquisa);

        recyclerViewPesquisar.addOnItemTouchListener(new RecyclerItemClickListener(activity, recyclerViewPesquisar, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Usuario usuario = listaUsuarios.get(position);
                Intent i = new Intent(activity, PerfilAmigoActivity.class);
                i.putExtra("user", listaUsuarios.get(position).getId());
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

    private void getCurrentUser() {
        Call<UserProfile> call = userServices.getCurrentUser();
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    currentUser = response.body();
                }else{
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(activity, json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void configRetrofit() {
        retrofit = Configurators.retrofitConfigurator(activity);
        userServices = retrofit.create(UserServices.class);
    }

    private void pesquisarUsuarios(String username){

        listaUsuarios.clear();

        Call<UserSearch> call = userServices.getUserProfileByUsername(username);
        call.enqueue(new Callback<UserSearch>() {
            @Override
            public void onResponse(Call<UserSearch> call, Response<UserSearch> response) {
                if(response.isSuccessful()){
                    userSearch = response.body();
                    for(UserProfile user : response.body().getContent()){
                        if (!user.getId().equals(currentUser.getId())) listaUsuarios.add(user);
                    }
                    adapterPesquisa.notifyDataSetChanged();
                }else{
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(activity, json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<UserSearch> call, Throwable t) {
                Toast.makeText(activity, "Erro ao fazer pesquisa. " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}