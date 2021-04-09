package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.model.Post;
import com.cursoandroid.gabriel.instagramclone.services.UserServices;
import com.like.LikeButton;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Post> listPost;
    private Retrofit retrofit;
    private UserServices userServices;
    private Context context;

    public AdapterFeed(List<Post> listPost, Retrofit retrofit, Context context) {
        this.listPost = listPost;
        this.retrofit = retrofit;
        userServices = retrofit.create(UserServices.class);
        this.context = context;
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

        ImageDownloaderGlide.downloadImage(post.getImageUrl(), context, holder.postImage);
        holder.name.setText(post.getUsername());
        if (post.getUserImageUrl() != null ) ImageDownloaderGlide.downloadImage(post.getUserImageUrl(), context, holder.profilePhoto);        holder.description.setText(post.getDescription());


    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profilePhoto;
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
