package com.example.maddarochatmodule.data_model;

import androidx.annotation.Nullable;

import com.example.maddarochatmodule.data_model.annotation.MessageType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.io.Serializable;
import java.util.Date;

public class ChatMessageModel implements Serializable, IMessage, MessageContentType, MessageContentType.Image {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("type")
    @Expose
    @MessageType
    private Integer type;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("message")
    @Expose
    private String body;



    @SerializedName("user")
    @Expose
    private ChatBuddyModel user;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("updated_at")
    @Expose
    private Long updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer __v;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("duration")
    @Expose
    private Long voiceDuration;
    @SerializedName("fileName")
    @Expose
    private String documentName;
    @Expose
    private boolean isVoicePlaying = false;

    public ChatMessageModel(String id, String body, ChatBuddyModel user, Long createdAt) {
        this(id, body, user, createdAt, MessageType.TEXT);
    }

    public ChatMessageModel(String id, String body, Long voiceDuration, ChatBuddyModel user, Long createdAt) {
        this.id = id;
        this.body = body;
        this.voiceDuration = voiceDuration;
        this.user = user;
        this.createdAt = createdAt;
        this.type = MessageType.VOICE;
    }

    public ChatMessageModel(String id, String body, ChatBuddyModel user, Long createdAt, @MessageType int type) {
        this.id = id;
        this.body = body;
        this.user = user;
        this.createdAt = createdAt;
        this.type = type;
    }

    // Public Used Methods

    @Override
    @Nullable
    public IUser getUser() {
        return user;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getText() {
        return this.type == MessageType.TEXT ? body : null;
    }

    public String getPhotoUrl() {
        return this.type == MessageType.IMAGE ? body : null;
    }

    @Nullable
    public String getVoiceUrl() {
        return this.type == MessageType.VOICE ? body : null;
    }

    @Nullable
    public String getDocumentUrl() {
        return this.type == MessageType.DOCUMENT ? body : null;
    }

    @Nullable
    public String getDocumentName() {
        return documentName;
    }

    @Nullable
    public Long getVoiceDuration() {
        return voiceDuration;
    }

    @Override
    public Date getCreatedAt() {
        return new Date(createdAt);
    }

    public Long getCreatedAtLong() {
        return createdAt;
    }

    public boolean isVoicePlaying() {
        return isVoicePlaying;
    }

    public void setVoicePlaying(boolean voicePlaying) {
        isVoicePlaying = voicePlaying;
    }

    // Unused Methods

    @Override
    public String getId() {
        return id;
    }

    public String get_id() {
        return _id;
    }

    /**
     * Should return null in order to avoid some chat-kit issues
     * Use getPhotoUrl() Instead
     */
    @Nullable
    @Override
    @Deprecated
    public String getImageUrl() {
        return null;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String newBody) {
        body = newBody;
    }

    public Integer get__v() {
        return __v;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUser(ChatBuddyModel user) {
        this.user = user;
    }

    public ChatBuddyModel getSenderUser() {
        return user;
    }
}
