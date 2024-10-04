package com.vitus.intelkisanmadad;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FarmerSignupActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPhone, etLocation, etLanguage, etPassword, etPlotSize;
    private MaterialButton btnSignup;
    private TextView tvLogin;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_signup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        // Initialize Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Farmers");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("FarmerPrefs", MODE_PRIVATE);

        // Initialize Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        etLanguage = findViewById(R.id.etLanguage);
        etPassword = findViewById(R.id.etPassword);
        etPlotSize = findViewById(R.id.etPlotSize);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tv_login);

        // Check for stored credentials
        checkStoredCredentials();

        // Set onClickListener for the Signup Button
        btnSignup.setOnClickListener(view -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String language = etLanguage.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String plotSize = etPlotSize.getText().toString().trim();

            // Validate inputs and check if email is a Gmail address
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)
                    || TextUtils.isEmpty(location) || TextUtils.isEmpty(language)
                    || TextUtils.isEmpty(password) || TextUtils.isEmpty(plotSize)) {
                Toast.makeText(FarmerSignupActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(FarmerSignupActivity.this, "Please use a valid Gmail address.", Toast.LENGTH_SHORT).show();
            } else {
                saveFarmerData(name, email, phone, location, language, password, plotSize);
            }
        });

        // Set onClickListener for the Login TextView
        tvLogin.setOnClickListener(view -> showLoginDialog());
    }

    private void checkStoredCredentials() {
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        if (email != null && password != null) {
            loginFarmer(email, password, null);
        }
    }

    private void saveFarmerData(String name, String email, String phone, String location, String language, String password, String plotSize) {
        String emailPrefix = email.split("@")[0];

        HashMap<String, String> farmerData = new HashMap<>();
        farmerData.put("Name", name);
        farmerData.put("Email", email);
        farmerData.put("Phone", phone);
        farmerData.put("Location", location);
        farmerData.put("Language", language);
        farmerData.put("Password", password);
        farmerData.put("PlotSize", plotSize);

        databaseReference.child(emailPrefix).setValue(farmerData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(FarmerSignupActivity.this, "Farmer registered successfully!", Toast.LENGTH_SHORT).show();
                        clearFields();
                    } else {
                        Toast.makeText(FarmerSignupActivity.this, "Registration failed! Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etLocation.setText("");
        etLanguage.setText("");
        etPassword.setText("");
        etPlotSize.setText("");
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FarmerSignupActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_login, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        // Reference to dialog's EditTexts, Button, and CheckBox
        TextInputEditText etLoginEmail = dialog.findViewById(R.id.etLoginEmail);
        TextInputEditText etLoginPassword = dialog.findViewById(R.id.etLoginPassword);
        MaterialButton btnLogin = dialog.findViewById(R.id.btnLogin);
        CheckBox cbRememberMe = dialog.findViewById(R.id.cbRememberMe);

        // Set onClickListener for login button in the dialog
        btnLogin.setOnClickListener(view -> {
            String email = etLoginEmail.getText().toString().trim();
            String password = etLoginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(FarmerSignupActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(FarmerSignupActivity.this, "Please use a valid Gmail address.", Toast.LENGTH_SHORT).show();
            } else {
                loginFarmer(email, password, dialog);

                if (cbRememberMe.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                }
            }
        });
    }

    private void loginFarmer(String email, String password, AlertDialog dialog) {
        String emailPrefix = email.split("@")[0];

        databaseReference.child(emailPrefix).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("Password").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(password)) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(FarmerSignupActivity.this, "Login successful! Welcome back, " + emailPrefix, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FarmerSignupActivity.this, FarmersDashboardActivity.class);
                        intent.putExtra("userid", emailPrefix);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(FarmerSignupActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FarmerSignupActivity.this, "No account found with this email.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FarmerSignupActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
