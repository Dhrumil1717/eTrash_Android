package com.example.dhrumil.test2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CompanySignup extends AppCompatActivity {

    EditText companyname, phonenumber, gstno, email, password;
    Button bregister;
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_signup);

        companyname = findViewById(R.id.companyname);
        phonenumber = findViewById(R.id.phonenumber);
        gstno = findViewById(R.id.gstno);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        bregister = findViewById(R.id.bregister);
        cb = findViewById(R.id.cb);
        bregister.setEnabled(false);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    bregister.setEnabled(true);
                } else {
                    bregister.setEnabled(false);

                }
            }
        });


        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companySignup();
            }
        });
    }

    private void companySignup() {
        final String CompanyName = companyname.getText().toString();
        final String PhoneNumber = phonenumber.getText().toString();
        final String GstNo = gstno.getText().toString();
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();


        validateAdmin(CompanyName, PhoneNumber, GstNo, Email, Password);

    }

    private void validateAdmin(final String CompanyName, final String PhoneNumber, final String GstNo, final String Email, final String Password) {

        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Companies").child(CompanyName).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("CompanyName", CompanyName);
                    userdataMap.put("PhoneNumber", PhoneNumber);
                    userdataMap.put("GstNo", GstNo);
                    userdataMap.put("Email", Email);
                    userdataMap.put("Password", Password);


                    RootRef.child("Companies").child(CompanyName).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "account is created successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "This User already exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
