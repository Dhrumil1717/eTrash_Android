package com.example.dhrumil.test2.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.R;

public class CompanyOrdersViewHolder extends RecyclerView.ViewHolder {
    public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
    public Button ShowOrderButton;


    public CompanyOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAddress = itemView.findViewById(R.id.order_address);
        ShowOrderButton = itemView.findViewById(R.id.show_all_products_btn);
    }
}
