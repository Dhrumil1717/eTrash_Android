package com.example.dhrumil.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.dhrumil.test2.Model.Products;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {


    ImageView productImage;
    ElegantNumberButton numberButton;
    TextView productPrice, productDescription, productName, tvcompanyName;
    String productId = "", state = "Normal", companyname;
    Button addtocartbutton;
    FirebaseAuth mAuth;
    String com;
    String imagelink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productId = getIntent().getStringExtra("pid");
        companyname = getIntent().getStringExtra("CompanyName");
        addtocartbutton = findViewById(R.id.pd_add_to_cart_button);
        mAuth = FirebaseAuth.getInstance();
        tvcompanyName = findViewById(R.id.product_company_name);

        productImage = findViewById(R.id.product_image_details);
        numberButton = findViewById(R.id.number);

        productPrice = findViewById(R.id.product_price);
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);

        getProductDetails(productId);

        addtocartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, "You can purchase more products once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
                } else {
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
        productId = getIntent().getStringExtra("pid");

        getProductDetails(productId);
    }

    private void addingToCartList() {
        String saveCurentDate, saveCurrentTime;
        Calendar callfordate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM,yyyy");
        saveCurentDate = currentDate.format(callfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callfordate.getTime());

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("username", currentUser.getDisplayName());
        cartMap.put("companyname", tvcompanyName.getText().toString());
        cartMap.put("pid", productId);
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("name", productName.getText().toString());
        cartMap.put("date", saveCurentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", imagelink);

        cartListRef.child("User View").child(currentUser.getDisplayName()).child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            cartListRef.child("Admin View").child(currentUser.getDisplayName()).child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                            } else {
                                                Toast.makeText(ProductDetailsActivity.this, "Error in admin View", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            cartListRef.child("Company View").child(currentUser.getDisplayName()).child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                            } else {
                                                Toast.makeText(ProductDetailsActivity.this, "Error in Company View", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Error in Adding Items to cart", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }

    private void getProductDetails(final String productId) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getName());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    tvcompanyName.setText(products.getCompanyname());
                    Picasso.get().load(products.getImage()).into(productImage);

                    imagelink = products.getImage();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CheckOrderState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(currentUser.getDisplayName());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("State").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        state = "Order Placed";
                    } else if (shippingState.equals("Not Shipped")) {
                        state = "Order Shipped";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
