package com.example.dhrumil.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent ii = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(ii);
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent ii = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(ii);
                }
            }, SPLASH_DISPLAY_LENGTH);

        }
    }
}
