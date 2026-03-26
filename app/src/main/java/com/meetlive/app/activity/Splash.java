package com.meetlive.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.meetlive.app.R;
import com.meetlive.app.utils.SessionManager;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sessionManager.checkLogin();
                // After 2 seconds the Splashscreen will disappear and user is taken to MainActivity
                /*Intent splashScreenIntent = new Intent(Splash.this, MainActivity.class);
                startActivity(splashScreenIntent);*/
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}