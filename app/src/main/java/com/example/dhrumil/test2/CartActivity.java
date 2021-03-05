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

import com.example.dhrumil.test2.Model.Cart;
import com.example.dhrumil.test2.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button NextprocessButton;
    TextView txtTotalAmount, txtMsg1;
    FirebaseAuth firebaseAuth;
    Integer overallTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextprocessButton = findViewById(R.id.next_process_button);
        txtTotalAmount = findViewById(R.id.total_price);
        txtMsg1 = findViewById(R.id.msg1);

        firebaseAuth = FirebaseAuth.getInstance();

        NextprocessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer price = overallTotalPrice;
                if (price == 0) {
                    Toast.makeText(CartActivity.this, "Please add some products in cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent ii = new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);
                    ii.putExtra("Total Price", String.valueOf(overallTotalPrice));
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

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {

            FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                    .setQuery(cartListRef.child("User View")
                            .child(currentUser.getDisplayName())
                            .child("Products"), Cart.class)
                    .build();

            FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                    holder.getTxtProductQuantity.setText("Qty                 " + model.getQuantity());
                    holder.getTxtProductPrice.setText("Price             " + model.getPrice() + "Rs");
                    holder.getTxtProductName.setText("Name      " + model.getName());
                    holder.getTxtCompanyName.setText("Company      " + model.getCompanyname());

                    Picasso.get().load(model.getDiscount()).into(holder.getProductImage);

                    int oneTypeProdutPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                    overallTotalPrice = overallTotalPrice + oneTypeProdutPrice;
                    NextprocessButton.setText("Proceed to Pay ₹ " + overallTotalPrice);

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CharSequence[] options = new CharSequence[]
                                    {
                                            "Edit",
                                            "Remove"
                                    };

                            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                            builder.setTitle("Cart Options");

                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        Toast.makeText(CartActivity.this, "" + model.getCompanyname(), Toast.LENGTH_SHORT).show();
                                        Intent ii = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                        ii.putExtra("pid", model.getPid());
                                        ii.putExtra("company", model.getCompanyname());
                                        startActivity(ii);

                                    }

                                    if (which == 1) {
                                        cartListRef.child("User View").child(currentUser.getDisplayName())
                                                .child("Products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
                public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newcartlayout, viewGroup, false);
                    CartViewHolder holder = new CartViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }

    private void CheckOrderState() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentUser.getDisplayName());

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

                        Toast.makeText(CartActivity.this, "You can purchase more products,once you recieve your order", Toast.LENGTH_SHORT).show();
                    } else if (shippingState.equals("Not Shipped")) {

                        txtTotalAmount.setText("Shipping status= not shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextprocessButton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more products,once you recieve your order", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
