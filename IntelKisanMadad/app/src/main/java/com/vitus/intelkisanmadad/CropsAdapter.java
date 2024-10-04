package com.vitus.intelkisanmadad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CropsAdapter extends RecyclerView.Adapter<CropsAdapter.CropViewHolder> {

    private final List<Crop> cropList;
    private final String userId;
    private final String sellerName;

    public CropsAdapter(List<Crop> cropList, String userId, String sellerName) {
        this.cropList = cropList;
        this.userId = userId;
        this.sellerName = sellerName;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_crop, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = cropList.get(position);
        holder.commodityNameTextView.setText("Commodity: " + crop.getName());
        holder.askingPriceTextView.setText("Asking Price: " + crop.getAskingPrice());
        holder.meanPriceTextView.setText("Mean Price: " + crop.getMeanPrice());

        holder.saveButton.setOnClickListener(v -> {
            String negotiationPriceStr = holder.negotiationPriceEditText.getText().toString();
            if (!negotiationPriceStr.isEmpty()) {
                double negotiationPrice = Double.parseDouble(negotiationPriceStr);
                double askingPrice = crop.getAskingPrice();
                double difference = askingPrice - negotiationPrice;

                // Save negotiation details in Firebase
                DatabaseReference negotiationsRef = FirebaseDatabase.getInstance().getReference("Negotiations")
                        .child(userId + "@" + sellerName).child(crop.getName());

                negotiationsRef.child("negotiationPrice").setValue(negotiationPrice);
                negotiationsRef.child("askingPrice").setValue(askingPrice);
                negotiationsRef.child("difference").setValue(difference);

                // Provide user feedback
                Toast.makeText(holder.itemView.getContext(), "Negotiation saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Please enter a negotiation price.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    static class CropViewHolder extends RecyclerView.ViewHolder {
        TextView commodityNameTextView;
        TextView askingPriceTextView;
        TextView meanPriceTextView;
        EditText negotiationPriceEditText;
        Button saveButton;

        CropViewHolder(View itemView) {
            super(itemView);
            commodityNameTextView = itemView.findViewById(R.id.commodityNameTextView);
            askingPriceTextView = itemView.findViewById(R.id.askingPriceTextView);
            meanPriceTextView = itemView.findViewById(R.id.meanPriceTextView);
            negotiationPriceEditText = itemView.findViewById(R.id.negotiationPriceEditText);
            saveButton = itemView.findViewById(R.id.saveButton);
        }
    }
}
