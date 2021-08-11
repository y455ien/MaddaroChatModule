package com.example.maddarochatmodule.data_model;

import com.example.maddarochatmodule.util.StringUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FolderModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("attachments")
    @Expose
    private List<FileModel> attachments = new ArrayList<>();

    public String getName() {
        if (StringUtils.getLanguage().equals("ar"))
            return nameAr;
        return nameEn;
    }

    public String getId() {
        return id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public List<FileModel> getAttachments() {
        return attachments;
    }
}
