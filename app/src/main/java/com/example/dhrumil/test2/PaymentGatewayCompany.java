package com.example.dhrumil.test2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dhrumil.test2.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class PaymentGatewayCompany extends AppCompatActivity {
    final int UPI_PAYMENT = 0;
    EditText noteEt, nameEt, upiIdEt;
    TextView amountEt;
    Button send;
    FirebaseAuth mAuth;
    String totalAmount = "", address = "", city = "", phone = "";

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway_company);

        totalAmount = getIntent().getStringExtra("totala");
        address = getIntent().getStringExtra("ada");
        phone = getIntent().getStringExtra("pha");
        city = getIntent().getStringExtra("cia");


        initializeViews();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the values from the EditTexts
                String amount = totalAmount;
                String note = noteEt.getText().toString();
                String name = nameEt.getText().toString();
                String upiId = upiIdEt.getText().toString();
                mAuth = FirebaseAuth.getInstance();
                payUsingUpi(amount, upiId, name, note);
            }
        });

    }

    private void payUsingUpi(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentGatewayCompany.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentGatewayCompany.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String[] response = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String[] equalStr = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentGatewayCompany.this, "Transaction successful.", Toast.LENGTH_SHORT).show();


                removeValueFromCart();
                confirmOrder();
                Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentGatewayCompany.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PaymentGatewayCompany.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(PaymentGatewayCompany.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmOrder() {
        Calendar callForDate = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        final String saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        final String saveCurrentTime = currentTime.format(callForDate.getTime());


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Company Buys")
                .child(Prevalent.currentOnlineCompany.getCompanyName());

        HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("TotalAmount", totalAmount);
        orderMap.put("Name", Prevalent.currentOnlineCompany.getCompanyName());
        orderMap.put("Phone", phone);
        orderMap.put("Address", address);
        orderMap.put("City", city);
        orderMap.put("Date", saveCurrentDate);
        orderMap.put("Time", saveCurrentTime);
        orderMap.put("State", "Not Shipped");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())                                         //REMOVING VALUE FROM CART
                {
                    FirebaseDatabase.getInstance().getReference().child("Company Cart List")
                            .child("User View").
                            child(Prevalent.currentOnlineCompany.getCompanyName())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PaymentGatewayCompany.this, "Your final order has been placed succesfully", Toast.LENGTH_LONG).show();
                                        Intent ii = new Intent(getApplicationContext(), CompanyCategories.class);
                                        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(ii);
                                        finish();

                                    }
                                }
                            });
                }
            }
        });

    }

    private void removeValueFromCart() {

    }

    private void initializeViews() {

        send = findViewById(R.id.company_send);
        amountEt = findViewById(R.id.company_amount_et);
        noteEt = findViewById(R.id.company_note);
        nameEt = findViewById(R.id.company_name);
        upiIdEt = findViewById(R.id.company_upi_id);

        amountEt.setText("Total Payable amount is " + totalAmount + " Rs");
    }
}