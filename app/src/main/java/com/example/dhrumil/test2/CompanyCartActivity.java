package com.example.dhrumil.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Model.CompaniesCart;
import com.example.dhrumil.test2.Prevalent.Prevalent;
import com.example.dhrumil.test2.ViewHolder.CompaniesCartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyCartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button NextprocessButton;
    TextView txtTotalAmount, txtMsg1;
    FirebaseAuth firebaseAuth;
    Integer overallTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_cart);
        recyclerView = findViewById(R.id.admin_cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextprocessButton = findViewById(R.id.admin_next_process_button);
        txtTotalAmount = findViewById(R.id.admin_total_price);
        txtMsg1 = findViewById(R.id.admin_msg);

        NextprocessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer price = overallTotalPrice;
                if (price == 0) {
                    Toast.makeText(CompanyCartActivity.this, "Please add some products in cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent ii = new Intent(getApplicationContext(), CompanyConfirmFinalOrder.class);
                    ii.putExtra("Total Prices", String.valueOf(overallTotalPrice));
                    startActivity(ii);
                    finish();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Company Cart List");
        FirebaseRecyclerOptions<CompaniesCart> options = new FirebaseRecyclerOptions.Builder<CompaniesCart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineCompany.getCompanyName())
                        .child("Products"), CompaniesCart.class).build();

        FirebaseRecyclerAdapter<CompaniesCart, CompaniesCartViewHolder> adapter = new FirebaseRecyclerAdapter<CompaniesCart, CompaniesCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CompaniesCartViewHolder holder, int position, @NonNull final CompaniesCart model) {
                holder.getTxtProductQuantity.setText("Quantity  " + model.getQuantity());
                holder.getTxtProductPrice.setText("Price  " + model.getPrice() + "Rs");
                holder.getTxtProductName.setText("Name  " + model.getName());
                holder.getTxtCompanyName.setText("Company " + model.getUsername());

                int oneTypeProdutPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overallTotalPrice = overallTotalPrice + oneTypeProdutPrice;
                txtTotalAmount.setText("Total Price = " + overallTotalPrice + "Rs");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyCartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent ii = new Intent(getApplicationContext(), CompanyProductDetails.class);
                                    ii.putExtra("pid", model.getPid());
                                    startActivity(ii);

                                }

                                if (which == 1) {
                                    cartListRef.child("User View").child(Prevalent.currentOnlineCompany.getCompanyName())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CompanyCartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), CompanyBuy.class));
                                                    }

                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CompaniesCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.companiescartlayout, viewGroup, false);
                CompaniesCartViewHolder holder = new CompaniesCartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Company Buys").child(Prevalent.currentOnlineCompany.getCompanyName());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("State").getValue().toString();
                    String userName = dataSnapshot.child("Name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        txtTotalAmount.setText("Congratulations!!" + userName + "Your order has been Shipped successully");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your order has been Shipped successfully . Soon you will recieve your order at your doorsteps");

                        NextprocessButton.setVisibility(View.GONE);

                        Toast.makeText(CompanyCartActivity.this, "You can purchase more products,once you recieve your order", Toast.LENGTH_SHORT).show();
                    } else if (shippingState.equals("Not Shipped")) {

                        txtTotalAmount.setText("Shipping status= not shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextprocessButton.setVisibility(View.GONE);

                        Toast.makeText(CompanyCartActivity.this, "You can purchase more products,once you recieve your order", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
