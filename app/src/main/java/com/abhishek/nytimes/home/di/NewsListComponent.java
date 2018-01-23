package com.abhishek.nytimes.home.di;

import com.abhishek.nytimes.base.ActivityScope;
import com.abhishek.nytimes.base.AppComponent;
import com.abhishek.nytimes.home.view.NewsListActivity;

import dagger.Component;

@ActivityScope
@Component(modules = {NewsListModule.class},dependencies = AppComponent.class)
public interface NewsListComponent {
    void injectActivity(NewsListActivity activity);
}
