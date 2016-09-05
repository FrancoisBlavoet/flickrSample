package com.flickrtest.mainscreen;

import com.flickrtest.application.ApplicationComponent;
import com.flickrtest.commons.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = MainScreenPresenterModule.class, dependencies = ApplicationComponent.class)
public interface MainScreenComponent {

    void inject(MainScreenFragment fragment);
}