package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CropHealthActivity extends AppCompatActivity {

    Button button_crop_health_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_health);
        button_crop_health_check = findViewById(R.id.button_crop_health_check);
        button_crop_health_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CropHealthActivity.this, CropHealthCheckActivity.class);
                startActivity(i);
            }
        });
    }
}