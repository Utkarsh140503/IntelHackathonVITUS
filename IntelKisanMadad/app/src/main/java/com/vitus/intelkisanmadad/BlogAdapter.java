package com.vitus.intelkisanmadad;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private final String[] blogUrls; // Array to hold blog URLs

    public BlogAdapter(String[] blogUrls) {
        this.blogUrls = blogUrls;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the blog card
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_blog, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        // Set the blog URL text
        String blogUrl = blogUrls[position];
        holder.blogTextView.setText(blogUrl);

        // Set click listener to open the blog in a browser (Chrome or default)
        holder.itemView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(blogUrl));
            v.getContext().startActivity(browserIntent);
        });
    }

    @Override
    public int getItemCount() {
        return blogUrls.length;
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView blogTextView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            blogTextView = itemView.findViewById(R.id.blogTextView);
        }
    }
}
