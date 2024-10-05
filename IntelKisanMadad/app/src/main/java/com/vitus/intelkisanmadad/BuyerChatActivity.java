package com.vitus.intelkisanmadad;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerChatActivity extends AppCompatActivity {

    private String sellerId;
    private String buyerId;
    private String commodityName;
    private String chatId;

    private EditText messageEditText;
    private Button sendMessageButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;

    private DatabaseReference chatReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_chat);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        }

        // Get the sellerId, buyerId, and commodityName from the Intent
        sellerId = getIntent().getStringExtra("sellerId");
        buyerId = getIntent().getStringExtra("buyerId");
        commodityName = getIntent().getStringExtra("commodityName");

        // Generate the unique chat ID based on seller, buyer, and commodity
        chatId = generateChatId(sellerId, buyerId, commodityName);

        // Initialize UI elements
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);

        // Initialize the TextViews in the CardView
        TextView sellerIdTextView = findViewById(R.id.sellerIdTextView);
        TextView buyerIdTextView = findViewById(R.id.buyerIdTextView);
        TextView commodityNameTextView = findViewById(R.id.commodityNameTextView);

        // Set the values for seller, buyer, and commodity
        sellerIdTextView.setText("Seller ID: " + sellerId);
        buyerIdTextView.setText("Buyer ID: " + buyerId);
        commodityNameTextView.setText("Commodity: " + commodityName);

        // Set up the RecyclerView and Adapter
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList, buyerId);  // Pass buyerId to track buyer's messages
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Firebase reference for the specific chat
        chatReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);

        // Load existing messages
        loadMessages();

        // Send message when the button is clicked
        sendMessageButton.setOnClickListener(v -> {
            String messageText = messageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText);
            }
        });
    }

    private String generateChatId(String sellerId, String buyerId, String commodityName) {
        // Generate unique chat ID based on seller, buyer, and commodity (ensure no spaces)
        return sellerId + "_" + buyerId + "_" + commodityName.replaceAll("\\s+", "_");
    }

    private void loadMessages() {
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    if (chatMessage != null) {
                        chatMessageList.add(chatMessage);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                chatRecyclerView.scrollToPosition(chatMessageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

    private void sendMessage(String messageText) {
        long timestamp = System.currentTimeMillis();
        ChatMessage chatMessage = new ChatMessage(messageText, buyerId, sellerId, timestamp);

        // Push the message to Firebase under the unique chat ID
        chatReference.push().setValue(chatMessage);

        // Clear the input field after sending the message
        messageEditText.setText("");
    }
}
