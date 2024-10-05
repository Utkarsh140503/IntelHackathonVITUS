package com.vitus.intelkisanmadad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.ViewHolder> {

    private Context context;
    private List<Sellers> sellerList;
    private String buyerId;

    public SellerAdapter(Context context, List<Sellers> sellerList, String buyerId) {
        this.context = context;
        this.sellerList = sellerList;
        this.buyerId = buyerId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sellers seller = sellerList.get(position);
        holder.sellerNameTextView.setText("Seller ID: " + seller.getSellerId());
        holder.commodityTextView.setText("Commodity: " + seller.getCommodityName());

        // When a seller item is clicked, navigate to the chat activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BuyerChatActivity.class);
            intent.putExtra("sellerId", seller.getSellerId());
            intent.putExtra("buyerId", buyerId);
            intent.putExtra("commodityName", seller.getCommodityName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sellerNameTextView;
        TextView commodityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerNameTextView = itemView.findViewById(R.id.seller_name);
            commodityTextView = itemView.findViewById(R.id.commodity_name);
        }
    }
}

class Sellers {
    private String sellerId;
    private String commodityName;

    public Sellers(String sellerId, String commodityName) {
        this.sellerId = sellerId;
        this.commodityName = commodityName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getCommodityName() {
        return commodityName;
    }
}