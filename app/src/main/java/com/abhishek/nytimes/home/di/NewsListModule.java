package com.abhishek.nytimes.home.di;

import com.abhishek.nytimes.base.HomeScope;
import com.abhishek.nytimes.home.presenter.INewsListPresenter;
import com.abhishek.nytimes.home.presenter.NewsListPresenter;
import com.abhishek.nytimes.repository.IDataProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class NewsListModule {
    @Provides @HomeScope
    public INewsListPresenter providePresenter(IDataProvider provider) {
        return new NewsListPresenter(provider);
    }
}
