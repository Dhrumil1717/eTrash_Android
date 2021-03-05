package com.example.dhrumil.test2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dhrumil.test2.Interface.ItemClickListner;
import com.example.dhrumil.test2.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtproductname, txtproductdescription, txtproductprice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
        txtproductdescription = itemView.findViewById(R.id.product_description);
        txtproductname = itemView.findViewById(R.id.product_name);
        txtproductprice = itemView.findViewById(R.id.product_price);
    }

    public void setItemClickListner(ItemClickListner listner) {
        this.listner = listner;
    }


    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
