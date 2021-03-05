package com.example.dhrumil.test2.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.R;

public class BookNowViewHolder extends RecyclerView.ViewHolder {

    public TextView bookingAdd, bookingDate, bookingTime, bookingName;

    public BookNowViewHolder(@NonNull View itemView) {

        super(itemView);

        bookingName = itemView.findViewById(R.id.booking_name);
        bookingAdd = itemView.findViewById(R.id.booking_address);
        bookingDate = itemView.findViewById(R.id.booking_date);
        bookingTime = itemView.findViewById(R.id.booking_time);

    }
}
