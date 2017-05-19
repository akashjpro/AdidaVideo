package com.adida.akashjpro.activity.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.adida.akashjpro.activity.MainActivity;
import com.adida.akashjpro.livevideo.R;

public class NoInternet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        Intent intent = new Intent(NoInternet.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
