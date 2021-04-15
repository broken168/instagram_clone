package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderPicasso;
import com.cursoandroid.gabriel.instagramclone.model.Comment;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;


public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.MyViewHolder> {

    private List<Comment> listaComments;
    private Context context;

    public AdapterComentarios(List<Comment> listaComments, Context context) {
        this.listaComments = listaComments;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comentarios, parent, false);
        return new AdapterComentarios.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comment comment = listaComments.get(position);

        if(comment.getUserImageUrl() != null){
            ImageDownloaderGlide.loadImage(comment.getUserImageUrl(), context, null, holder.imageView);
        }

        holder.textComentario.setText(comment.getMsg());
        holder.textNome.setText(comment.getUsername());

    }

    @Override
    public int getItemCount() {
        return listaComments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        private TextView textNome, textComentario;
        private ShapeableImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imagePerfilComentario);
            textComentario = itemView.findViewById(R.id.textComentario);
            textNome = itemView.findViewById(R.id.textNomeComentario);
        }
    }
}
