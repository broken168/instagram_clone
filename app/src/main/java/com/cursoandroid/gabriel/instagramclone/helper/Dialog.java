package com.cursoandroid.gabriel.instagramclone.helper;

import android.app.AlertDialog;
import android.content.Context;

public class Dialog {

    public static void dialogError(Context context, String message, String details){

        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("Error!")
                .setMessage("Mensagem: " + message
                        + "\n \n"
                        + "Detalhes: " + details)
                .create().show();
    }
}
