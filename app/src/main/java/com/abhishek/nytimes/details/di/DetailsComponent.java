package com.abhishek.nytimes.details.di;

import com.abhishek.nytimes.details.view.DetailsActivity;
import com.abhishek.nytimes.base.ActivityScope;
import com.abhishek.nytimes.base.AppComponent;

import dagger.Component;

@ActivityScope
@Component(modules = {DetailsModule.class}, dependencies = AppComponent.class)
public interface DetailsComponent {
    void injectActivity(DetailsActivity activity);
}
