package com.example.scheduling_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.scheduling_activity.ui.login.LoginApiActivity;

public class SplashScreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Thread(){

            @Override
            public void run() {
                final SessionManager session = new SessionManager(getApplicationContext());
                if(session.isLoggedIn()){
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(SplashScreenActivity.this, LoginApiActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }
}