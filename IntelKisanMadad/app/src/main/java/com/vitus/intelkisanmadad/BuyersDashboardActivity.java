package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BuyersDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StatesAdapter statesAdapter;
    private List<State> stateList;
    String userId;
    private ImageView bell_icon, bardIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyers_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        bell_icon = findViewById(R.id.bell_icon);
        bell_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BuyerNotificationActivity.class);
                intent.putExtra("buyerId", userId); // Pass the buyer's user ID
                startActivity(intent);
            }
        });

        userId = getIntent().getStringExtra("userid");

        recyclerView = findViewById(R.id.recycler_view_states);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bardIcon = findViewById(R.id.bard_icon);

        bardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BuyersDashboardActivity.this, TalkToAiActivity.class);
                startActivity(i);
            }
        });

        // Example data
        stateList = new ArrayList<>();
        stateList.add(new State("Arunachal Pradesh", R.drawable.arup));
        stateList.add(new State("Andhra Pradesh", R.drawable.ap));
        stateList.add(new State("Assam", R.drawable.assam));
        stateList.add(new State("Bihar", R.drawable.bihar));
        stateList.add(new State("Chattisgarh", R.drawable.chattisgarh));
        stateList.add(new State("Delhi", R.drawable.delhi));
        stateList.add(new State("Goa", R.drawable.goa));
        stateList.add(new State("Gujrat", R.drawable.gj));
        stateList.add(new State("Haryana", R.drawable.hr));
        stateList.add(new State("Himachal Pradesh", R.drawable.hp));
        stateList.add(new State("Jammu and Kashmir", R.drawable.jk));
        stateList.add(new State("Jharkhand", R.drawable.jharkhand));
        stateList.add(new State("Karnataka", R.drawable.karnataka));
        stateList.add(new State("Kerala", R.drawable.kerala));
        stateList.add(new State("Madhya Pradesh", R.drawable.mp));
        stateList.add(new State("Maharastra", R.drawable.maha));
        stateList.add(new State("Manipur", R.drawable.manipur));
        stateList.add(new State("Meghalaya", R.drawable.meghalaya));
        stateList.add(new State("Mizoram", R.drawable.mizoram));
        stateList.add(new State("Nagaland", R.drawable.naga));
        stateList.add(new State("Odisha", R.drawable.odi));
        stateList.add(new State("Punjab", R.drawable.punjab));
        stateList.add(new State("Rajasthan", R.drawable.rajasthan));
        stateList.add(new State("Skikkim", R.drawable.sikkim));
        stateList.add(new State("Tamil Nadu", R.drawable.tn));
        stateList.add(new State("Telangana", R.drawable.telangana));
        stateList.add(new State("Tripura", R.drawable.tripura));
        stateList.add(new State("Uttar Pradesh", R.drawable.up));
        stateList.add(new State("Uttarakhand", R.drawable.uk));
        stateList.add(new State("West Bengal", R.drawable.wb));
        // Add more states as needed

        statesAdapter = new StatesAdapter(this, stateList, userId);
        recyclerView.setAdapter(statesAdapter);
    }
}
