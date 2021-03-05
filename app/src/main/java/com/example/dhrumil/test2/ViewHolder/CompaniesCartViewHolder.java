package com.example.dhrumil.test2.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Interface.ItemClickListner;
import com.example.dhrumil.test2.R;

public class CompaniesCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView getTxtProductName, getTxtProductPrice, getTxtProductQuantity, getTxtCompanyName, getTxtUserName;
    private ItemClickListner itemClickListner;

    public CompaniesCartViewHolder(@NonNull View itemView) {
        super(itemView);
        getTxtProductName = itemView.findViewById(R.id.admin_cart_product_name);
        getTxtProductPrice = itemView.findViewById(R.id.admin_cart_product_price);
        getTxtProductQuantity = itemView.findViewById(R.id.admin_cart_product_quantity);
        getTxtCompanyName = itemView.findViewById(R.id.admin_cart_product_company_name);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
