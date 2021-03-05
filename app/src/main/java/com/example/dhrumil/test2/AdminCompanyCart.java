package com.example.dhrumil.test2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Model.CompaniesCart;
import com.example.dhrumil.test2.ViewHolder.CompaniesCartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminCompanyCart extends AppCompatActivity {
    RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference cartListRef, itemRef;
    String userID = "", user = "", Quantity = "", Price = "", Name = "", Company = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_company_cart);
        userID = getIntent().getStringExtra("uids");
        productList = findViewById(R.id.company_products_list);
        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Company Cart List").child("Admin View").child(userID).child("Products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<CompaniesCart> options = new FirebaseRecyclerOptions.Builder<CompaniesCart>()
                .setQuery(cartListRef, CompaniesCart.class)
                .build();

        FirebaseRecyclerAdapter<CompaniesCart, CompaniesCartViewHolder> adapter = new FirebaseRecyclerAdapter<CompaniesCart, CompaniesCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CompaniesCartViewHolder holder, int position, @NonNull CompaniesCart model) {
                holder.getTxtProductQuantity.setText("Quantity  " + model.getQuantity());
                holder.getTxtProductPrice.setText("Price  " + model.getPrice() + "Rs");
                holder.getTxtProductName.setText("Name  " + model.getName());
                holder.getTxtCompanyName.setText("Company " + model.getUsername());
            }

            @NonNull
            @Override
            public CompaniesCartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.companiescartlayout, viewGroup, false);
                CompaniesCartViewHolder holder = new CompaniesCartViewHolder(view);
                return holder;
            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();

    }
}
