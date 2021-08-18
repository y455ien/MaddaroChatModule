package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatMessageCountResponse {

    @SerializedName("total_unread_messages")
    @Expose
    private int totalUnreadMessages;

    public int getTotalUnreadMessages() {
        return totalUnreadMessages;
    }

    public void setTotalUnreadMessages(int totalUnreadMessages) {
        this.totalUnreadMessages = totalUnreadMessages;
    }

}


