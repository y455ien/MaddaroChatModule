package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatRoomSubscriberModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("user_id")
    @Expose
    private ChatBuddyModel user;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("updated_at")
    @Expose
    private Long updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer __v;

    public ChatRoomSubscriberModel(ChatBuddyModel user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String get_id() {
        return _id;
    }

    public ChatBuddyModel getUser() {
        return user;
    }

    public String getRoomId() {
        return roomId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Integer get__v() {
        return __v;
    }
}
