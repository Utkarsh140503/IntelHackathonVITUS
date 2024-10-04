package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WebinarAdapter extends RecyclerView.Adapter<WebinarAdapter.WebinarViewHolder> {

    private List<Webinar> webinarList;
    private String userId;

    public WebinarAdapter(List<Webinar> webinarList, String userId) {
        this.webinarList = webinarList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public WebinarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.webinar_card, parent, false);
        return new WebinarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WebinarViewHolder holder, int position) {
        Webinar webinar = webinarList.get(position);
        holder.webinarName.setText(webinar.getName());
        holder.webinarDate.setText(webinar.getDate());
        holder.webinarTime.setText(webinar.getTime());
        holder.webinarVenue.setText(webinar.getVenue());
        holder.webinarImage.setImageResource(webinar.getImage());

        // Check if the user is already registered for the webinar
        checkUserRegistrationStatus(webinar.getName(), userId, holder.registerButton);

        // Set onClickListener for register button
        holder.registerButton.setOnClickListener(v -> {
            registerForWebinar(webinar.getName(), userId, holder.registerButton);
        });
    }

    @Override
    public int getItemCount() {
        return webinarList.size();
    }

    public static class WebinarViewHolder extends RecyclerView.ViewHolder {
        TextView webinarName, webinarDate, webinarTime, webinarVenue;
        ImageView webinarImage;
        Button registerButton;

        public WebinarViewHolder(@NonNull View itemView) {
            super(itemView);
            webinarName = itemView.findViewById(R.id.webinar_name);
            webinarDate = itemView.findViewById(R.id.webinar_date);
            webinarTime = itemView.findViewById(R.id.webinar_time);
            webinarVenue = itemView.findViewById(R.id.webinar_venue);
            webinarImage = itemView.findViewById(R.id.webinar_image);
            registerButton = itemView.findViewById(R.id.register_button);
        }
    }

    // Method to check if the user is registered
    private void checkUserRegistrationStatus(String webinarName, String userId, Button registerButton) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Webinars");
        databaseReference.child(webinarName).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User is already registered
                    registerButton.setBackgroundColor(Color.GREEN);
                    registerButton.setText("Registered");
                    registerButton.setEnabled(false); // Disable the button once registered
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
            }
        });
    }

    // Method to register the user for a webinar
    private void registerForWebinar(String webinarName, String userId, Button registerButton) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Webinars");
        databaseReference.child(webinarName).child(userId).setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Registration successful
                registerButton.setBackgroundColor(Color.GREEN);
                registerButton.setText("Registered");
                registerButton.setEnabled(false);
            }
        });
    }
}

class Webinar {
    private String name;
    private String date;
    private String time;
    private String venue;
    private int image;

    public Webinar(String name, String date, String time, String venue, int image) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.image = image;
    }

    public String getName() { return name; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getVenue() { return venue; }
    public int getImage() { return image; }
}
