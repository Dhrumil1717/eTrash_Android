package com.example.dhrumil.test2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Model.BookNow;
import com.example.dhrumil.test2.ViewHolder.BookNowViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingInfo extends AppCompatActivity {
    RecyclerView bookingList;
    DatabaseReference bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);

        bookingList = findViewById(R.id.booking_list);
        bookingList.setLayoutManager(new LinearLayoutManager(this));
        bookingRef = FirebaseDatabase.getInstance().getReference().child("Sell");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<BookNow> options = new FirebaseRecyclerOptions.Builder<BookNow>()
                .setQuery(bookingRef, BookNow.class).build();

        FirebaseRecyclerAdapter<BookNow, BookNowViewHolder> adapter = new FirebaseRecyclerAdapter<BookNow, BookNowViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookNowViewHolder holder, final int position, @NonNull BookNow model) {
                holder.bookingAdd.setText("Address   " + model.getAddress());
                holder.bookingDate.setText("Date      " + model.getDate());
                holder.bookingTime.setText("Time Slot " + model.getTime());
                holder.bookingName.setText("Name      " + model.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "Not yet"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(BookingInfo.this);
                        builder.setTitle("Have you attend the customer");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    String uID = getRef(position).getKey();
                                    bookingRef.child(uID).removeValue();
                                }

                                if (which == 1) {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public BookNowViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.booking_item_layout, viewGroup, false);
                return new BookNowViewHolder(view);
            }
        };
        bookingList.setAdapter(adapter);
        adapter.startListening();
    }


}
