package com.abhishek.nytimes.service;

import com.abhishek.nytimes.model.ArticleSearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTService {
    @GET("svc/search/v2/articlesearch.json")
    Call<ArticleSearchResult> searchArticles(@Query("q") String query, @Query("page") int pageCount);

    @GET("svc/search/v2/articlesearch.json")
    Call<ArticleSearchResult> getArticles(@Query("page") int pageCount);
}
