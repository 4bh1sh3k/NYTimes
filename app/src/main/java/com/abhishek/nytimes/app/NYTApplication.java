package com.abhishek.nytimes.app;

import android.app.Application;

import com.abhishek.nytimes.base.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import javax.inject.Inject;

public class NYTApplication extends Application implements HasAndroidInjector {

    @Inject DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.create()
            .inject(this);
    }

    @Override public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }
}
