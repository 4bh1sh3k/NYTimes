package com.abhishek.nytimes.base;

import com.abhishek.nytimes.details.preseter.DetailsPresenter;
import com.abhishek.nytimes.details.preseter.IDetailsPresenter;
import com.abhishek.nytimes.repository.IDataProvider;
import com.abhishek.nytimes.repository.NewsProvider;
import com.abhishek.nytimes.service.NYTService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class AppModule {

    @Provides
    @Singleton
    IDataProvider provideDataProvider(NYTService service) {
        return new NewsProvider(service);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("api-key", "API_KEY")
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                }).build();


        return new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    NYTService provideNYTService(Retrofit retrofit) {
        return retrofit.create(NYTService.class);
    }

    @Provides
    @Singleton
    IDetailsPresenter provideDetailsPresenter(IDataProvider provider) {
        return new DetailsPresenter(provider);
    }
}
