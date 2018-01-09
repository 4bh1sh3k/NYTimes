package com.abhishek.nytimes.home;

import android.content.Context;

public interface INewsListView {
    Context getContext();

    void setOngoing(boolean isOngoing, SearchType type);
    void notifyNewsAdded(int startIndex, int count, SearchType type);
    void clearData(SearchType type);

    void showError(int stringId, SearchType type);
    void hideError();
}
