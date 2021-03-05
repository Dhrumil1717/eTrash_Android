package com.example.dhrumil.test2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CompanyUserProductActivity extends AppCompatActivity {

    RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference cartListRef, itemRef;
    String userID = "", user = "", Quantity = "", Price = "", Name = "", Company = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_user_product);

        userID = getIntent().getStringExtra("uid");

        productList = findViewById(R.id.products_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);
        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Company View").child(userID).child("Products");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, final int position, @NonNull final Cart model) {
                holder.getTxtProductQuantity.setText("Quantity  " + model.getQuantity());
                holder.getTxtProductPrice.setText("Price  " + model.getPrice() + "Rs");
                holder.getTxtProductName.setText("Name  " + model.getName());
                holder.getTxtCompanyName.setText("Company " + model.getCompanyname());

                user = model.getUsername();
                itemRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Company View").child(user).child("Products");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyUserProductActivity.this);
                        builder.setTitle("Did you notify the company");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    String uID = getRef(position).getKey();
                                    Quantity = model.getQuantity();
                                    Price = model.getPrice();
                                    Name = model.getName();
                                    Company = model.getCompanyname();

                                    DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference().child("CompanyOrder").child(model.getCompanyname()).child(uID);
                                    HashMap<String, Object> company1 = new HashMap<>();
                                    company1.put("quantity", Quantity);
                                    company1.put("price", Price);
                                    company1.put("name", Name);
                                    company1.put("company", Company);
                                    company1.put("uID", uID);

                                    companyRef.updateChildren(company1);
                                    itemRef.child(uID).removeValue();

                                }
                                if (which == 1) {
                                    Toast.makeText(CompanyUserProductActivity.this, "O.K.", Toast.LENGTH_SHORT).show();
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
        productList.setAdapter(adapter);
        adapter.startListening();
    }

//    private void RemoveOrder(String uID)
//    {
//        itemRef.child(uID).removeValue();
//    }
}
