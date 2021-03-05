package com.example.dhrumil.test2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class CustomerLogin extends AppCompatActivity {
    static final int RC_SIGN_IN = 9001;
    private static final String TAG = "EmailPassword";
    Button bgoogle, bfacebook;
    FirebaseAuth mAuth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    LoginButton bfblogin;
    LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        background = findViewById(R.id.bgss);
        bgoogle = findViewById(R.id.bgoogle);
        bfacebook = findViewById(R.id.bfacebooks);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        callbackManager = CallbackManager.Factory.create();
        bfblogin = findViewById(R.id.login_buttons);


        bgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLogin();
            }
        });

        bfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bfblogin.performClick();
            }
        });

        bfblogin.setReadPermissions("email", "public_profile");
        bfblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(CustomerLogin.this, "facebookLogin success", Toast.LENGTH_SHORT).show();
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(CustomerLogin.this, "3", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(CustomerLogin.this, "4" + error, Toast.LENGTH_SHORT).show();
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("926257620204-9d4lpoc50qlhofngp1gf4ab7vhnqhmut.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CustomerLogin.this, "2", Toast.LENGTH_SHORT).show();

                            FirebaseUser myuserobj = mAuth.getCurrentUser();
                            updateUI(myuserobj);

                        } else {
                            Toast.makeText(getApplicationContext(), "Could not Register to firebase", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    /////////////////////////////////////////////////GOOGLE LOGIN////////////////////////////////////////////////////////////////
    private void googleLogin() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null)
                {
                    Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                    firebaseAuthWithGoogle(account);
                }
                else
                {
                    Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [START auth_with_google]

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        if (user != null) {
            if (user.isAnonymous()) {
                anonymousSignupProcess(credential);
            }
        } else {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                updateUI(null);
                            }

                            // [START_EXCLUDE]

                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    private void anonymousSignupProcess(final AuthCredential credential) {

        Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            updateUI(user);
                        } else {

                            Log.w(TAG, "linkWithCredential:failure", task.getException());

                            mAuth.signInWithCredential(credential)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            FirebaseUser currentUser = task.getResult().getUser();
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                            } else {
                                                Toast.makeText(CustomerLogin.this, "MERGING UNSUCCESSFUL", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        background.setBackgroundResource(R.drawable.background);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            if (user.isAnonymous()) {

            } else {
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));

            }
        }
    }

}
