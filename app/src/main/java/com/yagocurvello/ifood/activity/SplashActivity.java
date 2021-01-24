package com.yagocurvello.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yagocurvello.ifood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAutenticacao();
            }
        }, 3000);

    }

    private void abrirAutenticacao(){
        startActivity(new Intent(SplashActivity.this, AuthActivity.class));
        finish();
    }
}