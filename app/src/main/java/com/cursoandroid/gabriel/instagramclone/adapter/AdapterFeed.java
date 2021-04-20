package com.cursoandroid.gabriel.instagramclone.adapter;

import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.fragment.ViewCommentFragment;
import com.cursoandroid.gabriel.instagramclone.helper.Configurators;
import com.cursoandroid.gabriel.instagramclone.helper.Dialog;
import com.cursoandroid.gabriel.instagramclone.helper.FeedCounter;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.services.PostService;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.imageview.ShapeableImageView;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Post> listPost;
    private Retrofit retrofit;
    private UserServices userServices;
    private View view;
    private FragmentActivity activity;
    private Integer position;
    private PostService postService;

    private FeedCounter feedCounter;


    public AdapterFeed(List<Post> listPost, Retrofit retrofit, View view, FragmentActivity activity, FeedCounter feedCounter) {
        this.listPost = listPost;
        this.retrofit = retrofit;
        userServices = retrofit.create(UserServices.class);
        this.view = view;
        this.activity = activity;
        postService = Configurators.retrofitConfigurator(activity).create(PostService.class);
        this.feedCounter = feedCounter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new AdapterFeed.MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Post post = listPost.get(position);
        feedCounter.count(position);

        ImageDownloaderGlide.loadImage(post.getImageUrl(), activity, null, holder.postImage);
        holder.name.setText(post.getUsername());
        if (post.getUserImageUrl() != null ) ImageDownloaderGlide.loadImage(post.getUserImageUrl(), activity, null, holder.profilePhoto);
        holder.description.setText(post.getDescription());

        if(post.getLikes() != null) holder.likeNumber.setText("Curtidas: " + post.getLikes());
        if(post.getLiked()) holder.likeButton.setLiked(true);
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(post.getLikes() == null) post.setLikes(1L);
                else post.setLikes(post.getLikes() + 1);
                holder.likeNumber.setText("Curtidas: " + post.getLikes());
                like(true, post, likeButton, holder);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                post.setLikes(post.getLikes() - 1);
                holder.likeNumber.setText("Curtidas: " + post.getLikes());
                like(false, post, likeButton, holder);
            }
        });
        holder.comentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCommentFragment viewCommentFragment = new ViewCommentFragment(post.getId());
                viewCommentFragment.show(activity.getSupportFragmentManager(), "ViewCommentFragment");

            }
        });

    }


    private void like(boolean like, Post post, LikeButton likeButton, MyViewHolder holder) {
        Call<Void> call = postService.like(post.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    if (like) {
                        post.setLikes(post.getLikes() - 1);
                        holder.likeNumber.setText("Curtidas: " + post.getLikes());
                        likeButton.setLiked(false);
                    } else {
                        post.setLikes(post.getLikes() + 1);
                        holder.likeNumber.setText("Curtidas: " + post.getLikes());
                        likeButton.setLiked(true);
                    }
                    try {
                        JSONObject json = new JSONObject(response.errorBody().string());
                        Dialog.dialogError(activity, json.getString("message"), json.getString("details"));
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView profilePhoto;
        TextView name, description, likeNumber;
        ImageView postImage;
        ImageButton comentButton;
        LikeButton likeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePhoto = itemView.findViewById(R.id.imagePerfilPostagem);
            name = itemView.findViewById(R.id.textPerfilPostagem);
            description = itemView.findViewById(R.id.textDescricaoPostagem);
            likeNumber = itemView.findViewById(R.id.textQtdCurtidasPostagem);
            postImage = itemView.findViewById(R.id.imagePostagemSelecionada);
            likeButton = itemView.findViewById(R.id.likeButtonFeed);
            comentButton = itemView.findViewById(R.id.imageButtonComentarioFeed);
        }
    }
}
