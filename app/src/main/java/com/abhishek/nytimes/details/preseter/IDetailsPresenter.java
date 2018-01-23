package com.abhishek.nytimes.details.preseter;

import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.model.NewsItem;

public interface IDetailsPresenter {
    void setView(IDetailsView view);
    void getNews(int position, QueryType type);

    interface IDetailsView {
        void showNews(NewsItem item);
    }
}
