package com.example.maddarochatmodule.data_model;

import com.example.maddarochatmodule.util.StringUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AttachmentModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("documents")
    @Expose
    private List<FolderModel> documents = null;
    @SerializedName("image")
    @Expose
    ImageModel image;

    public ImageModel getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public List<FolderModel> getDocuments() {
        return documents;
    }


    public String getName() {
        if (StringUtils.getLanguage().equals("ar"))
            return nameAr;
        return nameEn;
    }
}
