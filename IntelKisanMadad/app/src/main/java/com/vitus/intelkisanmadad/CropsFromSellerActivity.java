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

public class CropsFromSellerActivity extends AppCompatActivity {

    private TextView sellerNameTextView;
    private RecyclerView recyclerView;
    private CropsAdapter cropsAdapter;
    private List<Crop> cropList;
    private DatabaseReference databaseReference;
    private String userId;
    private String sellerName;
    private String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crops_from_seller);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        sellerNameTextView = findViewById(R.id.sellerNameTextView);
        recyclerView = findViewById(R.id.recycler_view_crops);

        // Retrieve data from the intent
        userId = getIntent().getStringExtra("userId");
        sellerName = getIntent().getStringExtra("sellerName");
        stateName = getIntent().getStringExtra("stateName");

        if (sellerName != null) {
            sellerNameTextView.setText("Seller: " + sellerName);
        }

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("MarketPlace")
                .child("Commodity").child(stateName).child(sellerName);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cropList = new ArrayList<>();
        cropsAdapter = new CropsAdapter(cropList, userId, sellerName);
        recyclerView.setAdapter(cropsAdapter);

        // Fetch crop data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cropList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String commodityName = snapshot.getKey();
                    double askingPrice = snapshot.child("askingPrice").getValue(Double.class);
                    double meanPrice = snapshot.child("meanPrice").getValue(Double.class);

                    cropList.add(new Crop(commodityName, askingPrice, meanPrice));
                }
                cropsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CropsFromSellerActivity.this, "Failed to load crops", Toast.LENGTH_SHORT).show();
                Log.e("CropsFromSellerActivity", "Error: " + databaseError.getMessage());
            }
        });
    }
}

class Crop {
    private String name;
    private double askingPrice;
    private double meanPrice;

    public Crop() {
        // Default constructor required for Firebase
    }

    public Crop(String name, double askingPrice, double meanPrice) {
        this.name = name;
        this.askingPrice = askingPrice;
        this.meanPrice = meanPrice;
    }

    public String getName() {
        return name;
    }

    public double getAskingPrice() {
        return askingPrice;
    }

    public double getMeanPrice() {
        return meanPrice;
    }
}
