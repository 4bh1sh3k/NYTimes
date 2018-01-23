package com.abhishek.nytimes.details.di;

import com.abhishek.nytimes.details.preseter.DetailsPresenter;
import com.abhishek.nytimes.details.preseter.IDetailsPresenter;
import com.abhishek.nytimes.base.ActivityScope;
import com.abhishek.nytimes.repository.IDataProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailsModule {
    @Provides @ActivityScope
    public IDetailsPresenter providePresenter(IDataProvider provider) {
        return new DetailsPresenter(provider);
    }
}
