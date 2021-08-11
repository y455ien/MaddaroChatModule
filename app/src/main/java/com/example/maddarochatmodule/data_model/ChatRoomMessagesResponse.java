package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatRoomMessagesResponse implements Serializable {
    @SerializedName("messages")
    @Expose
    private List<ChatMessageModel> messages;

    public List<ChatMessageModel> getMessages() {
        return messages;
    }
}
