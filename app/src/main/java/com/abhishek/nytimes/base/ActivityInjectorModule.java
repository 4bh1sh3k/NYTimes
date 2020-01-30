package com.abhishek.nytimes.base;

import com.abhishek.nytimes.details.di.DetailsModule;
import com.abhishek.nytimes.details.view.DetailsActivity;
import com.abhishek.nytimes.home.di.NewsListModule;
import com.abhishek.nytimes.home.view.NewsListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityInjectorModule {

    @DetailsScope
    @ContributesAndroidInjector(modules = DetailsModule.class)
    abstract DetailsActivity contributeDetailsActivityInjector();

    @HomeScope
    @ContributesAndroidInjector(modules = NewsListModule.class)
    abstract NewsListActivity contributesNewsListActivityInjector();
}
