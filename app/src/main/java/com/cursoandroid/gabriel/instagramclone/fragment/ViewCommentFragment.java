package com.cursoandroid.gabriel.instagramclone.fragment;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.adapter.AdapterComentarios;
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.model.Comment;
import com.cursoandroid.gabriel.instagramclone.search.CommentSearch;
import com.cursoandroid.gabriel.instagramclone.services.CommentService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCommentFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private Long postId;
    private AdapterComentarios adapterComentarios;
    private CommentService commentService;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(Build.VERSION.SDK_INT >= 30){
            getActivity().getDisplay().getRealMetrics(displayMetrics);
        }else {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        getDialog().getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, (displayMetrics.heightPixels / 3) * 2 );
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
        getDialog().setCancelable(true);
    }

    public ViewCommentFragment(Long postId){
        this.postId = postId;
    }

    public ViewCommentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_visualizar_comentarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commentService = Configurators.retrofitConfigurator(view.getContext()).create(CommentService.class);
        recyclerView = view.findViewById(R.id.recyclerComentarios);
        adapterComentarios = new AdapterComentarios(commentList, view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapterComentarios);
        loadComments(postId, view);

    }

    public void loadComments(Long postId, View view){

        Call<CommentSearch> call = commentService.getCommentsByPost(postId);
        call.enqueue(new Callback<CommentSearch>() {
            @Override
            public void onResponse(Call<CommentSearch> call, Response<CommentSearch> response) {
                if(response.isSuccessful()){
                    if (response.body().getContent() != null){
                        commentList.addAll(response.body().getContent());
                        Collections.sort(commentList);
                        adapterComentarios.notifyDataSetChanged();
                    }
                }else{
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(view.getContext(), json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommentSearch> call, Throwable t) {
                Toast.makeText(view.getContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
