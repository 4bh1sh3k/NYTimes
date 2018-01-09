package com.abhishek.nytimes.di;

import com.abhishek.nytimes.details.DetailsActivity;
import com.abhishek.nytimes.home.NewsListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void injectActivity(NewsListActivity activity);
    void injectActivity(DetailsActivity activity);
}
