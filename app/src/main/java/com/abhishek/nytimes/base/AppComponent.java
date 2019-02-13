package com.abhishek.nytimes.base;

import com.abhishek.nytimes.app.NYTApplication;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, AndroidInjectionModule.class, ActivityInjectorModule.class })
public interface AppComponent {
    void inject(NYTApplication app);
}
