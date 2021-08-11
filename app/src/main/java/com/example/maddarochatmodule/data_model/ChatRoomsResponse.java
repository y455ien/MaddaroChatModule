package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatRoomsResponse implements Serializable {

    @SerializedName("rooms")
    @Expose
    private List<ChatRoomModel> chatRooms;

    public List<ChatRoomModel> getChatRooms() {
        return chatRooms;
    }
}
