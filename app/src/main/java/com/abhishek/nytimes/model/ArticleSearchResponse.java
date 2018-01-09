package com.abhishek.nytimes.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleSearchResponse {
    @SerializedName("docs")
    private List<NewsItem> news;

    private Meta meta;

    public List<NewsItem> getNews() {
        return news;
    }

    public void setNews(List<NewsItem> news) {
        this.news = news;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
