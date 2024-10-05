package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView farmerOption, othersOption;
    private ImageView farmerTick, othersTick;
    private TextView nextButton;
    private String selectedOption = "";
    private AlertDialog dialog; // Declare dialog as a member variable
    private ImageView infoIcon; // Declare bardIcon for the info icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Optional: Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FCF9C1"));
        }

        // Initialize Views
        farmerOption = findViewById(R.id.farmerCard);
        othersOption = findViewById(R.id.othersCard);
        farmerTick = findViewById(R.id.farmerTick);
        othersTick = findViewById(R.id.othersTick);
        nextButton = findViewById(R.id.nextButton);
        infoIcon = findViewById(R.id.infoIcon); // Initialize the bard icon

        // Check if the device is in dark mode, if so, show the custom dialog
        if (isDarkModeEnabled()) {
            showCustomDialog();
        }

        // Set OnClickListener for Farmer option
        farmerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption("farmer");
            }
        });

        // Set OnClickListener for Others option
        othersOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption("others");
            }
        });

        // Set OnClickListener for Next button
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignup();
            }
        });

        // Set OnClickListener for bardIcon to open the GitHub URL
        infoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGitHubLink();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if the device is in dark mode, if so, show the custom dialog
        if (isDarkModeEnabled()) {
            showCustomDialog();
        }
    }

    private boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    private void showCustomDialog() {
        // Create a custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom, null);
        builder.setView(dialogView);

        // Create the dialog and show it
        dialog = builder.create(); // Initialize the dialog variable
        dialog.show();
    }

    private void selectOption(String option) {
        selectedOption = option;

        if (option.equals("farmer")) {
            // Highlight Farmer Card
            farmerOption.setCardBackgroundColor(Color.parseColor("#E8EAF6")); // Light color for selection
            othersOption.setCardBackgroundColor(Color.WHITE);

            // Show/Hide Tick Icons
            farmerTick.setVisibility(View.VISIBLE);
            othersTick.setVisibility(View.GONE);
        } else if (option.equals("others")) {
            // Highlight Others Card
            othersOption.setCardBackgroundColor(Color.parseColor("#E8EAF6")); // Light color for selection
            farmerOption.setCardBackgroundColor(Color.WHITE);

            // Show/Hide Tick Icons
            othersTick.setVisibility(View.VISIBLE);
            farmerTick.setVisibility(View.GONE);
        }

        // Enable Next Button
        nextButton.setEnabled(true);
    }

    private void navigateToSignup() {
        Intent intent;
        if (selectedOption.equals("farmer")) {
            intent = new Intent(MainActivity.this, FarmerSignupActivity.class);
        } else if (selectedOption.equals("others")) {
            intent = new Intent(MainActivity.this, OthersSignupActivity.class);
        } else {
            // Default case (optional)
            return;
        }
        startActivity(intent);
    }

    // Method to open GitHub link
    private void openGitHubLink() {
        String url = "https://github.com/Utkarsh140503/IntelHackathonVITUS";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
