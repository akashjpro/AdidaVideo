package com.adida.akashjpro.activity.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.adida.akashjpro.livevideo.R;

/**
 * Created by Aka on 12/25/2016.
 */

public class ShowHideProgessDialog{

    public static ProgressDialog progressDialog;

    public static void showHideProgessDialog(Context context){
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
            }
        }, 3000);
    }

    public static void hideProgessDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
