package com.example.maddarochatmodule.data_model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatRoomModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("updated_at")
    @Expose
    private Long updatedAt;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("last_message")
    @Expose
    private ChatMessageModel lastMessage;
    @SerializedName("subscribers")
    @Expose
    private List<ChatRoomSubscriberModel> subscribers;
    @SerializedName("messages")
    @Expose
    private List<ChatMessageModel> messages;

    @SerializedName("unread_messages")
    @Expose
    private Integer unreadMessages;


    @Expose
    private boolean hasUnreadMessages;

    public Integer getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(Integer unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ChatMessageModel getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessageModel lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<ChatMessageModel> getMessages() {
        return messages;
    }

    public boolean hasUnreadMessages() {
        return hasUnreadMessages;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Nullable
    public ChatBuddyModel getOtherBuddy(@NonNull String unWantedBuddyId) {
        if (subscribers == null || subscribers.isEmpty())
            return null;
        ChatBuddyModel chatBuddyModel = null;
        for (ChatRoomSubscriberModel subscriber : subscribers) {
            if (subscriber.getUser() != null
                    && !TextUtils.isEmpty(subscriber.getUser().getId())
                    && !unWantedBuddyId.equals(subscriber.getUser().getId())) {
                chatBuddyModel = subscriber.getUser();
                break;
            }
        }
        return chatBuddyModel;
    }

}