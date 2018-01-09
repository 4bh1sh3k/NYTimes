package com.abhishek.nytimes.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NewsProvider implements IDataProvider {

    private ArrayList<NewsItem> recentItems;
    private ArrayList<NewsItem> searchItems;

    public NewsProvider() {
        recentItems = new ArrayList<>();
        searchItems = new ArrayList<>();
    }

    @Override
    public void addToRecentItems(List<NewsItem> items) {
        recentItems.addAll(items);
    }

    @Override
    public List<NewsItem> getRecentItems() {
        return recentItems;
    }

    @Override
    public NewsItem getRecentItem(int position) {
        return position >= recentItems.size() ? null : recentItems.get(position);
    }

    @Override
    public int getRecentItemCount() {
        return recentItems.size();
    }

    @Override
    public void clearRecentItems() {
        recentItems.clear();
    }

    @Override
    public void addToSearchItems(List<NewsItem> items) {
        searchItems.addAll(items);
    }

    @Override
    public List<NewsItem> getSearchItems() {
        return searchItems;
    }

    @Override
    public NewsItem getSearchItem(int position) {
        return position >= searchItems.size() ? null : searchItems.get(position);
    }

    @Override
    public int getSearchItemCount() {
        return searchItems.size();
    }

    @Override
    public void clearSearchItems() {
        searchItems.clear();
    }
}
