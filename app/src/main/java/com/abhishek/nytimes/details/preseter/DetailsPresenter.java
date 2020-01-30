package com.abhishek.nytimes.details.preseter;

import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.model.NewsItem;
import com.abhishek.nytimes.repository.IDataProvider;

public class DetailsPresenter implements IDetailsPresenter {

    private IDataProvider newsProvider;
    private IDetailsView view;

    public DetailsPresenter(IDataProvider newsProvider) {
        this.newsProvider = newsProvider;
    }

    @Override
    public void setView(IDetailsView view) {
        this.view = view;
    }

    @Override
    public void getNews(int position, QueryType type) {
        NewsItem item = type == QueryType.Recent ? newsProvider.getRecentItem(position) : newsProvider.getSearchItem(position);
        view.showNews(item);
    }
}
