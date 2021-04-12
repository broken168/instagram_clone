package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.model.Comentario;

import java.util.List;


public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.MyViewHolder> {

    private List<Comentario> listaComentarios;
    private Context context;

    public AdapterComentarios(List<Comentario> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
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

        Comentario comentario = listaComentarios.get(position);

        if(!comentario.getCaminhoFoto().equals("")){

        }else{

        }

        holder.textComentario.setText(comentario.getComentario());
        holder.textNome.setText(comentario.getNome());

    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        private TextView textNome, textComentario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textComentario = itemView.findViewById(R.id.textComentario);
            textNome = itemView.findViewById(R.id.textNomeComentario);
        }
    }
}
