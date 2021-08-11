package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class ChatBuddyModel implements Serializable, IUser {

    @SerializedName("user_id")
    @Expose
    private String id;
    @SerializedName("id")
    @Expose
    private String _id;
    @SerializedName("_id")
    @Expose
    private String __id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("updated_at")
    @Expose
    private Long updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer __v;
    @SerializedName("image")
    @Expose
    private ImageModel image;

    public ChatBuddyModel(String id) {
        this.id = id;
    }

    public ChatBuddyModel(String id, String name, ImageModel image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return image != null ? image.getPath() : null;
    }

    public String get_id() {
        return _id;
    }

    public String get__id() {
        return __id;
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
