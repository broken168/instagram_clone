package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.helper.downloaders.ImageDownloaderGlide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterGrid extends ArrayAdapter<String> {

    private Context context;
    private int layoutResource;
    private List<String> urlFotos;
    private DisplayImageOptions displayImageOptions;

    public AdapterGrid(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.urlFotos = objects;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", ImageDownloaderGlide.BASIC_AUTH);

        displayImageOptions = new DisplayImageOptions.Builder()
                .extraForDownloader(headers)
                .build();
    }

    public class ViewHolder{
        ImageView imagem;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

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
        ImageDownloaderGlide.downloadImage(urlImagem, context, viewHolder.progressBar, viewHolder.imagem);

        return convertView;
    }
}
