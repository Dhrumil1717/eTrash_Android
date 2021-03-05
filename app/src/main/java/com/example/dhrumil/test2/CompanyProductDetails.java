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
import com.example.dhrumil.test2.Model.TrashProducts;
import com.example.dhrumil.test2.Prevalent.Prevalent;
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

public class CompanyProductDetails extends AppCompatActivity {

    ImageView productImage;
    ElegantNumberButton numberButton;
    TextView productPrice, productDescription, productName;
    String productId = "", state = "Normal", companyname;
    Button addtocartbutton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_product_details);

        productId = getIntent().getStringExtra("pids");
        addtocartbutton = findViewById(R.id.admin_pd_add_to_cart_button);
        productImage = findViewById(R.id.admin_product_image_details);
        numberButton = findViewById(R.id.admin_number);
        productPrice = findViewById(R.id.admin_product_price);
        productName = findViewById(R.id.admin_product_name);
        productDescription = findViewById(R.id.admin_product_description);
        mAuth = FirebaseAuth.getInstance();

        getProductDetails(productId);

        addtocartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
                    Toast.makeText(CompanyProductDetails.this, "You can purchase more products once your order is shipped or confirmed", Toast.LENGTH_LONG).show();
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
    }

    private void CheckOrderState() {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Company Buys").child(Prevalent.currentOnlineCompany.getCompanyName());

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

    private void addingToCartList() {
        String saveCurentDate, saveCurrentTime;
        Calendar callfordate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM,yyyy");
        saveCurentDate = currentDate.format(callfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callfordate.getTime());

        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Company Cart List");
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("username", Prevalent.currentOnlineCompany.getCompanyName());
        cartMap.put("pid", productId);
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("name", productName.getText().toString());
        cartMap.put("date", saveCurentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineCompany.getCompanyName()).child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            cartListRef.child("Admin View").child(Prevalent.currentOnlineCompany.getCompanyName()).child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CompanyProductDetails.this, "Added to Cart Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), CompanyBuy.class));
                                            } else {
                                                Toast.makeText(CompanyProductDetails.this, "Error in admin View", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        } else {
                            Toast.makeText(CompanyProductDetails.this, "Error in Adding Items to cart", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }

    private void getProductDetails(String productId) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("TrashProducts");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TrashProducts products = dataSnapshot.getValue(TrashProducts.class);

                productName.setText(products.getName());
                productPrice.setText(products.getPrice());
                productDescription.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(productImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
