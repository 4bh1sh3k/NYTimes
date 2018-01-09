package com.abhishek.nytimes.model;

import com.google.gson.annotations.SerializedName;

public class Headline {
    @SerializedName("main")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
