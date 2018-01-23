package com.abhishek.nytimes.home.presenter;

import android.os.Bundle;

import com.abhishek.nytimes.R;
import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.model.NewsItem;
import com.abhishek.nytimes.repository.IDataProvider;

public class NewsListPresenter implements INewsListPresenter, IDataProvider.Callback {
    private IDataProvider newsProvider;
    private INewsListView view;

    public NewsListPresenter(IDataProvider newsProvider) {
        this.newsProvider = newsProvider;
    }

    @Override
    public void init(QueryType type) {
        switch (type) {
            case Recent:
                newsProvider.clearSearchItems();
                if (newsProvider.getRecentItemCount() > 0)
                    view.notifyNewsChanged(type);
                else
                    newsProvider.loadRecentItems();
                break;
            case Search:
                view.notifyNewsChanged(type);
                break;
        }
    }

    @Override
    public void setQuery(String query) {
        newsProvider.setSearchParameter(query);
    }

    @Override
    public void getNextPage(QueryType type) {
        switch (type) {
            case Recent:
                if (newsProvider.canExecuteRecentQuery()) {
                    view.setOngoing(true, type);
                    newsProvider.loadRecentItems();
                }
                break;
            case Search:
                if (newsProvider.canExecuteSearchQuery()) {
                    view.setOngoing(true, type);
                    newsProvider.loadSearchItems();
                }
                break;
        }
    }

    @Override
    public void clearNews(QueryType type) {
        switch (type) {
            case Recent:
                newsProvider.clearRecentItems();
                view.clearData(QueryType.Recent);
                break;
            case Search:
                newsProvider.clearSearchItems();
                view.clearData(QueryType.Search);
                break;
        }
    }

    @Override
    public boolean isOnGoing(QueryType type) {
        return type == QueryType.Recent ? newsProvider.isRecentOngoing() : newsProvider.isSearchOngoing();
    }

    @Override
    public NewsItem getNewsItem(int position, QueryType type) {
        switch (type) {
            case Recent:
                return newsProvider.getRecentItem(position);
            case Search:
                return newsProvider.getSearchItem(position);
            default:
                return null;
        }
    }

    @Override
    public int getNewsCount(QueryType type) {
        return type == QueryType.Recent ? newsProvider.getRecentItemCount() : newsProvider.getSearchItemCount();
    }

    @Override
    public void setView(INewsListView view) {
        this.view = view;
    }

    @Override
    public void onSaveState(Bundle bundle) {
    }

    @Override
    public void onRestoreState(Bundle bundle) {
    }

    @Override
    public void onCreate() {
        newsProvider.setCallback(this);
    }

    @Override
    public void onDestroy() {
        newsProvider.setCallback(null);
    }

    @Override
    public void recentItemsLoaded(int count) {
        view.setOngoing(false, QueryType.Recent);
        if (count > 0)
            view.notifyNewsAdded(newsProvider.getRecentItemCount() - count, count, QueryType.Recent);
        else
            view.showError(R.string.no_result_message, QueryType.Recent);
    }

    @Override
    public void recentItemsError() {
        view.setOngoing(false, QueryType.Recent);
        view.showError(R.string.error_message, QueryType.Recent);
    }

    @Override
    public void searchItemsLoaded(int count) {
        view.setOngoing(false, QueryType.Search);
        if (count > 0)
            view.notifyNewsAdded(newsProvider.getSearchItemCount() - count, count, QueryType.Search);
        else
            view.showError(R.string.no_result_message, QueryType.Recent);
    }

    @Override
    public void searchItemsError() {
        view.setOngoing(false, QueryType.Search);
        view.showError(R.string.error_message, QueryType.Search);
    }
}
