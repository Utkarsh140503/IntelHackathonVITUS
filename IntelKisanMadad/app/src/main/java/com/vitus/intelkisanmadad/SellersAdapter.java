package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellersAdapter extends RecyclerView.Adapter<SellersAdapter.SellerViewHolder> {

    private final List<Seller> sellerList;
    private final String userId;
    private final String stateName;

    public SellersAdapter(List<Seller> sellerList, String userId, String stateName) {
        this.sellerList = sellerList;
        this.userId = userId;
        this.stateName = stateName;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_seller, parent, false);
        return new SellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
        Seller seller = sellerList.get(position);
        holder.sellerName.setText(seller.getName());
        holder.sellerImage.setImageResource(seller.getImageResource()); // Set image from resource or URL

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), CropsFromSellerActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("sellerName", seller.getName());
            intent.putExtra("stateName", stateName);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return sellerList.size();
    }

    static class SellerViewHolder extends RecyclerView.ViewHolder {
        CircleImageView sellerImage;
        TextView sellerName;

        SellerViewHolder(View itemView) {
            super(itemView);
            sellerImage = itemView.findViewById(R.id.seller_image);
            sellerName = itemView.findViewById(R.id.seller_name);
        }
    }
}

class Seller {

    private String name;
    private int imageResource; // You can use a resource ID or URL for images

    public Seller() {
        // Default constructor required for Firebase
    }

    public Seller(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
