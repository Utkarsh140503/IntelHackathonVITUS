package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class FarmersDashboardActivity extends AppCompatActivity {

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_dashboard);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#CBE49B"));
        }

        userId = getIntent().getStringExtra("userid");

        // Initialize the layout elements
        LinearLayout marketPlaceLayout = findViewById(R.id.marketPlaceLayout);
        LinearLayout smartIrrigationLayout = findViewById(R.id.smartIrrigationLayout);
        LinearLayout smartNutritionLayout = findViewById(R.id.smartNutritionLayout);
        LinearLayout cropHealthLayout = findViewById(R.id.cropHealthLayout);
        LinearLayout smartCropRecommender = findViewById(R.id.smartCropRecommender);
        LinearLayout educationResourcesLayout = findViewById(R.id.educationResourcesLayout);
        ImageView bellIcon = findViewById(R.id.bell_icon);
        ImageView bardIcon = findViewById(R.id.bard_icon);

        bardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FarmersDashboardActivity.this, TalkToAiActivity.class);
                startActivity(i);
            }
        });

        smartCropRecommender.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, CropRecommendationActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });

        // Set click listeners
        marketPlaceLayout.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, MarketplaceActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });

        smartNutritionLayout.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, SmartNutritionActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });

        smartIrrigationLayout.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, SmartIrrigationActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });

        cropHealthLayout.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, CropHealthActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });

        educationResourcesLayout.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, EducationResourcesActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });

        bellIcon.setOnClickListener(view -> {
            Intent intent = new Intent(FarmersDashboardActivity.this, SellerNotificationActivity.class);
            intent.putExtra("userid", userId);
            startActivity(intent);
        });
    }
}
