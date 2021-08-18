package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("mime_type")
    @Expose
    private String mimeType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public ImageModel(String id, String size, String path, String mimeType, String createdAt, String updatedAt) {
        this.id = id;
        this.size = size;
        this.path = path;
        this.mimeType = mimeType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static List<ImageModel> tomModel(List<ImageEntity> entities) {
        List<ImageModel> list = new ArrayList<>();
        if (entities == null || entities.isEmpty())
            return list;

        for (ImageEntity entity : entities) {
            ImageModel model = new ImageModel(
                    entity.getId(),
                    entity.getSize(),
                    entity.getPath(),
                    entity.getMimeType(),
                    entity.getCreatedAt(),
                    entity.getUpdatedAt()
            );

            list.add(model);
        }

        return list;
    }
}
