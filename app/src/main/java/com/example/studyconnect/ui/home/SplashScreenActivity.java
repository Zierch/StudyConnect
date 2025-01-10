package com.example.studyconnect.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studyconnect.R;
import com.example.studyconnect.ui.auth.SignInActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intentToSignInActivity = new Intent(
                    this, SignInActivity.class
            );
            startActivity(intentToSignInActivity);
            finish();
        }, 2500);
    }
}
