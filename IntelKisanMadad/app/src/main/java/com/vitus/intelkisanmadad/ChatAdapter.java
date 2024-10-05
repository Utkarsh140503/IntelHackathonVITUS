package com.vitus.intelkisanmadad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<ChatMessage> chatMessages;
    private String currentUserId;

    public ChatAdapter(List<ChatMessage> chatMessages, String currentUserId) {
        this.chatMessages = chatMessages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (chatMessage.getSenderId().equals(currentUserId)) {
            holder.senderMessageTextView.setText(chatMessage.getMessage());
            holder.senderMessageTextView.setVisibility(View.VISIBLE);
            holder.receiverMessageTextView.setVisibility(View.GONE);
        } else {
            holder.receiverMessageTextView.setText(chatMessage.getMessage());
            holder.receiverMessageTextView.setVisibility(View.VISIBLE);
            holder.senderMessageTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessageTextView, receiverMessageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageTextView = itemView.findViewById(R.id.senderMessageTextView);
            receiverMessageTextView = itemView.findViewById(R.id.receiverMessageTextView);
        }
    }
}
