package com.example.dhrumil.test2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AdminPanel extends Activity {
    ImageView booking, adminNewOrders, addnewproducts, adminCompanyNewOrders;
    LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        booking = findViewById(R.id.booking);
        adminNewOrders = findViewById(R.id.adminneworders);
        addnewproducts = findViewById(R.id.addnewproducts);
        adminCompanyNewOrders = findViewById(R.id.admincompanyneworders);
        logout = findViewById(R.id.logoutadmin);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BookingInfo.class));
            }
        });
        adminNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanel.this, AdminNewOrders.class));
            }
        });
        addnewproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminAddNewItem.class));
            }
        });
        adminCompanyNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminCompanyNewOrders.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent ii = new Intent(getApplicationContext(), LoginActivity.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(ii);
                finish();
            }
        });
    }
}
