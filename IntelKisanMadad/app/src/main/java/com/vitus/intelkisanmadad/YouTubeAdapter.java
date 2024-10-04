package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.YouTubeViewHolder> {

    private final String[] videoUrls; // Array to hold YouTube video URLs

    public YouTubeAdapter(String[] videoUrls) {
        this.videoUrls = videoUrls;
    }

    @NonNull
    @Override
    public YouTubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the YouTube thumbnail card
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_youtube, parent, false);
        return new YouTubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeViewHolder holder, int position) {
        // Get the video URL
        String videoUrl = videoUrls[position];

        // Extract the video ID from the URL
        String videoId = extractVideoId(videoUrl);

        // Build the thumbnail URL
        String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";

        // Use Glide to load the thumbnail image
        Glide.with(holder.itemView.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.ytplaceholderimage) // Optional placeholder image
                .error(R.drawable.ytplaceholderimage) // Optional error image
                .into(holder.thumbnailImageView);

        // Set click listener to open the YouTube video in the YouTube app
        holder.itemView.setOnClickListener(v -> {
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
            youtubeIntent.setPackage("com.google.android.youtube");
            try {
                v.getContext().startActivity(youtubeIntent); // Try opening in YouTube app
            } catch (Exception e) {
                // If YouTube app is not installed, open in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                v.getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoUrls.length;
    }

    private String extractVideoId(String url) {
        String videoId = "";
        String[] parts = url.split("v=");
        if (parts.length > 1) {
            videoId = parts[1];
            int ampersandIndex = videoId.indexOf("&");
            if (ampersandIndex != -1) {
                videoId = videoId.substring(0, ampersandIndex);
            }
        }
        return videoId;
    }

    public static class YouTubeViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;

        public YouTubeViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
        }
    }
}
