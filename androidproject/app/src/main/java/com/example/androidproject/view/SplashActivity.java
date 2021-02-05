package com.example.androidproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.androidproject.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GoHomeActivity();
//        new CountDownTimer(getResources().getInteger(R.integer.splashActivity_time)+200, 1000){
//            public void onTick(long millisUntilFinished){
//
//            }
//            public  void onFinish(){
//                GoHomeActivity();
//
//            }
//        }.start();
    }

    private void GoHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();
    }


}