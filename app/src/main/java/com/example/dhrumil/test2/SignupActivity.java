package com.example.dhrumil.test2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    static final int RC_SIGN_IN = 9001;
    private static final String TAG = "EmailPassword";
    TextView tvsignup, tvsignin, tvregisterascompany;
    EditText etpassword, etconfirmpassword;
    CheckBox cbu;
    Button bsignin, bgoogle, bfacebook;
    LoginButton bfblogin;
    FirebaseAuth mAuth;
    FirebaseUser user;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    LinearLayout background;
    private AutoCompleteTextView etemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        background = findViewById(R.id.bg);
        callbackManager = CallbackManager.Factory.create();
        tvsignin = findViewById(R.id.tvsignin);
        tvregisterascompany = findViewById(R.id.RegisterAsCompany);
        bsignin = findViewById(R.id.bsignin);
        bgoogle = findViewById(R.id.bgoogle);
        bfacebook = findViewById(R.id.bfacebook);
        bfblogin = findViewById(R.id.login_button);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        etconfirmpassword = findViewById(R.id.etconfirmpassword);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        cbu = findViewById(R.id.cbu);

        bgoogle.setEnabled(false);
        bsignin.setEnabled(false);
        bfacebook.setEnabled(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("926257620204-9d4lpoc50qlhofngp1gf4ab7vhnqhmut.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        cbu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    bsignin.setEnabled(true);
                    bgoogle.setEnabled(true);
                    bfacebook.setEnabled(true);
                } else {
                    bfacebook.setEnabled(false);
                    bsignin.setEnabled(false);
                    bgoogle.setEnabled(false);
                }
            }
        });

        bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();

            }
        });
        bgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
            }
        });

        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        bfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bfblogin.performClick();
            }
        });

        bfblogin.setReadPermissions("email", "public_profile");
        bfblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(SignupActivity.this, "facebookLogin success", Toast.LENGTH_SHORT).show();
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignupActivity.this, "3", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignupActivity.this, "4" + error, Toast.LENGTH_SHORT).show();

            }
        });

        tvregisterascompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompanySignup.class));

            }
        });

    }

    private void handleFacebookToken(AccessToken accessToken) {
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "2", Toast.LENGTH_SHORT).show();

                            FirebaseUser myuserobj = mAuth.getCurrentUser();
                            updateUI(myuserobj);

                        } else {
                            Toast.makeText(getApplicationContext(), "Could not Register to firebase", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    /////////////////////////////////////Google Sign In/////////////////////////////////////////////
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
                if (account != null) {
                    Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();

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

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        background.setBackgroundResource(R.drawable.background);
    }


    //////////////////////////////////////E-Mail Authentication/////////////////////////////////////
    private void attemptLogin() {

        etemail.setError(null);
        etpassword.setError(null);


        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        String confirmpassword = etconfirmpassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(email)) {
            etemail.setError(getString(R.string.error_field_required));
            focusView = etemail;
            cancel = true;
        }

        // Valid Password
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etpassword.setError(getString(R.string.error_invalid_password));
            focusView = etpassword;
            cancel = true;
        }
        // valid confirm password
        if (!TextUtils.isEmpty(confirmpassword) && !isPasswordValid(confirmpassword)) {
            etconfirmpassword.setError(getString(R.string.error_invalid_password));
            focusView = etconfirmpassword;
            cancel = true;
        }

        // Check for a valid email address.
        else if (!isEmailValid(email)) {
            etemail.setError(getString(R.string.error_invalid_email));
            focusView = etemail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; focus is set on error.
            focusView.requestFocus();
        } else {
            emailLogin();
        }


    }

    private void emailLogin() {

        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        String confirmpassword = etconfirmpassword.getText().toString().trim();
        //Authentication process with email and password


        if (confirmpassword.equals(password)) {
            if (user != null) {
                if (user.isAnonymous()) {
                    final AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                    anonymousSignupProcess(credential);
                }

            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
            }
        } else {
            View focusView = null;

            etconfirmpassword.setError("Passwords do not match");
            focusView = etconfirmpassword;
            focusView.requestFocus();
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
                                                Toast.makeText(SignupActivity.this, "MERGING UNSUCCESSFUL", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            updateUI(null);
                        }

                        // ...
                    }
                });

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
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
