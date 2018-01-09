package com.abhishek.nytimes.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.abhishek.nytimes.R;
import com.abhishek.nytimes.model.ArticleSearchResponse;
import com.abhishek.nytimes.model.ArticleSearchResult;
import com.abhishek.nytimes.model.IDataProvider;
import com.abhishek.nytimes.model.NewsItem;
import com.abhishek.nytimes.service.NYTService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListPresenter implements INewsListPresenter {

    private static final int PAGE_COUNT_THRESHOLD = 100;
    private int recentSearchPageCount = -1, customSearchPageCount = -1;
    private int recentSearchHitsCount = 0, customSearchHitsCount = 0;
    private String query = null;
    private boolean isRecentSearchOngoing = false, isCustomSearchOngoing = false;

    private Callback<ArticleSearchResult> recentNewsCallback, searchNewsCallback;

    private IDataProvider newsProvider;
    private INewsListView view;

    private NYTService service;
    private NetworkInfo networkInfo;

    public NewsListPresenter(IDataProvider newsProvider, NYTService service) {
        this.newsProvider = newsProvider;
        this.service = service;

        recentNewsCallback = new Callback<ArticleSearchResult>() {
            @Override
            public void onResponse(Call<ArticleSearchResult> call, Response<ArticleSearchResult> response) {
                if (response.isSuccessful()) {
                    ArticleSearchResponse result = response.body().getResponse();

                    recentSearchHitsCount = result.getMeta().getHits();
                    setResult(result.getNews(), SearchType.Recent);
                } else
                    setResult(R.string.error_message_home, SearchType.Recent);
            }

            @Override
            public void onFailure(Call<ArticleSearchResult> call, Throwable t) {
                Log.e(getClass().getSimpleName(), t.getMessage());
                setResult(R.string.error_message_home, SearchType.Recent);
            }
        };

        searchNewsCallback = new Callback<ArticleSearchResult>() {
            @Override
            public void onResponse(Call<ArticleSearchResult> call, Response<ArticleSearchResult> response) {
                if (response.isSuccessful()) {
                    ArticleSearchResponse result = response.body().getResponse();

                    customSearchHitsCount = result.getMeta().getHits();
                    setResult(result.getNews(), SearchType.Custom);
                } else
                    setResult(R.string.error_message, SearchType.Custom);
            }

            @Override
            public void onFailure(Call<ArticleSearchResult> call, Throwable t) {
                Log.e(getClass().getSimpleName(), t.getMessage());
                setResult(R.string.error_message, SearchType.Custom);
            }
        };
    }

    @Override
    public void onAttachView(INewsListView view, SearchType type) {
        this.view = view;
        view.setOngoing(isOnGoing(type), type);

        switch (type) {
            case Recent:
                if (newsProvider.getRecentItemCount() > 0)
                    view.notifyNewsAdded(0, newsProvider.getRecentItemCount(), SearchType.Recent);
                break;
            case Custom:
                if (newsProvider.getSearchItemCount() > 0)
                    view.notifyNewsAdded(0, newsProvider.getSearchItemCount(), SearchType.Custom);
                break;
        }
    }

    @Override
    public void onDetachView() {
        if (isViewAttached())
            view = null;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;

        clearNews(SearchType.Custom);
        if (query != null)
            getNextPage(SearchType.Custom);
    }

    @Override
    public void getNextPage(SearchType type) {
        if(!isOnGoing(type)) {
            if (networkInfo == null) {
                networkInfo = ((ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            }
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                if (isViewAttached())
                    view.hideError();

                switch (type) {
                    case Recent:
                        if ((newsProvider.getRecentItemCount() == 0 ||  newsProvider.getRecentItemCount() <  recentSearchHitsCount) && recentSearchPageCount < PAGE_COUNT_THRESHOLD) {
                            ++recentSearchPageCount;
                            setBusy(SearchType.Recent);

                            service.getArticles(recentSearchPageCount, view.getContext().getString(R.string.api_key)).enqueue(recentNewsCallback);
                        }
                        break;
                    case Custom:
                        if (query != null)
                            if ((newsProvider.getSearchItemCount() == 0 || newsProvider.getSearchItemCount() < customSearchHitsCount) && customSearchPageCount < PAGE_COUNT_THRESHOLD) {
                                ++customSearchPageCount;
                                setBusy(SearchType.Custom);

                                service.searchArticles(query, customSearchPageCount, view.getContext().getString(R.string.api_key)).enqueue(searchNewsCallback);
                            }
                        break;
                }
            } else if (isViewAttached()) {
                view.showError(R.string.no_connectivity_message, type);
            }
        }
    }

    @Override
    public void clearNews(SearchType type) {
        switch (type) {
            case Recent:
                recentSearchPageCount = -1;
                recentSearchHitsCount = 0;
                newsProvider.clearRecentItems();
                if (isViewAttached())
                    view.clearData(SearchType.Recent);
                break;
            case Custom:
                customSearchPageCount = -1;
                customSearchHitsCount = 0;
                newsProvider.clearSearchItems();
                if (isViewAttached())
                    view.clearData(SearchType.Custom);
                break;
        }
    }

    @Override
    public boolean isOnGoing(SearchType type) {
        return type == SearchType.Recent ? isRecentSearchOngoing : isCustomSearchOngoing;
    }

    private void setResult(List<NewsItem> items, SearchType type) {
        if (type == SearchType.Recent)
            isRecentSearchOngoing = false;
        else
            isCustomSearchOngoing = false;

        if (isViewAttached())
            view.setOngoing(false, type);

        if (items == null || items.size() == 0) {
            if (isViewAttached())
                view.showError(R.string.no_result_message, type);
        } else {
            switch (type) {
                case Recent:
                    int previousRecentCount = newsProvider.getRecentItemCount();
                    newsProvider.addToRecentItems(items);

                    if (isViewAttached())
                        view.notifyNewsAdded(previousRecentCount, items.size(), SearchType.Recent);
                    break;
                case Custom:
                    int previousSearchCount = newsProvider.getSearchItemCount();
                    newsProvider.addToSearchItems(items);

                    if (isViewAttached())
                        view.notifyNewsAdded(previousSearchCount, items.size(), SearchType.Custom);
                    break;
            }
        }
    }

    private void setResult(int messageId, SearchType type) {
        if (type == SearchType.Recent)
            isRecentSearchOngoing = false;
        else
            isCustomSearchOngoing = false;

        if (isViewAttached()) {
            view.setOngoing(false, type);
            view.showError(messageId,type);
        }
    }


    @Override
    public NewsItem getNewsItem(int position, SearchType type) {
        switch (type) {
            case Recent:
                return newsProvider.getRecentItem(position);
            case Custom:
                return newsProvider.getSearchItem(position);
            default:
                return null;
        }
    }

    @Override
    public int getNewsCount(SearchType type) {
        return type == SearchType.Recent ? newsProvider.getRecentItemCount() : newsProvider.getSearchItemCount();
    }

    private boolean isViewAttached() {
        return view != null;
    }

    private void setBusy(SearchType type) {
        if (type == SearchType.Recent)
            isRecentSearchOngoing = true;
        else
            isCustomSearchOngoing = true;

        if (isViewAttached())
            view.setOngoing(true, type);
    }
}
