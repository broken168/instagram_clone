package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.FeedCounter;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderPicasso;

import java.util.List;


public class AdapterGrid extends ArrayAdapter<String> {

    private Context context;
    private int layoutResource;
    private List<String> urlFotos;
    private FeedCounter feedCounter;

    public AdapterGrid(@NonNull Context context, int resource, @NonNull List<String> objects, FeedCounter feedCounter) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.urlFotos = objects;
        this.feedCounter = feedCounter;
    }

    public class ViewHolder{
        ImageView imagem;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        feedCounter.count(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder.progressBar = convertView.findViewById(R.id.progressBarGridPerfil);
            viewHolder.imagem = convertView.findViewById(R.id.imageGridPerfil);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        String urlImagem = getItem(position);
        ImageDownloaderGlide.loadImage(urlImagem, context, viewHolder.progressBar, viewHolder.imagem);

        return convertView;
    }
}
