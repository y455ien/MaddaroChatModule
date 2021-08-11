package com.example.maddarochatmodule.data_model;

import com.google.gson.annotations.SerializedName;

public class SuccessResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
