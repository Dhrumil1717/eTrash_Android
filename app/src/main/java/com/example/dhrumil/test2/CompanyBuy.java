package com.example.dhrumil.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Model.TrashProducts;
import com.example.dhrumil.test2.ViewHolder.TrashProductsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CompanyBuy extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference trashProductRef;
    Button fab1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_buy);

        recyclerView = findViewById(R.id.admin_recycler_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trashProductRef = FirebaseDatabase.getInstance().getReference().child("TrashProducts");
        fab1 = findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompanyCartActivity.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<TrashProducts> options = new FirebaseRecyclerOptions.Builder<TrashProducts>()
                .setQuery(trashProductRef, TrashProducts.class).build();

        FirebaseRecyclerAdapter<TrashProducts, TrashProductsViewHolder> adapter = new FirebaseRecyclerAdapter
                <TrashProducts, TrashProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TrashProductsViewHolder holder, int position, @NonNull final TrashProducts model) {
                holder.txtproductname.setText(model.getName());
                holder.txtproductdescription.setText(model.getDescription());
                holder.txtproductprice.setText("Price = " + model.getPrice() + "RS");
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CompanyBuy.this, CompanyProductDetails.class);
                        intent.putExtra("pids", model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public TrashProductsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trash_product_item_layout, viewGroup, false);
                TrashProductsViewHolder holder = new TrashProductsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
