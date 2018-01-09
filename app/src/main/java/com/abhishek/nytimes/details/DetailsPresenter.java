package com.abhishek.nytimes.details;

import com.abhishek.nytimes.home.SearchType;
import com.abhishek.nytimes.model.IDataProvider;
import com.abhishek.nytimes.model.NewsItem;

public class DetailsPresenter implements IDetailsPresenter {

    private IDataProvider newsProvider;
    private IDetailsView view;

    private NewsItem item;

    public DetailsPresenter(IDataProvider newsProvider) {
        this.newsProvider = newsProvider;
    }

    @Override
    public void onAttachView(IDetailsView view) {
        this.view = view;
        if (item != null)
            view.showNews(item);
    }

    @Override
    public void onDetachView() {
        if (isViewAttached())
            this.view = null;
    }

    @Override
    public void getNews(int position, SearchType type) {
        item = type == SearchType.Recent ? newsProvider.getRecentItem(position) : newsProvider.getSearchItem(position);

        if (isViewAttached())
            view.showNews(item);
    }

    @Override
    public boolean isNewsReady() {
        return item!= null;
    }

    @Override
    public void clearNews() {
        item = null;
    }

    private boolean isViewAttached() {
        return view != null;
    }
}
