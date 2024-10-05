package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BuyerNotificationActivity extends AppCompatActivity {
    private TextView chatInfoTextView;

    private RecyclerView recyclerView;
    private SellerAdapter sellerAdapter;
    private List<Sellers> sellerList;
    private String buyerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_notification);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        buyerId = getIntent().getStringExtra("buyerId"); // Get the current buyer's ID
        recyclerView = findViewById(R.id.recycler_view_sellers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sellerList = new ArrayList<>();
        sellerAdapter = new SellerAdapter(this, sellerList, buyerId);
        recyclerView.setAdapter(sellerAdapter);

        loadSellers();
    }

    private void loadSellers() {
        // Firebase reference to Chats node
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("Chats");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> uniqueSellerCommodityPairs = new HashSet<>(); // To ensure we only add unique (seller, commodity) pairs
                sellerList.clear();

                // Loop through all chats
                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    String chatId = chatSnapshot.getKey(); // e.g. "seller123_buyer456_rice"
                    if (chatId != null && chatId.contains(buyerId)) {
                        // Extract seller ID and commodity from chatId
                        String[] chatParts = chatId.split("_");
                        if (chatParts.length >= 3) {
                            String sellerId = chatParts[0];
                            String commodityName = chatParts[2]; // Get the commodity name from chatId

                            // Create a unique key combining sellerId and commodityName
                            String uniqueKey = sellerId + "_" + commodityName;

                            if (!uniqueSellerCommodityPairs.contains(uniqueKey)) {
                                uniqueSellerCommodityPairs.add(uniqueKey);
                                // Add this seller and commodity to the seller list
                                sellerList.add(new Sellers(sellerId, commodityName));
                            }
                        }
                    }
                }

                sellerAdapter.notifyDataSetChanged(); // Notify the adapter to update the UI
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("BuyerNotification", "loadSellers:onCancelled", databaseError.toException());
            }
        });
    }
}
