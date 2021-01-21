package com.yagocurvello.ifood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yagocurvello.ifood.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

    }
}