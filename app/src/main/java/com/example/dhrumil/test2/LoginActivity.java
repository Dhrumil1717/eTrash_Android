package com.example.dhrumil.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dhrumil.test2.Model.Companies;
import com.example.dhrumil.test2.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "TAG";
    LinearLayout background;
    CheckBox cb, cbc;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private AutoCompleteTextView etemail;
    private EditText etpassword, etconfirmpassword, companyname;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvsignin, tvsignup, tvforgotpassword, tvnothaveanaccount, tvskip, tvadminlogin, tvuserlogin;
    private Button bsignin;
    private String parentDbName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        tvsignin = findViewById(R.id.tvsignin);
        tvsignup = findViewById(R.id.tvsignup);
        tvforgotpassword = findViewById(R.id.tvforgotpassword);
        tvnothaveanaccount = findViewById(R.id.nothaveanaccount);
        tvskip = findViewById(R.id.tvskip);
        background = findViewById(R.id.bgs);

        cb = findViewById(R.id.cb);
        cbc = findViewById(R.id.cbc);

        etemail = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
        companyname = findViewById(R.id.companyname);
        etconfirmpassword = findViewById(R.id.etconfirmpassword);

        bsignin = findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        bsignin.setEnabled(false);


        tvskip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                signInAnonymously();

            }
        });

        bsignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etemail.getText().toString().trim();
                String pass = etpassword.getText().toString().trim();
                if (email.equals("admin@gmail.com") && pass.equals("admin123")) {
                    Intent ii = new Intent(getApplicationContext(), AdminPanel.class);
                    ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(ii);
                    finish();

                } else {
                    attemptLogin();

                }
            }
        });

        tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompanySignup.class));
            }
        });

        tvforgotpassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordReset();
            }
        });
        tvnothaveanaccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompanySignup.class));
            }
        });

        String text = "<font color=#000000>I agree to all statements in </font> <font color=#ffffff><u>Terms of service</u></font>";
        cb.setText(Html.fromHtml(text));
        cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Terms of service");
                builder.setCancelable(true);

                builder.setMessage("You must follow this terms in order to use our services.\n" +
                        "\n" +

                        "We may collect personal identification information from Users in a variety of ways, including, but not limited to," +
                        " when Users visit our site, register on the site, place an order, fill out a form, and in connection with other activities, services, features or resources we make available on our Site. Users may be asked for, as appropriate, name, phone number,gst number,email-id. Users may, however, visit our Site anonymously. We will collect personal identification information from Users only if they voluntarily submit such information to us. Users can always refuse to supply personally identification information, except that it may prevent them from engaging in certain Site related activities."


                );

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (buttonView.isChecked()) {
                    bsignin.setEnabled(true);
                }

                if (buttonView.isChecked()) {
                    companyLogin();
                } else if (!buttonView.isChecked()) {
                    attemptLogin();
                } else {
                    bsignin.setEnabled(false);
                    Toast.makeText(LoginActivity.this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });


        /////////////////////////////////////////////////////////Admin Login/////////////////////////////////////////////////////////////////



      /* cbc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
       {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
           {
               companyname.setVisibility(View.VISIBLE);

           }
       });
*/
    }

    private void companyLogin() {
        bsignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                View focusView = null;

                String emails = etemail.getText().toString().trim();
                String pass = etpassword.getText().toString().trim();
                String CompanyNames = companyname.getText().toString();

                if (emails.equals("admin@gmail.com") && pass.equals("admin123") && CompanyNames.equals("admin")) {
                    Intent ii = new Intent(getApplicationContext(), AdminPanel.class);
                    ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(ii);
                    finish();

                }
                String email = etemail.getText().toString();
                String password = etpassword.getText().toString();
                String CompanyName = companyname.getText().toString();
                if (TextUtils.isEmpty(CompanyName)) {
                    companyname.setError(getString(R.string.error_field_required));
                    focusView = companyname;
                    focusView.requestFocus();

                } else {
                    authenticateCompany(email, password, CompanyName);
                }
            }
        });

    }

    private void authenticateCompany(final String email, final String password, final String CompanyName) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Companies").child(CompanyName).exists()) {
                    Companies companiesData = dataSnapshot.child("Companies").child(CompanyName).getValue(Companies.class);

                    if (companiesData.getEmail().equals(email)) {

                        if (companiesData.getPassword().equals(password)) {
                            Toast.makeText(getApplicationContext(), "Company Logged in Succesfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CompanyCategories.class));
                            Prevalent.currentOnlineCompany = companiesData;
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(LoginActivity.this, "Email-id is incorrect", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Company Name is incorrect or you are not registered", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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


    private void sendPasswordReset() {
        View focusView = null;
        boolean cancel = false;

        String email = etemail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etemail.setError(getString(R.string.error_field_required));
            focusView = etemail;
            cancel = true;

        } else if (!isEmailValid(email)) {
            etemail.setError(getString(R.string.error_invalid_email));
            focusView = etemail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Toast.makeText(this, "Password has been set to your E-mail", Toast.LENGTH_LONG).show();
            resetPassword();
        }
    }

    private void resetPassword() {
        String email = etemail.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    private static final String TAG = "reset";

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }


    private void attemptLogin() {

        // finish();
        // startActivity(new Intent(getApplicationContext(), SignupActivity.class));

        // Reset errors.


        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();

        if (email == null || password == null) {
            Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_SHORT).show();
        }

        etemail.setError(null);
        etpassword.setError(null);

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etemail.setError(getString(R.string.error_field_required));
            focusView = etemail;
            cancel = true;

        }

        if (TextUtils.isEmpty(password)) {
            etpassword.setError(getString(R.string.error_field_required));
            focusView = etpassword;
            cancel = true;

        } else if (!isEmailValid(email)) {
            etemail.setError(getString(R.string.error_invalid_email));
            focusView = etemail;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etpassword.setError(getString(R.string.error_invalid_password));
            focusView = etpassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            loginUser();
            showProgress(true);

        }
    }

    private void loginUser() {
        String email = etemail.getText().toString().trim();
        String pass = etpassword.getText().toString().trim();
        String CompanyName = companyname.getText().toString();

        if (email.equals("admin@gmail.com") && pass.equals("admin123") && CompanyName.equals("admin")) {
            Intent ii = new Intent(getApplicationContext(), AdminPanel.class);
            ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ii);
            finish();

        } else {


            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getApplicationContext(), "LOGIN FAILED", Toast.LENGTH_LONG).show();
                        etpassword.setError("Username or password is incorrect");
                        View focusView = null;
                        focusView = etpassword;
                        focusView.requestFocus();
                        showProgress(false);
                    }
                }
            });
        }

    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    private void showProgress(final boolean show) {
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        tvskip.setVisibility(show ? View.GONE : View.VISIBLE);
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
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();

        }

    }
   /* public void onBackPressed(){
        finishAffinity();
    }*/

}

