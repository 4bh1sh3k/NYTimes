package com.abhishek.nytimes.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.abhishek.nytimes.model.ArticleSearchResponse;
import com.abhishek.nytimes.model.ArticleSearchResult;
import com.abhishek.nytimes.model.NewsItem;
import com.abhishek.nytimes.service.NYTService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class NewsProvider implements IDataProvider {

    private static final int PAGE_COUNT_THRESHOLD = 100;
    private int recentPageCount = -1, searchPageCount = -1;
    private int recentHitsCount = -1, searchHitsCount = 0;
    private boolean isLoadingRecent = false, isLoadingSearch = false;
    private String query = null;

    private ArrayList<NewsItem> recentItems;
    private ArrayList<NewsItem> searchItems;
    private NYTService service;
    private IDataProvider.Callback callback;

    public NewsProvider(NYTService service) {
        recentItems = new ArrayList<>();
        searchItems = new ArrayList<>();

        this.service = service;
    }

    //region Recent Items

    @Override
    public void loadRecentItems() {
        if (canExecuteRecentQuery()) {
            ++recentPageCount;
            isLoadingRecent = true;

            service.getArticles(recentPageCount).enqueue(new retrofit2.Callback<ArticleSearchResult>() {
                @Override
                public void onResponse(@NonNull Call<ArticleSearchResult> call, @NonNull Response<ArticleSearchResult> response) {
                    isLoadingRecent = false;

                    ArticleSearchResult result = response.body();
                    if (response.isSuccessful() && result != null) {
                        ArticleSearchResponse articles = result.getResponse();
                        recentHitsCount = articles.getMeta().getHits();

                        recentItems.addAll(articles.getNews());
                        callback.recentItemsLoaded(articles.getNews().size());
                    } else
                        callback.recentItemsError();
                }

                @Override
                public void onFailure(@NonNull Call<ArticleSearchResult> call, @NonNull Throwable t) {
                    isLoadingRecent = false;

                    Log.e(getClass().getSimpleName(), t.getMessage());
                    callback.recentItemsError();
                }
            });
        }
    }

    @Override
    public boolean canExecuteRecentQuery() {
        return !isLoadingRecent &&
                (recentHitsCount < 0 || (recentItems.size() < recentHitsCount && recentPageCount < PAGE_COUNT_THRESHOLD));
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
    public boolean isRecentOngoing() {
        return isLoadingRecent;
    }

    @Override
    public void clearRecentItems() {
        recentItems.clear();
        recentPageCount = recentHitsCount = -1;
    }

    //endregion

    //region Search Items

    @Override
    public void setSearchParameter(String query) {
        this.query = query;
        clearSearchItems();
    }

    @Override
    public void loadSearchItems() {
        if (canExecuteSearchQuery()) {
            ++searchPageCount;
            isLoadingSearch = true;

            service.searchArticles(query, searchPageCount).enqueue(new retrofit2.Callback<ArticleSearchResult>() {
                @Override
                public void onResponse(Call<ArticleSearchResult> call, Response<ArticleSearchResult> response) {
                    isLoadingSearch = false;

                    if (response.isSuccessful()) {
                        ArticleSearchResponse result = response.body().getResponse();
                        searchHitsCount = result.getMeta().getHits();

                        searchItems.addAll(result.getNews());
                        callback.searchItemsLoaded(result.getNews().size());
                    } else
                        callback.searchItemsError();
                }

                @Override
                public void onFailure(Call<ArticleSearchResult> call, Throwable t) {
                    isLoadingSearch = false;

                    Log.e(getClass().getSimpleName(), t.getMessage());
                    callback.searchItemsError();
                }
            });
        }
    }

    @Override
    public boolean canExecuteSearchQuery() {
        return !isLoadingSearch &&
                (searchHitsCount < 0 || (searchItems.size() < searchHitsCount && searchPageCount < PAGE_COUNT_THRESHOLD));
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
    public boolean isSearchOngoing() {
        return isLoadingSearch;
    }

    @Override
    public void clearSearchItems() {
        searchItems.clear();
        searchPageCount = searchHitsCount = -1;
    }

    //endregion

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
