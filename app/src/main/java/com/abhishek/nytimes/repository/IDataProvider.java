package com.abhishek.nytimes.repository;

import com.abhishek.nytimes.model.NewsItem;

public interface IDataProvider {

    //region Recent Items

    void loadRecentItems();

    boolean canExecuteRecentQuery();

    NewsItem getRecentItem(int position);

    int getRecentItemCount();

    boolean isRecentOngoing();

    void clearRecentItems();

    //endregion

    //region Search Items

    void setSearchParameter(String query);

    void loadSearchItems();

    boolean canExecuteSearchQuery();

    NewsItem getSearchItem(int position);

    int getSearchItemCount();

    boolean isSearchOngoing();

    void clearSearchItems();

    //endregion

    void setCallback(IDataProvider.Callback callback);

    interface Callback {
        void recentItemsLoaded(int count);

        void recentItemsError();

        void searchItemsLoaded(int count);

        void searchItemsError();
    }
}
