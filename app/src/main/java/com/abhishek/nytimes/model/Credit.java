package com.abhishek.nytimes.model;

import com.google.gson.annotations.SerializedName;

public class Credit {
    @SerializedName("original")
    private String author;

    public String getAuthor() {
        return author;
    }
}
