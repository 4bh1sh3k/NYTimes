package com.abhishek.nytimes.di;

import com.abhishek.nytimes.details.DetailsPresenter;
import com.abhishek.nytimes.details.IDetailsPresenter;
import com.abhishek.nytimes.home.INewsListPresenter;
import com.abhishek.nytimes.home.NewsListPresenter;
import com.abhishek.nytimes.model.IDataProvider;
import com.abhishek.nytimes.model.NewsProvider;
import com.abhishek.nytimes.service.NYTService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class AppModule {

    @Provides @Singleton
    IDataProvider provideDataProvider() {
        return new NewsProvider();
    }

    @Provides @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides @Singleton
    NYTService provideNYTService(Retrofit retrofit){
        return retrofit.create(NYTService.class);
    }

    @Provides @Singleton
    INewsListPresenter provideNewsPresenter(IDataProvider provider, NYTService service) {
        return new NewsListPresenter(provider, service);
    }

    @Provides @Singleton
    IDetailsPresenter provideDetailsPresenter(IDataProvider provider) {
        return new DetailsPresenter(provider);
    }
}
