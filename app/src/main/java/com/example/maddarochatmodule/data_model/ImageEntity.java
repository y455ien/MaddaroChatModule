package com.example.maddarochatmodule.data_model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ImageEntity {
    @NonNull
    String id;
    String size;
    String path;
    String mimeType;
    String createdAt;
    String updatedAt;

    public ImageEntity(@NonNull String id, String size, String path, String mimeType, String createdAt, String updatedAt) {
        this.id = id;
        this.size = size;
        this.path = path;
        this.mimeType = mimeType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    @NonNull
    public String getId() {
        return id;
    }

    public String getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public static List<ImageEntity> toEntity(List<ImageModel> models) {
        List<ImageEntity> list = new ArrayList<>();
        if (models == null || models.isEmpty())
            return list;

        for (ImageModel model : models) {
            ImageEntity entity = new ImageEntity(
                    model.getId(),
                    model.getSize(),
                    model.getPath(),
                    model.getMimeType(),
                    model.getCreatedAt(),
                    model.getUpdatedAt()
            );

            list.add(entity);
        }

        return list;
    }
}
