package com.abhishek.nytimes.details;

import com.abhishek.nytimes.home.SearchType;

public interface IDetailsPresenter {
    void onAttachView(IDetailsView view);
    void onDetachView();

    void getNews(int position, SearchType type);
    boolean isNewsReady();
    void clearNews();
}
