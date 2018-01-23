package com.abhishek.nytimes.base;

import com.abhishek.nytimes.details.view.DetailsActivity;
import com.abhishek.nytimes.repository.IDataProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    IDataProvider getDataProvider();
    void injectActivity(DetailsActivity activity);
}
