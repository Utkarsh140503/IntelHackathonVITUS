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
import java.util.List;

public class SellerNotificationActivity extends AppCompatActivity {

    private String userId;
    private RecyclerView recyclerView;
    private NegotiationAdapter adapter;
    private List<Negotiation> negotiationList;
    private TextView sellerNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_notification);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        recyclerView = findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        negotiationList = new ArrayList<>();
        adapter = new NegotiationAdapter(negotiationList, this);
        recyclerView.setAdapter(adapter);

        sellerNameTextView = findViewById(R.id.sellerNameTextView);

        userId = getIntent().getStringExtra("userid");
        if (userId != null) {
            sellerNameTextView.setText(userId);  // Set the userId in the TextView
            loadNegotiations(userId);
        }
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    private void loadNegotiations(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Negotiations");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                negotiationList.clear();
                for (DataSnapshot buyerSnapshot : dataSnapshot.getChildren()) {
                    String buyerUserId = buyerSnapshot.getKey();
                    if (buyerUserId != null && buyerUserId.endsWith(userId)) {
                        String buyerName = buyerUserId.split("@")[0];  // Extract buyer name
                        DatabaseReference buyerPhoneRef = FirebaseDatabase.getInstance().getReference("Buyers").child(buyerName).child("phone");

                        buyerPhoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot phoneSnapshot) {
                                String buyerPhoneNumber = phoneSnapshot.getValue(String.class);

                                for (DataSnapshot commoditySnapshot : buyerSnapshot.getChildren()) {
                                    String commodityName = commoditySnapshot.getKey();
                                    Long askingPrice = commoditySnapshot.child("askingPrice").getValue(Long.class);
                                    Long difference = commoditySnapshot.child("difference").getValue(Long.class);
                                    Long negotiationPrice = commoditySnapshot.child("negotiationPrice").getValue(Long.class);

                                    if (askingPrice != null && difference != null && negotiationPrice != null) {
                                        String askingPriceStr = askingPrice.toString();
                                        String differenceStr = difference.toString();
                                        String negotiationPriceStr = negotiationPrice.toString();

                                        Negotiation negotiation = new Negotiation(buyerName, commodityName, askingPriceStr, differenceStr, negotiationPriceStr, buyerPhoneNumber);
                                        negotiationList.add(negotiation);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.w("SellerNotification", "loadNegotiations:onCancelled", databaseError.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("SellerNotification", "loadNegotiations:onCancelled", databaseError.toException());
            }
        });
    }
}
