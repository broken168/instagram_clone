package com.cursoandroid.gabriel.instagramclone.helper.downloaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

    private ImageView image;
    private CircleImageView circleImage;
    private ProgressBar progressBar;

    public ImageDownloader(ImageView image) {
        this.image = image;
    }
    public ImageDownloader(CircleImageView image){
        this.circleImage = image;
    }

    public ImageDownloader(CircleImageView image, ProgressBar progressBarImagePerfil) {
        this.circleImage = image;
        this.progressBar = progressBarImagePerfil;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        try {
            URL myURL = new URL(strings[0]);
            //URL myURL = new URL("http://i.stack.imgur.com/WxVXe.jpg");
            HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();

            String userCredentials = "gabrigov.instagram_clone@gabriel.govmail.com.br:cRfENlDB=REh";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP));

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
        if(image != null) image.setImageBitmap(bmp);
        else circleImage.setImageBitmap(bmp);

        if(progressBar != null) progressBar.setVisibility(View.GONE);

    }

}
