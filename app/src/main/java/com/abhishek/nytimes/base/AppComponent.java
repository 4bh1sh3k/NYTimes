package com.abhishek.nytimes.base;

import com.abhishek.nytimes.app.NYTApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AppModule.class, AndroidInjectionModule.class, ActivityInjectorModule.class})
public interface AppComponent {
    void inject(NYTApplication app);
}
