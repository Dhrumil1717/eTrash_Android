package com.example.dhrumil.test2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends Activity {

    private static final String TAG = "TAG";
    Button bCustomer, bCompany;
    LinearLayout background;
    TextView tvskip;
    FirebaseAuth mAuth;
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bCustomer = findViewById(R.id.bCustomer);
        bCompany = findViewById(R.id.bCompany);
        background = findViewById(R.id.forbackground);
        tvskip = findViewById(R.id.tvSkip);
        mAuth = FirebaseAuth.getInstance();
        mProgressView = findViewById(R.id.login_progresss);


        bCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerLogin.class));
            }
        });

        bCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        tvskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAnonymously();
                showProgress(true);

            }
        });


    }


    private void showProgress(final boolean show) {
        background.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvskip.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void signInAnonymously() {
        mProgressView.setVisibility(View.VISIBLE);
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        showProgress(false);
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        background.setBackgroundColor(Color.WHITE);
        mAuth.signOut();
        LoginManager.getInstance().logOut();

    }

}
