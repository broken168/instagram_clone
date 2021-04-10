package com.cursoandroid.gabriel.instagramclone.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterFeed;
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.cursoandroid.gabriel.instagramclone.search.PostSearch;
import com.cursoandroid.gabriel.instagramclone.services.PostService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerFeed;
    private AdapterFeed adapterFeed;

    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Post> postList = new ArrayList<>();

    private Retrofit retrofit;
    private PostService postService;
    private UserServices userServices;

    private UserProfile currentUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerFeed = view.findViewById(R.id.recyclerFeed);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        configRetrofit();

        adapterFeed = new AdapterFeed(postList, retrofit, getActivity());
        recyclerFeed.setHasFixedSize(true);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerFeed.setAdapter(adapterFeed);

        return view;
    }

    private void configRetrofit() {
        retrofit = Configurators.retrofitConfigurator(getActivity());
        postService = retrofit.create(PostService.class);
        userServices = retrofit.create(UserServices.class);
    }

    private void getCurrentUser(){
        Call<UserProfile> call = userServices.getCurrentUser();
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if(response.isSuccessful()){
                    currentUser = response.body();
                    listarFeed();
                }
                else {
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(getActivity(), json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void listarFeed(){
        postList.clear();
        List<Long> list = currentUser.getFollowing();
        StringBuilder ids = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            ids.append(list.get(i));
            if(i+1 != list.size()){
                ids.append(",");
            }
        }
        if(!ids.toString().isEmpty()) getPostsByIds(ids.toString());

    }

    private void getPostsByIds(String ids) {
        Call<PostSearch> call = userServices.getPostsByIds(ids);
        call.enqueue(new Callback<PostSearch>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<PostSearch> call, Response<PostSearch> response) {
                if(response.isSuccessful()){
                    for (UserProfile user : response.body().getContent()){
                        for(Post post : user.getPosts()){
                            post.setUsername(user.getUsername());
                            post.setUserImageUrl(user.getImageUrl());
                            postList.add(post);
                        }
                    }
                    if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

                    Collections.sort(postList, Comparator.comparing(Post::getId).reversed());
                    adapterFeed.notifyDataSetChanged();

                }
                else {
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(getActivity(), json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostSearch> call, Throwable t) {
                Toast.makeText(getActivity(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRefresh() {
        listarFeed();
    }
}