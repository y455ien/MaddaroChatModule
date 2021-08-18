package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatRoomResponse implements Serializable {
    @SerializedName("room")
    @Expose
    private ChatRoomModel room;

    public ChatRoomModel getRoom() {
        return room;
    }
}
