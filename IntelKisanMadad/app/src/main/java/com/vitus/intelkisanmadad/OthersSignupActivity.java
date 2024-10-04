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

public class OthersSignupActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPhone, etLocation, etPassword;
    private MaterialButton btnSignup;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView tvLogin;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_signup);

        // Set status bar color for devices with Lollipop or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#F5F5F5"));
        }

        // Initialize Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Buyers");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("BuyerPrefs", MODE_PRIVATE);

        // Initialize UI elements
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tv_login);

        // Check for stored credentials
        checkStoredCredentials();

        // Set click listener for Signup button
        btnSignup.setOnClickListener(v -> {
            // Retrieve values from the form
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Extract username from email
            String username = extractUsernameFromEmail(email);

            if (validateInputs(name, email, phone, location, password, username)) {
                // Create a new Buyer object
                Buyer newBuyer = new Buyer(name, email, phone, location, password);

                // Save data to Firebase Realtime Database
                databaseReference.child(username).setValue(newBuyer)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(OthersSignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                                clearFields();
                            } else {
                                Toast.makeText(OthersSignupActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Set click listener for Login TextView
        tvLogin.setOnClickListener(view -> showLoginDialog());
    }

    private String extractUsernameFromEmail(String email) {
        int index = email.indexOf("@");
        if (index > 0) {
            return email.substring(0, index);
        }
        return email;
    }

    private void checkStoredCredentials() {
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);

        if (email != null && password != null) {
            loginBuyer(email, password, null);
        }
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OthersSignupActivity.this);
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
                Toast.makeText(OthersSignupActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
            } else if (!email.endsWith("@gmail.com")) {
                Toast.makeText(OthersSignupActivity.this, "Please use a valid Gmail address.", Toast.LENGTH_SHORT).show();
            } else {
                loginBuyer(email, password, dialog);

                if (cbRememberMe.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                }
            }
        });
    }

    private void loginBuyer(String email, String password, AlertDialog dialog) {
        String emailPrefix = email.split("@")[0];

        databaseReference.child(emailPrefix).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(password)) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        Toast.makeText(OthersSignupActivity.this, "Login successful! Welcome back, " + emailPrefix, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OthersSignupActivity.this, BuyersDashboardActivity.class);
                        intent.putExtra("userid", emailPrefix);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OthersSignupActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OthersSignupActivity.this, "No account found with this email.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OthersSignupActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String name, String email, String phone, String location, String password, String username) {
        // Basic validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || location.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.endsWith("@gmail.com")) {
            Toast.makeText(this, "Email must be a Gmail address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etLocation.setText("");
        etPassword.setText("");
    }

    public static class Buyer {
        public String name, email, phone, location, password;

        public Buyer() {
            // Default constructor required for calls to DataSnapshot.getValue(Buyer.class)
        }

        public Buyer(String name, String email, String phone, String location, String password) {
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.location = location;
            this.password = password;
        }
    }
}
