package com.vitus.intelkisanmadad;
public class ChatMessage {
    private String message;
    private String senderId;   // ID of the user who sends the message (seller or buyer)
    private String receiverId; // ID of the user who receives the message
    private long timestamp;    // Timestamp to sort messages

    // Default constructor required for calls to DataSnapshot.getValue(ChatMessage.class)
    public ChatMessage() {
    }

    public ChatMessage(String message, String senderId, String receiverId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
