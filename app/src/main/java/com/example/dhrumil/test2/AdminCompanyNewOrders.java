package com.example.dhrumil.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Model.CompanyBuys;
import com.example.dhrumil.test2.ViewHolder.CompanyOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCompanyNewOrders extends AppCompatActivity {
    RecyclerView adminOrderList;
    DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_company_new_orders);


        ordersRef = FirebaseDatabase.getInstance().getReference().child("Company Buys");
        adminOrderList = findViewById(R.id.admin_company_orders_list);
        adminOrderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<CompanyBuys> options = new FirebaseRecyclerOptions.Builder<CompanyBuys>()
                .setQuery(ordersRef, CompanyBuys.class).build();

        FirebaseRecyclerAdapter<CompanyBuys, CompanyOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<CompanyBuys, CompanyOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CompanyOrdersViewHolder holder, final int position, @NonNull CompanyBuys model) {
                holder.userName.setText("Name: " + model.getName());
                holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                holder.userTotalPrice.setText("Total Amount: " + model.getTotalAmount());
                holder.userDateTime.setText("Order at " + model.getDate() + " " + model.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + model.getAddress() + model.getAddress() + "," + model.getCity());

                holder.ShowOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent ii = new Intent(AdminCompanyNewOrders.this, AdminCompanyCart.class);
                        ii.putExtra("uids", uID);
                        startActivity(ii);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminCompanyNewOrders.this);
                        builder.setTitle("Have you shipped this order?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    String uID = getRef(position).getKey();
                                    ordersRef.child(uID).removeValue();

                                } else {
                                    finish();
                                }

                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CompanyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_layout, viewGroup, false);
                return new CompanyOrdersViewHolder(view);
            }
        };
        adminOrderList.setAdapter(adapter);
        adapter.startListening();


    }
}
