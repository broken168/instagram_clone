package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloader;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPesquisa extends RecyclerView.Adapter<AdapterPesquisa.MyViewHolder> {

    private List<UserProfile> userList;
    private Context context;
    private CircleImageView image;

    public AdapterPesquisa(List<UserProfile> l, Context c) {
        this.userList = l;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pesquisa_usuario, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserProfile user = userList.get(position);
        holder.nome.setText(user.getUsername());


        if(user.getImageUrl() != null) {
            new ImageDownloader(holder.foto).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user.getImageUrl());
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imagePerfilPesquisa);
            nome = itemView.findViewById(R.id.textNomePesquisa);
        }
    }

}
