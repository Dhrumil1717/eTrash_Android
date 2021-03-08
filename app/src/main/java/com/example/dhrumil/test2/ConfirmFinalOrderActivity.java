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
    String phonenumber,newVerificationId,code;
    FirebaseAuth mAuth;
    Spinner spinner;
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

               // OptCheck();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber,
                                                                60,
                                                                TimeUnit.SECONDS,
                                                                ConfirmFinalOrderActivity.this,
                                                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                    {
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e)
                    {
                         progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConfirmFinalOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConfirmFinalOrderActivity.this, "otp sent", Toast.LENGTH_SHORT).show();
                        newVerificationId = verificationID;
                    }
                });


            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                code = etOTP.getText().toString();
                if (newVerificationId!= null)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(newVerificationId,code);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful())
                            {
                                Toast.makeText(ConfirmFinalOrderActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                Check();
                            }
                            else 
                            {
                                Toast.makeText(ConfirmFinalOrderActivity.this, "The verification code entered is invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

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

            Intent ii = new Intent(getApplicationContext(), PaymentGateway.class);
            ii.putExtra("total", totalAmount);
            ii.putExtra("ph", editTextPhoneNumber.getText().toString());
            ii.putExtra("ad", addressEditText.getText().toString());
            ii.putExtra("ci", cityEditText.getText().toString());
            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(ii);
    }
}
