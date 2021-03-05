package com.example.dhrumil.test2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CompanyConfirmFinalOrder extends AppCompatActivity {
    EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    Button confirmOrderBtn;
    String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_confirm_final_order);

        confirmOrderBtn = findViewById(R.id.admin_confirm_final_order_btn);
        nameEditText = findViewById(R.id.admin_shipment_name);
        addressEditText = findViewById(R.id.admin_shipment_address);
        phoneEditText = findViewById(R.id.admin_shipment_phonenumber);
        cityEditText = findViewById(R.id.admin_shipment_city);

        totalAmount = getIntent().getStringExtra("Total Prices");

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

    }

    private void Check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Name", Toast.LENGTH_SHORT).show();
        }


        if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Phone Number", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your Address", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(cityEditText.getText().toString())) {
            Toast.makeText(this, "Please Provide your City", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();

        }
    }

    private void ConfirmOrder() {

        paymentgateway();


    }

    private void paymentgateway() {
        Intent ii = new Intent(getApplicationContext(), PaymentGatewayCompany.class);
        ii.putExtra("totala", totalAmount);
        ii.putExtra("pha", phoneEditText.getText().toString());
        ii.putExtra("ada", addressEditText.getText().toString());
        ii.putExtra("cia", cityEditText.getText().toString());
        startActivity(ii);
    }
}
