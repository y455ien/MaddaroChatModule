package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FileListResponse {

    @SerializedName("documents")
    @Expose
    private List<AttachmentModel> documents;
    @SerializedName("is_limit_exceeded")
    @Expose
    private Boolean isLimitExceeded;

    public List<AttachmentModel> getDocuments() {
        return documents;
    }

    public Boolean isLimitExceeded() {
        return isLimitExceeded;
    }
}
