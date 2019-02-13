package com.abhishek.nytimes.app;

import android.app.Activity;
import android.app.Application;

import com.abhishek.nytimes.base.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

public class NYTApplication extends Application implements HasActivityInjector {

    @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.create()
            .inject(this);
    }

    @Override public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
