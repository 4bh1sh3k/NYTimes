package com.abhishek.nytimes.home.presenter;

import android.os.Bundle;

import com.abhishek.nytimes.home.QueryType;
import com.abhishek.nytimes.model.NewsItem;

public interface INewsListPresenter {
    void init(QueryType type);

    void setQuery(String query);

    void getNextPage(QueryType type);

    void clearNews(QueryType type);

    boolean isOnGoing(QueryType type);

    NewsItem getNewsItem(int position, QueryType type);

    int getNewsCount(QueryType type);

    void setView(INewsListView view);

    void onSaveState(Bundle bundle);

    void onRestoreState(Bundle bundle);

    void onCreate();

    void onDestroy();

    interface INewsListView {
        void setOngoing(boolean isOngoing, QueryType type);

        void notifyNewsChanged(QueryType type);

        void notifyNewsAdded(int startIndex, int count, QueryType type);

        void clearData(QueryType type);

        void showError(int stringId, QueryType type);
    }
}