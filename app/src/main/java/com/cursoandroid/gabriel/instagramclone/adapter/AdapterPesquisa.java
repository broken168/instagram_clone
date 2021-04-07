package com.cursoandroid.gabriel.instagramclone.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cursoandroid.gabriel.instagramclone.R;
import com.cursoandroid.gabriel.instagramclone.model.UserProfile;
import com.nostra13.universalimageloader.core.assist.FlushedInputStream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

        /*
        if(user.getProfileImage_path_name() != null) {
            new DownloadImage(holder.foto).execute(user.getProfileImage_path_name());
        }

         */

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

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        private CircleImageView image;

        public DownloadImage(CircleImageView foto) {
            this.image = foto;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL myURL = new URL(strings[0]);
                //URL myURL = new URL("http://i.stack.imgur.com/WxVXe.jpg");
                HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();

                String userCredentials = "gabrielm@gabriel.municipiodigital.com.br:,z[V;Dku*3Jv";
                String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), 0));

                connection.setRequestProperty("Authorization", basicAuth);
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestMethod("GET");
                connection.connect();

                Log.d("result", "doInBackground: " + connection.getResponseCode());
                InputStream is = connection.getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(new FlushedInputStream(is));
                connection.disconnect();
                return bmp;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            image.setImageBitmap(bmp);
        }
    }
}
