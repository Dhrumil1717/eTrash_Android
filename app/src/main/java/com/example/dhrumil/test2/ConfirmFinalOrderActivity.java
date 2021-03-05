package com.example.dhrumil.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.android.gms.tasks.TaskExecutors.MAIN_THREAD;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    public static final String CHANNEL_1_ID = "channel1";
    EditText nameEditText, addressEditText, cityEditText, editTextPhoneNumber;
    Button confirmOrderBtn, sendOTP;
    String totalAmount = "";
    FirebaseAuth mAuth;
    Spinner spinner;
    String phonenumber, codes;
    Integer b = 1;
    NotificationManagerCompat notificationManager;
    PhoneAuthCredential credential;

     String mVerificationId;
     PhoneAuthProvider.ForceResendingToken mResendToken;
     ProgressBar progressBar;
     EditText etOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);
        etOTP = findViewById(R.id.editTextCode);
        sendOTP = findViewById(R.id.resend);
        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);
        nameEditText = findViewById(R.id.shipment_name);
        addressEditText = findViewById(R.id.shipment_address);
        cityEditText = findViewById(R.id.shipment_city);
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
        editTextPhoneNumber = findViewById(R.id.editTextPhone);
        mAuth = FirebaseAuth.getInstance();

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total amount" + totalAmount, Toast.LENGTH_SHORT).show();

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = editTextPhoneNumber.getText().toString().trim();
                phonenumber = "+" + code + number;
                progressBar.setVisibility(View.VISIBLE);
                OptCheck();

            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Check();
            }
        });
    }

    private void OptCheck()
    {
        sendVerificationCode(phonenumber);
    }

    private void sendVerificationCode(String phonenumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phonenumber)       // Phone number to verify
                        .setTimeout(6L, TimeUnit.SECONDS)
                        .setActivity(this)// Timeout and unit
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        progressBar.setVisibility(View.GONE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential)
        {
            String code = credential.getSmsCode();
            if (code != null)
            {
                codes=code;
                Toast.makeText(ConfirmFinalOrderActivity.this, "Verification complete", Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.VISIBLE);
               // verifyCode(code);
            }
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.


            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }
    };

    private void verifyCode(String code)
    {
        codes = code;
       // credential = PhoneAuthProvider.getCredential(verificationid, code);
      //  signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        Integer a = 1;

        if (a == 1) {
            Toast.makeText(this, "Otp received", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else {

            Toast.makeText(ConfirmFinalOrderActivity.this, "Error occured", Toast.LENGTH_LONG).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Firebase verification Successful", Toast.LENGTH_SHORT).show();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


//    mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
//    {
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
//        {
//            Toast.makeText(ConfirmFinalOrderActivity.this, "in Callback 1", Toast.LENGTH_SHORT).show();
//           // super.onCodeSent(s, forceResendingToken);
//            verificationid = s;
//            mResendToken = forceResendingToken;
//
//        }
//
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
//        {
//            Toast.makeText(ConfirmFinalOrderActivity.this, "in Callback 2", Toast.LENGTH_SHORT).show();
//            String code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                Toast.makeText(ConfirmFinalOrderActivity.this, "in Callback 3", Toast.LENGTH_SHORT).show();
//
//                progressBar.setVisibility(View.VISIBLE);
//                verifyCode(code);
//            }
//            else
//            {
//                Toast.makeText(ConfirmFinalOrderActivity.this, "in Callback 4", Toast.LENGTH_SHORT).show();
//
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(ConfirmFinalOrderActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    };
    private void Check() {
        View focusView = null;

        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            focusView.requestFocus();
            Toast.makeText(this, "Please Provide your Name", Toast.LENGTH_SHORT).show();
        }


        if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            addressEditText.setError(getString(R.string.error_field_required));
            focusView = addressEditText;
            focusView.requestFocus();
            Toast.makeText(this, "Please Provide your Address", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(cityEditText.getText().toString())) {
            cityEditText.setError(getString(R.string.error_field_required));
            focusView = cityEditText;
            focusView.requestFocus();
            Toast.makeText(this, "Please Provide your City", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(editTextPhoneNumber.getText().toString())) {
            editTextPhoneNumber.setError("Valid number is required");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etOTP.getText().toString())) {

            etOTP.setError("Otp Not Received yet");
            etOTP.requestFocus();
            return;
        }

        else
        {
            paymentgateway();
        }
    }




    private void paymentgateway() {
        if (etOTP.getText().toString() == codes) {
            Intent ii = new Intent(getApplicationContext(), PaymentGateway.class);
            ii.putExtra("total", totalAmount);
            ii.putExtra("ph", editTextPhoneNumber.getText().toString());
            ii.putExtra("ad", addressEditText.getText().toString());
            ii.putExtra("ci", cityEditText.getText().toString());
            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ii);
        }
        else
        {
            Toast.makeText(this, "code is"+ codes, Toast.LENGTH_LONG).show();
            Toast.makeText(this, "U have entered incorrect OTP" + codes + "  " + etOTP.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
