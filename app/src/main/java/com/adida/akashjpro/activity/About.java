package com.adida.akashjpro.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.adida.akashjpro.livevideo.R;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
