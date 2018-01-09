package com.abhishek.nytimes.home;

import com.abhishek.nytimes.model.NewsItem;

public interface INewsListPresenter {
    void onAttachView(INewsListView view, SearchType type);
    void onDetachView();

    void setQuery(String query);
    void getNextPage(SearchType type);
    void clearNews(SearchType type);
    boolean isOnGoing(SearchType type);

    NewsItem getNewsItem(int position, SearchType type);
    int getNewsCount(SearchType type);
}