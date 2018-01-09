package com.abhishek.nytimes.details;

import com.abhishek.nytimes.model.NewsItem;

public interface IDetailsView {
    void showNews(NewsItem item);
}
