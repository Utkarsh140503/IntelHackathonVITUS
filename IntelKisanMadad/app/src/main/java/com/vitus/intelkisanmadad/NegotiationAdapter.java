package com.vitus.intelkisanmadad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NegotiationAdapter extends RecyclerView.Adapter<NegotiationAdapter.ViewHolder> {
    private List<Negotiation> negotiationList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView buyerNameTextView;
        public TextView commodityNameTextView;
        public TextView askingPriceTextView;
        public TextView differenceTextView;
        public TextView negotiationPriceTextView;
        public TextView buyerPhoneNumberTextView;  // New TextView

        public ViewHolder(View itemView) {
            super(itemView);
            buyerNameTextView = itemView.findViewById(R.id.buyerNameTextView);
            commodityNameTextView = itemView.findViewById(R.id.commodityNameTextView);
            askingPriceTextView = itemView.findViewById(R.id.askingPriceTextView);
            differenceTextView = itemView.findViewById(R.id.differenceTextView);
            negotiationPriceTextView = itemView.findViewById(R.id.negotiationPriceTextView);
            buyerPhoneNumberTextView = itemView.findViewById(R.id.buyerPhoneNumberTextView);  // Initialize new TextView
        }
    }

    public NegotiationAdapter(List<Negotiation> negotiationList, Context context) {
        this.negotiationList = negotiationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sellernotificationcardview, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Negotiation negotiation = negotiationList.get(position);
        holder.buyerNameTextView.setText("Negotiation from: " + negotiation.getBuyerName());
        holder.commodityNameTextView.setText(negotiation.getCommodityName());
        holder.askingPriceTextView.setText("Asking Price: " + negotiation.getAskingPrice());
        holder.differenceTextView.setText("Difference: " + negotiation.getDifference());
        holder.negotiationPriceTextView.setText("Negotiation Price: " + negotiation.getNegotiationPrice());
        holder.buyerPhoneNumberTextView.setText("Phone: " + negotiation.getBuyerPhoneNumber());  // Set phone number

        // Add click listener to redirect to TalkToBuyerActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TalkToBuyerActivity.class);
            intent.putExtra("buyerName", negotiation.getBuyerName());
            intent.putExtra("commodityName", negotiation.getCommodityName());
            intent.putExtra("sellerId", ((SellerNotificationActivity) context).getUserId()); // Pass sellerId (userId)
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return negotiationList.size();
    }
}


class Negotiation {
    private String buyerName;
    private String commodityName;
    private String askingPrice;
    private String difference;
    private String negotiationPrice;
    private String buyerPhoneNumber;  // New field

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Negotiation() {}

    public Negotiation(String buyerName, String commodityName, String askingPrice, String difference, String negotiationPrice, String buyerPhoneNumber) {
        this.buyerName = buyerName;
        this.commodityName = commodityName;
        this.askingPrice = askingPrice;
        this.difference = difference;
        this.negotiationPrice = negotiationPrice;
        this.buyerPhoneNumber = buyerPhoneNumber;
    }

    public String getBuyerName() { return buyerName; }
    public String getCommodityName() { return commodityName; }
    public String getAskingPrice() { return askingPrice; }
    public String getDifference() { return difference; }
    public String getNegotiationPrice() { return negotiationPrice; }
    public String getBuyerPhoneNumber() { return buyerPhoneNumber; }  // New getter
}
