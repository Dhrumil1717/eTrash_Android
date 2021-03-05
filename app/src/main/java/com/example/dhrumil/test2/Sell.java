package com.example.dhrumil.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.HashMap;

public class Sell extends AppCompatActivity implements DatePickerListener {

    Button booknow;
    EditText etaddress;
    TextView terms;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String user = "", time = "", date = "";
    Spinner mySpinner;
    Integer i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        HorizontalPicker picker = findViewById(R.id.datePicker);
        picker
                .setListener(this)
                .setDays(20)
                .setOffset(0)
                .setDateSelectedColor(getColor(R.color.colorGrassDark))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getColor(R.color.colorPrimary))
                .setTodayDateTextColor(getColor(R.color.colorPrimary))
                .setTodayDateBackgroundColor(getColor(R.color.colorGrassDark))
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(getColor(R.color.colorGrassDark))
                .showTodayButton(false)
                .init();

        booknow = findViewById(R.id.booknow);
        etaddress = findViewById(R.id.etaddress);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        user = currentUser.getDisplayName();
        terms = findViewById(R.id.terms);

        mySpinner = findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Sell.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Sell.this);
                builder.setTitle("Terms of service");
                builder.setCancelable(true);

                builder.setMessage("You must follow this terms in order to use our services.\n" +
                        "\n" + "For our pickup service, you must have to provide your accurate information like address.\n" +
                        "You pickup will held on the schedule you provide while submitting the request. You can reschedule or cancel your pickup request anytime after submitting your request. \n" +
                        "You must have 5Kg or more scrap for pickup, otherwise pickup charges may apply according to distance. \n" +
                        "Rates of all material will be mentioned in our price list with the valet that will reach you home.For the rates of other materials that are not listed on the list your can contact us. \n" +
                        "We can also reschedule your pickup request if we won't able to do it due to some reasons after informing you.\n"

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

        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (currentUser.isAnonymous()) {
                    Toast.makeText(Sell.this, "Please Sign in", Toast.LENGTH_SHORT).show();
                } else {
                    final DatabaseReference sellRef = FirebaseDatabase.getInstance().getReference().child("Sell").child(user);

                    sellRef.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(Sell.this, "Booking already done", Toast.LENGTH_SHORT).show();
                            } else {

                                checkConditions();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void checkConditions() {
        if (TextUtils.isEmpty(etaddress.getText().toString())) {
            Toast.makeText(this, "Please Provide your Address", Toast.LENGTH_SHORT).show();
        } else if (date == null) {
            Toast.makeText(this, "Please select a date for pickup", Toast.LENGTH_SHORT).show();
        } else {
            time = mySpinner.getSelectedItem().toString();
            SaveAddress();
        }
    }

    private void SaveAddress() {
        DatabaseReference sellRef = FirebaseDatabase.getInstance().getReference().child("Sell").child(user);
        HashMap<String, Object> info = new HashMap<>();
        info.put("name", user);
        info.put("address", etaddress.getText().toString());
        info.put("time", time);
        info.put("date", date);

        sellRef.updateChildren(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Sell.this, "Appointment placed Successfully", Toast.LENGTH_SHORT).show();
                    Intent ii = new Intent(Sell.this, HomeActivity.class);
                    ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(ii);
                    finish();
                }
            }
        });

    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        Log.i("Hi", "Selected date is " + dateSelected.toString());
        date = dateSelected.toString();
    }
}
