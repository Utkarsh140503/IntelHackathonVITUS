package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class StateSellersActivity extends AppCompatActivity {

    private TextView stateNameTextView;
    private CircleImageView stateImageView;
    private RecyclerView recyclerView;
    private SellersAdapter sellersAdapter;
    private List<Seller> sellerList;
    private DatabaseReference databaseReference;
    private String stateName;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_sellers);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        stateNameTextView = findViewById(R.id.stateBanner);
        stateImageView = findViewById(R.id.stateImage);
        recyclerView = findViewById(R.id.recycler_view_sellers);

        // Retrieve data from the intent
        stateName = getIntent().getStringExtra("stateName");
        int stateDrawable = getIntent().getIntExtra("stateDrawable", -1);
        userId = getIntent().getStringExtra("userId");

        if (stateName != null) {
            stateNameTextView.setText("Sellers in " + stateName);
        }

        if (stateDrawable != -1) {
            stateImageView.setImageResource(stateDrawable);
        }

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("MarketPlace").child("Commodity").child(stateName);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellerList = new ArrayList<>();
        sellersAdapter = new SellersAdapter(sellerList, userId, stateName);
        recyclerView.setAdapter(sellersAdapter);

        // Fetch seller data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellerList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String sellerName = snapshot.getKey();
                    // You might want to fetch additional details if available
                    sellerList.add(new Seller(sellerName, R.drawable.farmericon)); // Use a default image or fetch from Firebase
                }
                sellersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StateSellersActivity.this, "Failed to load sellers", Toast.LENGTH_SHORT).show();
                Log.e("StateSellersActivity", "Error: " + databaseError.getMessage());
            }
        });
    }
}
