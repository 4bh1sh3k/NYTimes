package com.abhishek.nytimes.model;

import com.google.gson.annotations.SerializedName;

public class MultiMedia {
    @SerializedName("url")
    private String subUrl;

    private String type;

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
