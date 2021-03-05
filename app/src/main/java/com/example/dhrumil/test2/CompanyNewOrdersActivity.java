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

import com.example.dhrumil.test2.Model.ModelCompany;
import com.example.dhrumil.test2.Prevalent.Prevalent;
import com.example.dhrumil.test2.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompanyNewOrdersActivity extends AppCompatActivity {

    RecyclerView orderList;
    DatabaseReference ordersRef;
    FirebaseAuth mAuth;
    String Price = "", Name = "", Quantity = "", Company = "", CurrentOnlineCompany = "";
    ArrayList<ModelCompany> blogs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("CompanyOrder").child(Prevalent.currentOnlineCompany.getCompanyName());
        orderList = findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ModelCompany> options = new FirebaseRecyclerOptions.Builder<ModelCompany>()
                .setQuery(ordersRef, ModelCompany.class).build();
        Toast.makeText(this, "" + ordersRef.toString(), Toast.LENGTH_SHORT).show();

        FirebaseRecyclerAdapter<ModelCompany, CartViewHolder> adapter = new FirebaseRecyclerAdapter<ModelCompany, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, final int position, @NonNull final ModelCompany model) {

                ordersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        blogs = new ArrayList<ModelCompany>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            ModelCompany modelCompany = dataSnapshot1.getValue(ModelCompany.class);
                            blogs.add(modelCompany);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.getTxtProductPrice.setText("Price:" + model.getPrice());
                holder.getTxtProductName.setText("Name: " + model.getName());
                holder.getTxtProductQuantity.setText("Quantity" + model.getQuantity());
                holder.getTxtCompanyName.setText("" + model.getCompany());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CompanyNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    String uID = getRef(position).getKey();
                                    ordersRef.child(uID).removeValue();
                                }
                                if (which == 1) {
                                    Toast.makeText(CompanyNewOrdersActivity.this, "O.K. ", Toast.LENGTH_SHORT).show();
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
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout, viewGroup, false);
                return new CartViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }


}
