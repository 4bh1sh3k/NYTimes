package com.abhishek.nytimes.model;

import java.util.List;

public interface IDataProvider {
    void addToRecentItems(List<NewsItem> items);
    List<NewsItem> getRecentItems();
    NewsItem getRecentItem(int position);
    int getRecentItemCount();
    void clearRecentItems();

    void addToSearchItems(List<NewsItem> items);
    List<NewsItem> getSearchItems();
    NewsItem getSearchItem(int position);
    int getSearchItemCount();
    void clearSearchItems();
}
