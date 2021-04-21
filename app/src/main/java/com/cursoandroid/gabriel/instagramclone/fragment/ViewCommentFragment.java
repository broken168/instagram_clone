package com.cursoandroid.gabriel.instagramclone.fragment;

import android.app.ActionBar;
import android.content.Context;
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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private AppCompatEditText editText;

    private FragmentActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

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

        editText = view.findViewById(R.id.editTextComentario);
        FloatingActionButton fab = view.findViewById(R.id.fabAdicionarComentario);
        fab.setOnClickListener(v -> commentValidation());
        commentService = Configurators.retrofitConfigurator(view.getContext()).create(CommentService.class);
        recyclerView = view.findViewById(R.id.recyclerComentarios);
        adapterComentarios = new AdapterComentarios(commentList, view.getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapterComentarios);
        loadComments();
    }

    public void commentValidation(){
        if (!editText.getText().toString().isEmpty()){
            newComment(editText.getText().toString());
            editText.setText("");
        }
        else Toast.makeText(activity, "Comente algo", Toast.LENGTH_SHORT).show();
    }

    private void newComment(String msg){
        Comment comment = new Comment(null, null, msg, null);
        Call<Void> call = commentService.addComment(comment,  postId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    String[] vet = response.headers().get("Location").split("/");
                    addNewCommentToList(Long.valueOf(vet[vet.length-1]));
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

            private void addNewCommentToList(Long id) {
                Call<Comment> call1 = commentService.getCommentById(id);
                call1.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if ( response.isSuccessful()){
                            commentList.add(0,  response.body());
                            adapterComentarios.notifyItemInserted(0);
                            recyclerView.scrollToPosition(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {

                    }
                });
            }
        });

    }
    private void loadComments(){
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
                }
            }

            @Override
            public void onFailure(Call<CommentSearch> call, Throwable t) {
                Toast.makeText(activity, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
