package com.example.dhrumil.test2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Interface.ItemClickListner;
import com.example.dhrumil.test2.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView getTxtProductName, getTxtProductPrice, getTxtProductQuantity, getTxtCompanyName, getTxtUserName;
    public ImageView getProductImage;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        getTxtProductName = itemView.findViewById(R.id.cart_product_name);
        getTxtProductPrice = itemView.findViewById(R.id.cart_product_price);
        getTxtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        getTxtCompanyName = itemView.findViewById(R.id.cart_product_company_name);
        getProductImage = itemView.findViewById(R.id.ivProductImage);

    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}


