package com.cursoandroid.gabriel.instagramclone.adapter;

import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.fragment.ViewCommentFragment;
import com.cursoandroid.gabriel.instagramclone.helper.FeedCounter;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.google.android.material.imageview.ShapeableImageView;
import com.like.LikeButton;
import java.util.List;
import retrofit2.Retrofit;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Post> listPost;
    private Retrofit retrofit;
    private UserServices userServices;
    private View view;
    private FragmentActivity activity;
    private Integer position;
    private FeedCounter feedCounter;

    public AdapterFeed(List<Post> listPost, Retrofit retrofit, View view, FragmentActivity activity) {
        this.listPost = listPost;
        this.retrofit = retrofit;
        userServices = retrofit.create(UserServices.class);
        this.view = view;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new AdapterFeed.MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = listPost.get(position);

        ImageDownloaderGlide.loadImage(post.getImageUrl(), activity, null, holder.postImage);
        holder.name.setText(post.getUsername());
        if (post.getUserImageUrl() != null ) ImageDownloaderGlide.loadImage(post.getUserImageUrl(), activity, null, holder.profilePhoto);
        holder.description.setText(post.getDescription());
        holder.comentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCommentFragment viewCommentFragment = new ViewCommentFragment(post.getId());
                viewCommentFragment.show(activity.getSupportFragmentManager(), "ViewCommentFragment");

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
